package com.kayky.config;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

import static com.kayky.commons.Constants.*;

@TestConfiguration
@Lazy
public class RestAssuredConfig {

    @LocalServerPort
    private int port;

    @Bean(name = "requestSpecificationRegularUser")
    public RequestSpecification requestSpecificationRegularUser(){
        return RestAssured.given()
                .baseUri(BASE_URI + port)
                .auth().basic(REGULAR_USERNAME, PASSWORD);
    }

    @Bean(name = "requestSpecificationAdminUser")
    public RequestSpecification requestSpecificationAdminUser() {
        return RestAssured.given()
                .baseUri(BASE_URI + port)
                .auth().basic(ADMIN_USERNAME, PASSWORD);
    }
}
