package com.qaguru.shoptestsuite.tests;

import com.qaguru.shoptestsuite.models.Product;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class ShopApiTests {
    private final String baseUri = "http://localhost:8086";
    private final String basePath = "/api/v1/products";
    @Test
    public void addANewProduct(){

        Product product = new Product();
        product.setName("One A Day");
        product.setDescription("Advanced multi-vitamin tablets");
        product.setPrice(12.7f);
//        System.out.println(product);
        //==========================
        ValidatableResponse response = given()
                .baseUri(baseUri)
                .basePath(basePath)
                .contentType(ContentType.JSON)
                .body(product)
                .log().all()
        .when()
                .post("")
        .then()
                .log().all()
                .assertThat().statusCode(HttpStatus.SC_CREATED)
                .assertThat().header("Location",containsString("/api/v1/products/"));
        String location = response.extract().header("Location");
        String id = location.substring("/api/v1/products/".length());
        //Invoke Get end point and check the details
        Product actResponseBody = given()
                .baseUri(baseUri)
                .basePath(basePath)
                .log().all()
        .when()
                .get("/" + id)
        .then()
                .log().all()
                .assertThat().statusCode(HttpStatus.SC_OK)
                .extract().body().as(Product.class);

        Assert.assertEquals(actResponseBody.getName(), product.getName());
        Assert.assertEquals(actResponseBody.getDescription(), product.getDescription());

    }

}
