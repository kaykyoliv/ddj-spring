package com.kayky.controller;

import com.kayky.commons.FileUtils;
import com.kayky.config.IntegrationTestConfig;
import com.kayky.config.TestRestTemplateConfig;
import com.kayky.response.ProfileGetResponse;
import com.kayky.response.ProfilePostResponse;
import net.javacrumbs.jsonunit.assertj.JsonAssertions;
import net.javacrumbs.jsonunit.core.Option;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestRestTemplateConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Sql(value = "/sql/user/init_one_login_regular_user.sql")
@Sql(value = "/sql/user/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SqlMergeMode(value = SqlMergeMode.MergeMode.MERGE )
class ProfileControllerIT extends IntegrationTestConfig {

    private static final String URL = "/v1/profiles";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FileUtils fileUtils;

    @Test
    @DisplayName("GET v1/profiles returns a list with all profiles")
    @Sql(value = "/sql/profile/init_two_profiles.sql", executionPhase =  Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/profile/clean_profiles.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(1)
    void findAll_ReturnsAllProfiles_WhenSuccessful() throws Exception {
        var typeReference = new ParameterizedTypeReference<List<ProfileGetResponse>>() {
        };

        var responseEntity = restTemplate.exchange(URL, GET, null, typeReference);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotEmpty().doesNotContainNull();

        responseEntity.getBody()
                .forEach(profileGetResponse -> assertThat(profileGetResponse).hasNoNullFieldsOrProperties());
    }

    @Test
    @DisplayName("GET v1/profiles returns empty list when nothing is not found")
    @Order(2)
    void findAll_ReturnsEmptyList_WhenNothingIsNotFound() {
        var typeReference = new ParameterizedTypeReference<List<ProfileGetResponse>>() {
        };

        var responseEntity = restTemplate.exchange(URL, GET, null, typeReference);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("POST v1/profiles creates an profile")
    @Order(3)
    void save_CreatesProfile_WhenSuccessful() throws Exception {
        var request = fileUtils.readResourceFile("profile/post-request-profile-200.json");
        var profileJson = buildHttpEntity(request);

        var responseEntity = restTemplate.exchange(URL, POST, profileJson, ProfilePostResponse.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody()).isNotNull().hasNoNullFieldsOrProperties();
    }

    private static HttpEntity<String> buildHttpEntity(String request) {
        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(request, httpHeaders);
    }

    @ParameterizedTest
    @MethodSource("postProfileBadRequestSource")
    @DisplayName("POST v1/profiles returns bad request when fields are invalid")
    @Order(4)
    void save_ReturnsBadRequest_WhenFieldsAreInvalid(String requestFile, String responseFile) throws Exception {

        var request = fileUtils.readResourceFile("profile/%s".formatted(requestFile));
        var expectedResponse = fileUtils.readResourceFile("profile/%s".formatted(responseFile));

        var profileJson = buildHttpEntity(request);

        var responseEntity = restTemplate.exchange(URL, POST, profileJson, String.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        JsonAssertions.assertThatJson(responseEntity.getBody())
                .whenIgnoringPaths("timestamp")
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(expectedResponse);

    }

    private static Stream<Arguments> postProfileBadRequestSource() {
        return Stream.of(
                Arguments.of("post-request-profile-empty-fields-400.json", "post-response-profile-empty-fields-400.json"),
                Arguments.of("post-request-profile-blank-fields-400.json", "post-response-profile-blank-fields-400.json")
        );
    }

}
