# Vaccine-order-mgr project

See explanation of this application in [this article](https://pages.github.ibm.com/vaccine-cold-chain/vaccine-cold-chain-main/solution/orderms/)

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell
./mvnw quarkus:dev
```

## Packaging and running the application

The application can be packaged using `./mvnw package`.
It produces the `vaccine-order-mgr-1.0-SNAPSHOT-runner.jar` file in the `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

The application is now runnable using `java -jar target/vaccine-order-mgr-1.0-SNAPSHOT-runner.jar`.

## Build docker image for Openshift

```shell
./mvnw clean package -Dquarkus.container-image.build=true
```

## Creating a native executable

You can create a native executable using: `./mvnw package -Pnative`.

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: `./mvnw package -Pnative -Dquarkus.native.container-build=true`.

You can then execute your native executable with: `./target/vaccine-order-mgr-1.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/building-native-image.