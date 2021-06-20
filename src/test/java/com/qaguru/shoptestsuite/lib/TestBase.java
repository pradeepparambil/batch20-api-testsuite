package com.qaguru.shoptestsuite.lib;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class TestBase {
    private final String baseUri = "http://localhost:8086";
    private final String basePath = "/api/v1/products";
    protected RequestSpecification requestSpecification;

    public TestBase() {
        requestSpecification = new RequestSpecBuilder()
                .setBaseUri(baseUri)
                .setBasePath(basePath)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
    }
}
