package com.kayky.controller;

import com.kayky.response.ProfileGetResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProfileControllerIt {

    private static final String URL = "/v1/profiles";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("GET v1/profiles returns a list with all profiles")
    @Sql(value = "/sql/init_two_profiles.sql")
    @Order(1)
    void findAll_ReturnsAllProfiles_WhenSuccessful() throws Exception {
        var typeReference = new ParameterizedTypeReference<List<ProfileGetResponse>>() {
        };

        var responseEntity = restTemplate.exchange(URL, HttpMethod.GET, null, typeReference);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull().doesNotContainNull();

        responseEntity.getBody()
                .forEach(profileGetResponse -> assertThat(profileGetResponse).hasNoNullFieldsOrProperties());
    }

}
