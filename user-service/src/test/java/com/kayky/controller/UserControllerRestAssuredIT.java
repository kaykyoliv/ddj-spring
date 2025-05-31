package com.kayky.controller;

import com.kayky.commons.FileUtils;
import com.kayky.commons.UserUtils;
import com.kayky.config.IntegrationTestConfig;
import com.kayky.config.RestAssuredConfig;
import com.kayky.exception.NotFoundException;
import com.kayky.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import net.javacrumbs.jsonunit.assertj.JsonAssertions;
import net.javacrumbs.jsonunit.core.Option;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = RestAssuredConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerRestAssuredIT extends IntegrationTestConfig {

    private static final String URL = "/v1/users";

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private FileUtils fileUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository repository;

    @Autowired
    @Qualifier(value = "requestSpecificationRegularUser")
    private RequestSpecification requestSpecificationRegularUser;

    @Autowired
    @Qualifier(value = "requestSpecificationAdminUser")
    private RequestSpecification requestSpecificationAdminUser;

    @BeforeEach
    void setUrl() {
        RestAssured.requestSpecification = requestSpecificationRegularUser;
    }

    @Test
    @DisplayName("GET v1/users returns a list with all users when argument is null")
    @Sql(value = "/sql/user/init_three_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/user/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(1)
    void findAll_returnsAllUsers_WhenArgumentIsNull() {
        RestAssured.requestSpecification = requestSpecificationAdminUser;

        var expectedResponse = fileUtils.readResourceFile("user/get-user-null-first-name-200.json");

        var response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract().response().body().asString();

        JsonAssertions.assertThatJson(response)
                .and(users -> {
                    users.node("[0].id").asNumber().isPositive();
                    users.node("[1].id").asNumber().isPositive();
                    users.node("[2].id").asNumber().isPositive();
                });

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("[*].id")
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("GET v1/users?firstName=Toyohisa returns list with found object when first name exists")
    @Sql(value = "/sql/user/init_three_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/user/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(2)
    void findAll_ReturnsFoundUserInList_WhenFirstNameIsFound() {
        RestAssured.requestSpecification = requestSpecificationAdminUser;

        var expectedResponse = fileUtils.readResourceFile("user/get-user-toyohisa-first-name-200.json");
        var firstName = "Toyohisa";

        var response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .queryParam("firstName", firstName)
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract().response().body().asString();

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("[*].id")
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("GET v1/users?firstName=Toyohisa returns empty list when first name is not found   ")
    @Sql(value = "/sql/user/init_three_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/user/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(3)
    void findAll_ReturnsEmptyList_WhenFirstNameIsNotFound() {
        RestAssured.requestSpecification = requestSpecificationAdminUser;

        var expectedResponse = fileUtils.readResourceFile("user/get-user-x-first-name-200.json");
        var firstName = "x";

        var response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .queryParam("firstName", firstName)
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract().response().body().asString();

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("[*].id")
                .isEqualTo(expectedResponse);
    }


    @Test
    @DisplayName("GET v1/users/1 returns an user with given id")
    @Sql(value = "/sql/user/init_three_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/user/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(4)
    void findById_ReturnsUserById_WhenSuccessful() throws Exception {
        RestAssured.requestSpecification = requestSpecificationAdminUser;

        var expectedResponse = fileUtils.readResourceFile("user/get-user-by-id-200.json");
        var users = repository.findByFirstNameIgnoreCase("Toyohisa");

        assertThat(users).hasSize(1);


        var response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .pathParam("id", users.getFirst().getId())
                .get(URL + "/{id}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract().response().body().asString();

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("id")
                .isEqualTo(expectedResponse);
    }


    @Test
    @DisplayName("GET v1/users/99 throws NotFound 404 when user is not found")
    @Sql(value = "/sql/user/init_three_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/user/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(5)
    void findById_ThrowsNotFound_WhenUserIsNotFound() throws Exception {
        var expectedResponse = fileUtils.readResourceFile("user/get-user-by-id-404.json");

        var id = 99L;

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .pathParam("id", id)
                .get(URL + "/{id}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body(Matchers.equalTo(expectedResponse))
                .log().all();
    }

    @Test
    @DisplayName("POST v1/users creates an user")
    @Sql(value = "/sql/user/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(6)
    void save_CreatesUser_WhenSuccessful() throws Exception {
        var request = fileUtils.readResourceFile("user/post-request-user-200.json");
        var expectedResponse = fileUtils.readResourceFile("user/post-response-user-201.json");

        var response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .body(request)
                .post(URL)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .log().all()
                .extract().response().body().asString();

        JsonAssertions.assertThatJson(response)
                .node("id")
                .asNumber()
                .isPositive();

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("id")
                .isEqualTo(expectedResponse);
    }


    @Test
    @DisplayName("DELETE v1/users/1 removes an user")
    @Sql(value = "/sql/user/init_one_login_admin_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/user/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(7)
    void delete_RemoveUser_WhenSuccessful() throws Exception {
        RestAssured.requestSpecification = requestSpecificationAdminUser;

        var id = repository.findAll().getFirst().getId();

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .pathParam("id", id)
                .delete(URL + "/{id}")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .log().all();
    }



    @Test
    @DisplayName("DELETE v1/users/1 removes an user")
    @Sql(value = "/sql/user/init_one_login_admin_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/user/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(8)
    void delete_ThrowsNotFound_WhenUserIsNotFound() throws Exception {
        RestAssured.requestSpecification = requestSpecificationAdminUser;

        var expectedResponse = fileUtils.readResourceFile("user/delete-user-by-id-404.json");

        var id = 99L;

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .pathParam("id", id)
                .delete(URL + "/{id}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body(Matchers.equalTo(expectedResponse))
                .log().all();
    }

    @Test
    @DisplayName("PUT v1/users updates an user")
    @Sql(value = "/sql/user/init_one_login_regular_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/user/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(9)
    void update_UpdatesUser_WhenSuccessful() throws Exception {

        var request = fileUtils.readResourceFile("user/put-request-user-200.json");
        var users = repository.findByFirstNameIgnoreCase("Ash");

        var oldUser = users.getFirst();

        assertThat(users).hasSize(1);

        request = request.replace("1", users.getFirst().getId().toString());

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .body(request)
                .when()
                .put(URL)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .log().all();

        var updatedUser = repository.findById(oldUser.getId()).orElseThrow(() -> new NotFoundException("user not found"));

        var encryptedPassword = updatedUser.getPassword();

        assertThat(passwordEncoder.matches("leigan", encryptedPassword)).isTrue();
    }

    @Test
    @DisplayName("PUT v1/users throws NotFound when user is not found")
    @Sql(value = "/sql/user/init_one_login_regular_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/user/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(10)
    void update_ThrowsNotFound_WhenUserIsNotFound() throws Exception {
        var request = fileUtils.readResourceFile("user/put-request-user-404.json");
        var expectedResponse = fileUtils.readResourceFile("user/put-user-by-id-404.json");

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .body(request)
                .put(URL)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body(Matchers.equalTo(expectedResponse))
                .log().all();
    }

    @ParameterizedTest
    @MethodSource("postUserBadRequestSource")
    @DisplayName("POST v1/users returns bad request when fields are invalid")
    @Sql(value = "/sql/user/init_one_login_regular_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/user/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(11)
    void save_ReturnsBadRequest_WhenFieldsAreInvalid(String requestFile, String responseFile) throws Exception {
        var request = fileUtils.readResourceFile("user/%s".formatted(requestFile));
        var expectedResponse = fileUtils.readResourceFile("user/%s".formatted(responseFile));

        var response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .body(request)
                .when()
                .post(URL)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .log().all()
                .extract().response().body().asString();


        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("timestamp")
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(expectedResponse);
    }

    @ParameterizedTest
    @MethodSource("putUserBadRequestSource")
    @DisplayName("PUT v1/users returns bad request when fields are invalid")
    @Sql(value = "/sql/user/init_one_login_regular_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/user/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(12)
    void update_ReturnsBadRequest_WhenFieldsAreInvalid(String requestFile, String responseFile) throws Exception {
        var request = fileUtils.readResourceFile("user/%s".formatted(requestFile));
        var expectedResponse = fileUtils.readResourceFile("user/%s".formatted(responseFile));

        var response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .body(request)
                .when()
                .put(URL)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .log().all()
                .extract().response().body().asString();


        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("timestamp")
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(expectedResponse);
    }


    private static Stream<Arguments> postUserBadRequestSource() {

        return Stream.of(
                Arguments.of("post-request-user-empty-fields-400.json", "post-response-user-empty-fields-400.json"),
                Arguments.of("post-request-user-blank-fields-400.json", "post-response-user-blank-fields-400.json"),
                Arguments.of("post-request-user-invalid-email-400.json", "post-response-user-invalid-email-400.json")
        );
    }

    private static Stream<Arguments> putUserBadRequestSource() {
        return Stream.of(
                Arguments.of("put-request-user-empty-fields-400.json", "put-response-user-empty-fields-400.json"),
                Arguments.of("put-request-user-blank-fields-400.json", "put-response-user-blank-fields-400.json"),
                Arguments.of("put-request-user-invalid-email-400.json", "put-response-user-invalid-email-400.json")
        );
    }

}
