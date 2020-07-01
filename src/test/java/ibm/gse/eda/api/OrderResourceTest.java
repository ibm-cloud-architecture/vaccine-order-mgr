package ibm.gse.eda.api;

import static ibm.gse.eda.api.MongoDbContainer.MONGODB_HOST;
import static ibm.gse.eda.api.MongoDbContainer.MONGODB_PORT;
import static ibm.gse.eda.api.MongoDbContainer.MONGODB_MAPPED_PORT;
import static io.restassured.config.LogConfig.logConfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import ibm.gse.eda.api.dto.VaccineOrder;
import ibm.gse.eda.vaccines.domain.OrderStatus;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.assertj.core.api.Assertions;

@Testcontainers
@QuarkusTest
public class OrderResourceTest {

    @Container
    static GenericContainer MONGO_DB_CONTAINER = new MongoDbContainer()
            .withCreateContainerCmdModifier(cmd -> cmd.withHostName(MONGODB_HOST)
                    .withPortBindings(new PortBinding(Ports.Binding.bindPort(MONGODB_PORT), new ExposedPort(MONGODB_MAPPED_PORT))));

    @BeforeAll
    static void initAll() {
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.config
                .logConfig((logConfig().enableLoggingOfRequestAndResponseIfValidationFails()))
                .objectMapperConfig(new ObjectMapperConfig().jackson2ObjectMapperFactory((type, s) -> new ObjectMapper()
                        .registerModule(new Jdk8Module())
                        .registerModule(new JavaTimeModule())
                        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)));
    }
    @Test
    public void testOrderCRUD() {
        String order1 = "{ \"deliveryLocation\" : \"Paris\",\"quantity\" : \"2\", \"status\" : \"OPEN\", \"priority\" : \"1\" , \"deliveryDate\" : \"2020-09-30\" }";
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(order1)
                .when()
                .post("/orders")
                .then()
                .statusCode(201);

        String order2 = "{ \"id\" : \"5889273c093d1c3e614bf2fa\",\"deliveryLocation\" : \"Hanoi\",\"quantity\" : \"1\", \"status\" : \"OPEN\", \"priority\" : \"1\" , \"deliveryDate\" : \"2020-08-30\" }";
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(order2)
                .when()
                .post("/orders")
                .then()
                .statusCode(201);

        VaccineOrder order = RestAssured
                .given()
                .when()
                .contentType(ContentType.JSON)
                .get("/orders/search/{destination}", "Paris")
                .then()
                .statusCode(200)
                .extract()
                .body().as(VaccineOrder.class);

        Assertions.assertThat(order.deliveryLocation).isEqualTo("Paris");
        Assertions.assertThat(order.status).isEqualTo(OrderStatus.OPEN);

        order = RestAssured
                .given()
                .when()
                .contentType(ContentType.JSON)
                .get("/orders/{id}", "5889273c093d1c3e614bf2fa")
                .then()
                .statusCode(200)
                .extract()
                .body().as(VaccineOrder.class);

        Assertions.assertThat(order.id).isEqualTo("5889273c093d1c3e614bf2fa");
        Assertions.assertThat(order.deliveryLocation).isEqualTo("Hanoi");

        VaccineOrder[] persons = RestAssured.given()
                .when()
                .contentType(ContentType.JSON)
                .get("/orders")
                .then()
                .statusCode(200)
                .extract()
                .body().as(VaccineOrder[].class);

        Assertions.assertThat(persons.length).isEqualTo(2);
        Long count = RestAssured
                .given()
                .when()
                .contentType(ContentType.JSON)
                .get("/orders/count")
                .then()
                .statusCode(200)
                .extract()
                .body().as(Long.class);

        Assertions.assertThat(count).isGreaterThan(1);
    }

}