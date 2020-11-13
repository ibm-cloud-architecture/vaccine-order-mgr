# Vaccine Order Manager event-driven microservice

This service is responsible to manage the Vaccine Order entity. It is done with Smallrye microprofile and reactive messaging with Kafka, hibernate ORM with panache for DB2 database, Vert.x with reactive route, Appsody Quarkus stack and Tekton pipeline.

For detail implementation approach and design and different deployment model, read explanations of this service in [the main solution documentation](https://ibm-cloud-architecture.github.io/vaccine-solution-main/solution/orderms/).


## Build and run locally

As the environment is using a DB2 configured for change data capture, a specific Db2 image is necessary. We have integrated a dockerfile and asn configuration to build such image. The details on this configuration are in the [Debezium Db2 connector documentation](https://debezium.io/documentation/reference/connectors/db2.html).

If you want to build this image you can do the following command. The other way is to start all the components with the docker-compose and the ``--build` option.

 ```shell
 # under environment/db2image
 docker build -t ibmcase/db2cdc .
 ```

In the logs you should see the execution of the [cdcsetup.sh](https://github.com/ibm-cloud-architecture/vaccine-order-mgr/blob/master/environment/db2image/cdcsetup.sh) script which maps to the steps described in [this debezium note](https://debezium.io/documentation/reference/connectors/db2.html#setting-up-db2)

To run locally we have defined a [docker-compose file](https://github.com/ibm-cloud-architecture/vaccine-order-mgr/blob/master/environment/docker-compose.yaml) with one Kafka broker, one zookeeper, one DB2 container for the persistence, one Kafka Connect with the Debezium code and DB2 Jdbc driver and one vaccine-order-service. 

 ```shell
 cd environment
 # with the option to build the db2, and debezium cdc container images
 docker-compose up -d --build
 # or with pre-existing images coming from dockerhub
 docker-compose up -d
 ```

 If you want to start everything in development mode, the vaccine order service is executed via a maven container which starts `quarkus:dev`. Therefore the command is: 

 ```shell
 cd environment
 docker-compose -f dev-docker-compose.yaml up -d [--build]
 ```

 When started for the first time, the DB2 container may take some time to complete the TESTDB creation with the change data capture tables. (To do need to find a healthcheck way to assess db2 is running)

* To validate DB2 settings you can do one of the following troubleshooting commands:

```shell
# connect to DB2 server
# Access the database
db2 connect to TESTDB USER DB2INST1
# list tables
db2 list tables
# this is the outcomes if the order services was started
Table/View                      Schema          Type  Creation time             
------------------------------- --------------- ----- --------------------------
ORDERCREATEDEVENT               DB2INST1        T     2020-11-12-01.50.10.400490
ORDEREVENTS                     DB2INST1        T     2020-11-12-01.50.10.650172
ORDERUPDATEDEVENT               DB2INST1        T     2020-11-12-01.50.10.796566
VACCINEORDERENTITY              DB2INST1        T     2020-11-12-01.50.10.874172
# Verify the content of the orders
db2 "select * from vaccineorderentity"
```

* Define Kafka topics

```
```

* Deploy and start the Debezium DB2 connector. The connector definition is [register-db2.json](https://github.com/ibm-cloud-architecture/vaccine-order-mgr/blob/master/environment/cdc/register-db2.json)

 ```shell
 # under environment/cdc
 curl -i -X POST -H "Accept:application/json" -H  "Content-Type:application/json" http://localhost:8083/connectors/ -d @cdc/register-db2.json
 ```

* Verify Topics created

 ```shell
  ./listTopics.sh 
  __consumer_offsets
  db2
  db2.DB2INST1.VACCINEORDERENTITY
  db2.orders
  src_connect_configs
  src_connect_offsets
  src_connect_statuses
 ```

* Start a consumer on the CDC topic for the order events

 ```shell
 docker-compose -f docker-compose.yaml exec kafka /opt/kafka/bin/kafka-console-consumer.sh     --bootstrap-server kafka:9092     --from-beginning     --property print.key=true     --topic db2.DB2INST1.VACCINEORDERENTITY
 ```

* Post an order using the API: [http://localhost:8080/swagger-ui/#/default/post_orders](http://localhost:8080/swagger-ui/#/default/post_orders). Use the following JSON

 ```json

 ```

### Package the oder service with docker

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

use the end to end testing as:

```shell
cd e2e
./post-order.sh
```

## UI development

Under the ui folder, do the following:

```
yarn install
yarn serve
```

Use the web browser and developer console to the address [http://localhost:4545](http://localhost:4545)




## Openshift deployment

To deploy on OpenShift cluster see instruction in [main documentation](https://ibm-cloud-architecture.github.io/vaccine-solution-main/solution/orderms/).


## Troubleshooting

If you need to connect to the local DB2 instance use the following commands to first connect to DB2 server and then use db2 CLI:

```shell
docker exec -ti db2 bash -c "su - db2inst1"
# connect to the  db
db2 connect to TESTDB
# list tables
 db2 list tables
# List the existing orders:
db2 "select * from VaccineOrderEntity"
# Look at the table structure
db2 describe table DB2INST1.ORDEREVENTS

db2 "select * from ORDEREVENTS"

db2 connect reset 
```


### Errors

#### com.ibm.db2.jcc.am.SqlException: DB2 SQL Error: SQLCODE=-1031, SQLSTATE=58031

DB2 may not have finished its setup
