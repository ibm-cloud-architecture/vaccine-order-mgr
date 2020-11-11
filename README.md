# Vaccine Order Manager event-driven microservice

This service is responsible to manage the Vaccine Order entity. It is done with Smallrye microprofile and reactive messaging with Kafka, hibernate ORM with panache for DB2 database, Vert.x with reactive route, Appsody Quarkus stack and Tekton pipeline.

For detail implementation approach and design and different deployment model, read explanations of this service in [the main solution documentation](https://ibm-cloud-architecture.github.io/vaccine-solution-main/solution/orderms/).


## Build and run locally

To run locally we have defined a [docker-compose file]() with one Kafka broker node one zookeeper, one DB2 container for the persistence.

Start Kafka backend from a terminal window as well as DB2 and maven with quarkus:dev: 

```
cd environment
docker-compose -f dev-docker-compose.yaml up --build -d
```

Use the following address to post an order using the Swagger UI [http://localhost:8080/swagger-ui/#/default/post_orders](http://localhost:8080/swagger-ui/#/default/post_orders).

### Package for docker

```shell
  mvn package quarkus:dev -Dui.deps -Dui.dev
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

If you need to connect to the local DB2 instance use the command:

```shell
docker exec -ti db2 bash -c "su - db2inst1"
# connect to the sample db
db2 connect to sample
# List the orders:
db2 "select * from VaccineOrderEntity"
db2 connect reset 
```