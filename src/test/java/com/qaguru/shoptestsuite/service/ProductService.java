package com.qaguru.shoptestsuite.service;

import com.qaguru.shoptestsuite.models.Product;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.testng.Assert;

import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class ProductService {
    private RequestSpecification requestSpecification;

    public ProductService(RequestSpecification requestSpecification) {
        this.requestSpecification = requestSpecification;
    }

    public String createANewRecord(Product product, int expStatCode){
        ValidatableResponse response =
                    given()
                        .spec(requestSpecification)
                        .body(product)
                    .when()
                        .post("")
                    .then()
                        .log().all()
                        .assertThat().statusCode(expStatCode)
                        .assertThat().header("Location",containsString("/api/v1/products/"));
        String location = response.extract().header("Location");
        String id = location.substring("/api/v1/products/".length());
        return id;
    }
    public void updateRecord(Product product){

        given()
            .spec(requestSpecification)
            .body(product)
        .when()
            .put("/" + product.getId())
        .then()
            .log().all()
            .assertThat().statusCode(HttpStatus.SC_NO_CONTENT);
    }

    public Product getRecord(String id, int statusCode){
        ExtractableResponse response =
        given()
            .spec(requestSpecification)
        .when()
            .get("/" + id)
        .then()
            .log().all()
            .assertThat().statusCode(statusCode)
                .extract();
        Product product = new Product();
        if(response.statusCode() == HttpStatus.SC_OK){
            product = response.body().as(Product.class);
        }
        return product;

    }
    public void deleteRecord(String id){

        given()
                .spec(requestSpecification)
                .when()
                .delete("/" + id)
                .then()
                .log().all()
                .assertThat().statusCode(HttpStatus.SC_NO_CONTENT);
    }


    public void findAllRecords(List<Product> expProducts) {
        List<Product> actProducts = List.of(given()
                .spec(requestSpecification)
                .when()
                .get()
                .then()
                .log().all()
                .assertThat().statusCode(HttpStatus.SC_OK)
                .extract().body().as(Product[].class));

        for (Product expProd:expProducts){

            for(Product actProduct: actProducts){
                if(actProduct.getId().equals(expProd.getId())){
                    Assert.assertEquals(actProduct.getName(),expProd.getName());
                    Assert.assertEquals(actProduct.getDescription(),expProd.getDescription());
                    Assert.assertEquals(actProduct.getPrice(),expProd.getPrice());
                }
            }
//            Assert.assertTrue(actProducts.contains(expProd),"The product "+expProd + " is missing");
        }

    }
}

