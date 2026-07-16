package com.sparta.endpointtesting.utils;

import com.sparta.endpointtesting.pojoconfig.pojos.AccountResponse;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CreateAccountTest {
    private static Response validResponse;
    private static Response missingEmailResponse;

    private static AccountResponse validAccountPojo;
    private static AccountResponse missingEmailPojo;

    @BeforeAll
    static void beforeAll() {

        RestAssured.defaultParser = Parser.JSON;

        String uniqueEmail =
                "testuser" + System.currentTimeMillis() + "@test.com";

        validResponse = RestAssured
                .given()
                .spec(Helper.createAccountRequest(
                        "Test User",
                        uniqueEmail,
                        "Password123!"
                ))
                .when()
                .post()
                .then()
                .log().all()
                .extract()
                .response();

        validAccountPojo =
                validResponse.as(AccountResponse.class);

        missingEmailResponse = RestAssured
                .given()
                .spec(Helper.createAccountWithoutEmailRequest(
                        "Test User",
                        "Password123!"
                ))
                .when()
                .post()
                .then()
                .log().all()
                .extract()
                .response();

        missingEmailPojo =
                missingEmailResponse.as(AccountResponse.class);
    }

    @Test
    @DisplayName("Valid registration returns response code 201")
    void validRegistrationReturnsResponseCode201() {

        // MatcherAssert.assertThat(
        //         validResponse.jsonPath().getInt("responseCode"),
        //         Matchers.is(201)
        // );

        MatcherAssert.assertThat(
                validAccountPojo.getResponseCode(),
                Matchers.is(201)
        );
    }

    @Test
    @DisplayName("Valid registration returns User created message")
    void validRegistrationReturnsUserCreatedMessage() {

        // MatcherAssert.assertThat(
        //         validResponse.jsonPath().getString("message"),
        //         Matchers.is("User created!")
        // );

        MatcherAssert.assertThat(
                validAccountPojo.getMessage(),
                Matchers.is("User created!")
        );
    }

    @Test
    @DisplayName("Missing email returns response code 400")
    void missingEmailReturnsResponseCode400() {

        // MatcherAssert.assertThat(
        //         missingEmailResponse.jsonPath().getInt("responseCode"),
        //         Matchers.is(400)
        // );

        MatcherAssert.assertThat(
                missingEmailPojo.getResponseCode(),
                Matchers.is(400)
        );
    }

    @Test
    @DisplayName("Missing email returns expected error message")
    void missingEmailReturnsExpectedMessage() {

        // MatcherAssert.assertThat(
        //         missingEmailResponse.jsonPath().getString("message"),
        //         Matchers.is(
        //                 "Bad request, email parameter is missing in POST request."
        //         )
        // );

        MatcherAssert.assertThat(
                missingEmailPojo.getMessage(),
                Matchers.is(
                        "Bad request, email parameter is missing in POST request."
                )
        );
    }
}
