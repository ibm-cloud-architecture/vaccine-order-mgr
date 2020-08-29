# Vaccine Order Manager event-driven microservice

This service is responsible to manage the Vaccine Order entity. It is done with Smallrye microprofile and reactive messaging with Kafka, hibernate ORM with panache for DB2 database, Vert.x with reactive route, Appsody Quarkus stack and Tekton pipeline.

For detail implementation approach and design and different deployment model, read explanations of this service in [the main solution documentation](https://ibm-cloud-architecture.github.io/vaccine-solution-main/solution/orderms/).


## Appsody pre-requisites

You need to install the Appsody CLI for [your operating system.](https://appsody.dev/docs/installing/installing-appsody)

Then you need to add the Appsody repository we have started to develop for our own EDA stacks, as this project references this stack.

```shell
appsody repo add ibmcase https://raw.githubusercontent.com/ibm-cloud-architecture/appsody-stacks/master/ibmcase-index.yaml
# Verify you have ibmcase stacks listed
appsody repo list
```

## Build and run locally

To deploy on OpenShift cluster see instruction in [main documentation](https://ibm-cloud-architecture.github.io/vaccine-solution-main/solution/orderms/).

To run locally we have defined a docker-compose file with a simple one broker Kafka node based on Strimzi Kafka 2.5 image.

Start Kafka backend: `docker-compose up` in one Terminal window.

Start the app in development mode: `./mvnw compile quarkus:dev`.

You can also generate the native executable with `./mvnw clean package -Pnative`.

Use the following address to post an order using Swagger UI [http://localhost:8080/swagger-ui/#/default/post_orders](http://localhost:8080/swagger-ui/#/default/post_orders) or use the end to end testing as:

```shell
cd e2e
./post-order.sh
```

## Tekton pipeline




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