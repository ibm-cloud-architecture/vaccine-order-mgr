# Vaccine Order Manager event-driven microservice

This service is responsible to manage the Vaccine Order entity. It is done with Smallrye microprofile and reactive messaging with Kafka, hibernate ORM with panache for DB2 database, Vert.x with reactive route, Appsody Quarkus stack and Tekton pipeline.

For detail implementation approach, design and different deployment model, read explanations of this service in [the main solution documentation](https://ibm-cloud-architecture.github.io/vaccine-solution-main/solution/orderms/).

The goals of this project are:

* Quarkus app with [Debezium outbox](https://debezium.io/documentation/reference/integrations/outbox.html) extension
* Reactive APP with Mutiny
* JPA with Panache
* DB2 settings for change data capture
* Debezium DB2 connector to publish OrderEvents to Kafka topic

## Build and run locally

### Prepare DB2 for Debezium CDC

As the environment is using a DB2 database configured for change data capture, a specific Db2 image is necessary. We have integrated a [dockerfile](https://github.com/ibm-cloud-architecture/vaccine-order-mgr/blob/master/environment/db2image/Dockerfile) and asn configuration to build such image. The details for this configuration are in the [Debezium Db2 connector documentation](https://debezium.io/documentation/reference/connectors/db2.html).

If you want to build this image you can do the following command.

 ```shell
 # under environment/db2image
 docker build -t ibmcase/db2orders .
 # push the image to a registry
 docker push ibmcase/db2orders
 ```cd ..


 Then start db2 to prepare the database for the first time only.

 ```shell
 docker-compose -f strimzi-docker-compose.yaml up db2 -d
 ```

In the logs you should see the execution of the [cdcsetup.sh](https://github.com/ibm-cloud-architecture/vaccine-order-mgr/blob/master/environment/db2image/cdcsetup.sh) script which maps to the steps described in [this Debezium note](https://debezium.io/documentation/reference/connectors/db2.html#setting-up-db2)

To run locally we have defined a [docker-compose file](https://github.com/ibm-cloud-architecture/vaccine-order-mgr/blob/master/environment/docker-compose.yaml) with one Kafka broker, one zookeeper, one DB2 container for the persistence, one Kafka Connect with the Debezium code with the DB2 Jdbc driver and one vaccine-order-service to support the demonstration.


* To validate DB2 settings you can do one of the following troubleshooting commands:

```shell
# connect to DB2 server
docker exec -ti db2 bash -c "su - db2inst1"
# Access the database 
db2 connect to TESTDB user db2inst1
# list existing schemas
 db2 "select * from syscat.schemata"
# list tables
db2 list tables
# this is the outcomes if the order services was started
Table/View                      Schema          Type  Creation time             
------------------------------- --------------- ----- --------------------------
ORDERCREATEDEVENT               DB2INST1        T     2020-11-12-01.50.10.400490
ORDEREVENTS                     DB2INST1        T     2020-11-12-01.50.10.650172
ORDERUPDATEDEVENT               DB2INST1        T     2020-11-12-01.50.10.796566
VACCINEORDERENTITY              DB2INST1        T     2020-11-12-01.50.10.874172
# Verify the content of the current orders
db2 "select * from vaccineorderentity"
# List the table for the change data capture schema
db2 list tables for schema asncdc
```


* To deploy this DB2 image on OpenShift cluster do the following steps:

```shell
oc login ...
oc new-project eda-db2
# Define PV and PVC to persist data
oc apply -f environment/db2image/db2orders-pvc.yaml
# Authorize default user using security constraint
oc adm policy add-scc-to-user anyuid -z default
# Deploy statefulset for the db2 image
oc apply -f  environment/db2image/statefulset-db2orders.yaml

```

### Build vaccine order mgr service

 ```shell
 # be sure to have packaged the order app first with
 mvn package -Dui.deps -Dui.dev -D -DskipTests
 # build the image
 docker build -f src/main/docker/Dockerfile.jvm -t ibmcase/vaccineorderms  .
 # can also build with docker compose
 cd environment
 # with the option to build the db2, and debezium cdc container images
 docker-compose -f strimzi-docker-compose.yaml up -d --build
 # or with pre-existing images coming from dockerhub or your local registry if images already built
 docker-compose -f strimzi-docker-compose.yaml up -d
 # As an ALTERNATE you can use confluent kafka
 ```

 If you want to start everything in development mode, the vaccine order service is executed via a maven container which starts `quarkus:dev`. Therefore the command is using another compose file: 

 ```shell
 cd environment
 docker-compose -f dev-docker-compose.yaml up -d [--build]
 ```

 When started for the first time, the DB2 container may take some time to complete the TESTDB creation with the change data capture tables. (To do need to find a health check way to assess db2 is running)


* Define Kafka topics

Some topics are created bu Kafka Connector.

```shell
# Under environment folder
./createTopic.sh
# validate topics created
./listTopics.sh

__consumer_offsets
db_history_vaccine_orders
src_connect_configs
src_connect_offsets
src_connect_statuses
vaccine_shipments
```

The `db_history_vaccine_orders` is the topic used to include database schema change on the vaccine orders table. 

* Deploy and start the Debezium DB2 connector. The connector definition is [register-db2.json](https://github.com/ibm-cloud-architecture/vaccine-order-mgr/blob/master/environment/cdc/register-db2.json)

 ```shell
 # under environment/cdc
 curl -i -X POST -H "Accept:application/json" -H  "Content-Type:application/json" http://localhost:8083/connectors/ -d @cdc/register-db2.json
 ```

* Verify Topics created by the connector:

 ```shell
  ./listTopics.sh 
  vaccine_lot_db
  vaccine_lot_db.DB2INST1.ORDEREVENTS
 ```

The newly created `vaccine_lot_db` topic includes definition of the database and the connector. It does not aim to be used by application. The one to be used to get business events is `vaccine_lot_db.DB2INST1.ORDEREVENTS`.

The connector is doing a snapshot of the `DB2INST1.ORDEREVENTS` table to send existing records to the topic.

* Start a consumer on the CDC topic for the order events

 ```shell
 docker-compose exec kafka /opt/kafka/bin/kafka-console-consumer.sh     --bootstrap-server kafka:9092     --from-beginning     --property print.key=true     --topic vaccine_lot_db.DB2INST1.ORDEREVENTS
 ```

* Add new order from the user interface: http://localhost:8080/#/Orders, or...

* Post an order using the API: [http://localhost:8080/swagger-ui/#/default/post_orders](http://localhost:8080/swagger-ui/#/default/post_orders). Use the following JSON

 ```json
 {
    "deliveryDate": "2021-07-25",
    "deliveryLocation": "Milano",
    "askingOrganization": "Italy gov",
    "priority": 1,
    "quantity": 100,
    "type": "COVID-19"
 }
 ```

 * The expected result should have the following records in the Kafka topic:

 ```json
 {"ID":"lvz4gYs/Q+aSqKmWjVGMXg=="}	
 {"before":null,"after":{"ID":"lvz4gYs/Q+aSqKmWjVGMXg==","AGGREGATETYPE":"VaccineOrderEntity","AGGREGATEID":"21","TYPE":"OrderCreated","TIMESTAMP":1605304440331350,"PAYLOAD":"{\"orderID\":21,\"deliveryLocation\":\"London\",\"quantity\":150,\"priority\":2,\"deliveryDate\":\"2020-12-25\",\"askingOrganization\":\"UK Governement\",\"vaccineType\":\"COVID-19\",\"status\":\"OPEN\",\"creationDate\":\"13-Nov-2020 21:54:00\"}"},"source":{"version":"1.3.0.Final","connector":"db2","name":"vaccine_lot_db","ts_ms":1605304806596,"snapshot":"last","db":"TESTDB","schema":"DB2INST1","table":"ORDEREVENTS","change_lsn":null,"commit_lsn":"00000000:0000150f:0000000000048fca"},"op":"r","ts_ms":1605304806600,"transaction":null}
 ```


### Package the order service with docker

If you want to build each images manually:

* Db2 with ASN table and scripts for CDC

 ```shell
  cd environment/db2images
  docker build -t ibmcase/db2orders .
 ```

* Build the Debezium Kafka Connector

 ```shell
  cd environment/db2images
  docker build -t ibmcase/cdc-connector .
 ```

* Build the vaccine order service

 ```shell
  # from project folder
  mvn package -Dui.deps -Dui.dev -DskipTests
  docker build  -f src/main/docker/Dockerfile.jvm -t ibmcase/vaccineorderms .
 ```


## Tests

Unit and integration tests are done with Junit 5 and Test Container when needed or mockito to avoid backend access for CI/CD.

For end to end testing the `e2e` folder includes some python scripts to test new order creation.

```shell
cd e2e
./post-order.sh
```

## UI development

For UI development start the components with `docker-compose  -f dev-docker-compose.yaml up -d`, then under the ui folder, do the following:

```
yarn install
yarn serve
```

Use the web browser and developer console to the address [http://localhost:4545](http://localhost:4545). The vue app is configured to proxy to `localhost:8080`.


## OpenShift deployment

To deploy on OpenShift cluster see instructions in the [main documentation](https://ibm-cloud-architecture.github.io/vaccine-solution-main/solution/orderms/).


## Troubleshooting

### Logs:

```shell
# microservice logs:

```
### Connector operations

To delete the CDC connector:

```
curl -i -X DELETE  http://localhost:8083/connectors/orderdb-connector
```

### Errors

#### com.ibm.db2.jcc.am.SqlException: DB2 SQL Error: SQLCODE=-1031, SQLSTATE=58031

DB2 may not have finished its setup, specially using docker compose where the image may be built with Debezium and change data capture settings.

#### A DRDA Data Stream Syntax Error was detected.  Reason: 0x2110. ERRORCODE=-4499, SQLSTATE=58009

This error may happen on OpenShift deployment.



## Git Action

This repository includes a Github [workflow](https://github.com/ibm-cloud-architecture/vaccine-order-mgr/blob/master/.github/workflows/dockerbuild.yaml) to build the app and push a new docker image to public registry. To do that we need to define 4 secrets in the github repository:

* DOCKER_IMAGE_NAME the image name to build. Here it is `vaccineorderms`
* DOCKER_USERNANE: user to access docker hub
* DOCKER_PASSWORD: and its password.
* DOCKER_REPOSITORY for example the organization we use is `ibmcase`
