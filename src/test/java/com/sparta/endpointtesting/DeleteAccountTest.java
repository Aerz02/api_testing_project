package com.sparta.endpointtesting;

import com.sparta.endpointtesting.pojoconfig.pojos.AccountResponse;
import com.sparta.endpointtesting.utils.Helper;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * User story: Delete an existing user account
 * Confirmed via manual curl verification
 * - This endpoint always returns HTTP 200 at the transport level.
 * - The real outcome (success/failure) is nested in the JSON body as
 *   "responseCode" + "message", not in the HTTP status. This mirrors
 *   the same pattern already observed in SearchProductUserStoryTest.
 */
public class DeleteAccountTest {

    private static final String PASSWORD = "TestPass123!";
    private static final String NON_EXISTENT_EMAIL = "sdet_does_not_exist_" + System.currentTimeMillis() + "@example.com";
    private static String existingEmail;

    private static Response happyResponse;
    private static Response sadResponse;
    private static AccountResponse happyAccountResponse;
    private static AccountResponse sadAccountResponse;

    @BeforeAll
    static void beforeAll() {
        RestAssured.registerParser("text/html", Parser.JSON);

        existingEmail = "sdet_test_" + System.currentTimeMillis() + "@example.com";
        createTestAccount(existingEmail, PASSWORD);
        verifyAccountExists(existingEmail);

        // Happy Path - delete an account that exists
        happyResponse = RestAssured
                .given()
                .spec(Helper.deleteAccountRequest(existingEmail, PASSWORD))
                .when()
                .delete()
                .then()
                .log().all()
                .extract().response();

        happyAccountResponse = happyResponse.as(AccountResponse.class);

        // Sad Path - attempt to delete an account that does not exist
        sadResponse = RestAssured
                .given()
                .spec(Helper.deleteAccountRequest(NON_EXISTENT_EMAIL, "irrelevant-password"))
                .when()
                .delete()
                .then()
                .log().all()
                .extract().response();

        sadAccountResponse = sadResponse.as(AccountResponse.class);
    }

    // --- Happy Path ---

    @Test
    @DisplayName("Happy Path - HTTP status 200 returned")
    void testHappyPathHttpStatus200() {
        MatcherAssert.assertThat(happyResponse.statusCode(), Matchers.is(200));
    }

    @Test
    @DisplayName("Happy Path - responseCode 200 returned in body")
    void testHappyPathResponseCode200() {
        MatcherAssert.assertThat(happyAccountResponse.getResponseCode(), Matchers.is(200));
    }

    @Test
    @DisplayName("Happy Path - message confirms account deleted")
    void testHappyPathMessageConfirmsDeletion() {
        MatcherAssert.assertThat(happyAccountResponse.getMessage(), Matchers.is("Account deleted!"));
    }

    @Test
    @DisplayName("Happy Path - deleted account no longer resolves via getUserDetailByEmail")
    void testDeletedAccountNoLongerExists() {
        Response verifyResponse = RestAssured
                .given()
                .spec(Helper.getUserDetailByEmailRequest(existingEmail))
                .when()
                .get()
                .then()
                .extract().response();

        // Confirms delete had a real side effect, not just a 200 response
        MatcherAssert.assertThat(verifyResponse.jsonPath().getInt("responseCode"), Matchers.is(404));
    }

    // --- Sad Path ---

    @Test
    @DisplayName("Sad Path - HTTP status 200 returned (per confirmed API behaviour)")
    void testSadPathHttpStatus200() {
        MatcherAssert.assertThat(sadResponse.statusCode(), Matchers.is(200));
    }

    @Test
    @DisplayName("Sad Path - responseCode 404 returned in body for non-existent account")
    void testSadPathResponseCode404() {
        MatcherAssert.assertThat(sadAccountResponse.getResponseCode(), Matchers.is(404));
    }

    @Test
    @DisplayName("Sad Path - correct error message returned")
    void testSadPathMessage() {
        MatcherAssert.assertThat(sadAccountResponse.getMessage(), Matchers.is("Account not found!"));
    }

    /**
     * TEMPORARY local helper — createAccount requires all 16 fields from API 11
     * to return 201. Helper.createAccountRequest(email, password) currently only
     * sends 2 of them and is still being fixed by its owner. Once that lands,
     * replace this method's usage with Helper.createAccountRequest(...) and delete
     * this method.
     */
    private static void createTestAccount(String email, String password) {
        Response response = RestAssured
                .given()
                .spec(Helper.createAccountRequest("Delete Test User", email, password))
                .when()
                .post()
                .then()
                .log().all()
                .extract().response();

        MatcherAssert.assertThat(
                "Account setup failed — createAccount body: " + response.getBody().asString(),
                response.jsonPath().getInt("responseCode"),
                Matchers.is(201)
        );
    }

    private static void verifyAccountExists(String email) {
        Response response = RestAssured
                .given()
                .spec(Helper.getUserDetailByEmailRequest(email))
                .when()
                .get()
                .then()
                .extract().response();

        MatcherAssert.assertThat(
                "Setup verification failed — account does not appear to exist after creation. Body: " + response.getBody().asString(),
                response.jsonPath().getInt("responseCode"),
                Matchers.is(200)
        );
    }
}