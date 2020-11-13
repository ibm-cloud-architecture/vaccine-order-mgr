package it;

import static io.restassured.RestAssured.given;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import org.junit.jupiter.api.Test;

import ibm.gse.eda.vaccines.api.dto.VaccineOrderRequest;
import ibm.gse.eda.vaccines.domain.OrderStatus;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
public class OrderMgtHappyPathTest {
    String ORDER_URL = "http://localhost:8080/orders";

    @Test
    public void createOrder(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        String date = "18/08/2020";
        VaccineOrderRequest order = new VaccineOrderRequest(1l, 
            "Paris, France",
            "France",
            10l,
            1, 
            LocalDate.parse(date).toString(),
            "Covid=19", 
            OrderStatus.OPEN);
        
        Jsonb jsonb = JsonbBuilder.create();

        given()
            .contentType(ContentType.JSON)
            .body(jsonb.toJson(order))
            .when()
            .post(ORDER_URL)
            .then()
            .statusCode(200)
            ;
    }
}