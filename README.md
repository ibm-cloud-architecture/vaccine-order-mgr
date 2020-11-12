# Vaccine Order Manager event-driven microservice

This service is responsible to manage the Vaccine Order entity. It is done with Smallrye microprofile and reactive messaging with Kafka, hibernate ORM with panache for DB2 database, Vert.x with reactive route, Appsody Quarkus stack and Tekton pipeline.

For detail implementation approach and design and different deployment model, read explanations of this service in [the main solution documentation](https://ibm-cloud-architecture.github.io/vaccine-solution-main/solution/orderms/).


## Build and run locally

As the environment is using a DB2 configured for change data capture a specific Db2 image is necessary. We have integrated a dockerfile and asn configuration for a special image. The details on this configuration are in the [Debezium Db2 connector documentation]().

```shell
# under environment/db2image
docker build -t ibmcase/db2orders .
```
To run locally we have defined a [docker-compose file]() with one Kafka broker node one zookeeper, one DB2 container for the persistence.

Start Kafka backend from a terminal window as well as DB2 and maven with quarkus:dev: 

```
cd environment
docker-compose -f dev-docker-compose.yaml up -d
```

At the first start DB2 will take some time to complete as it creates the TESTDB and other setup.

* Deploy and start the Debezium DB2 connector

 ```shell
 # under environment/cdc
 curl -i -X POST -H "Accept:application/json" -H  "Content-Type:application/json" http://localhost:8083/connectors/ -d @register-db2.json
 ```

* Start a consumer on the CDC topic for the order events

 ```shell
 docker-compose -f dev-docker-compose.yaml exec kafka /opt/kafka/bin/kafka-console-consumer.sh     --bootstrap-server kafka:9092     --from-beginning     --property print.key=true     --topic db2.orders
 ```

* Post an order using the API: [http://localhost:8080/swagger-ui/#/default/post_orders](http://localhost:8080/swagger-ui/#/default/post_orders). Use the following JSON

 ```json

 ```

### Package for docker

```shell
  mvn package -Dui.deps -Dui.dev -Dquarkus.container-image.build=true
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
