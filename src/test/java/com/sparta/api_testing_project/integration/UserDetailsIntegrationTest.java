package com.sparta.api_testing_project.integration;

import com.sparta.api_testing_project.client.ApiClient;
import com.sparta.api_testing_project.pojos.UserDetailsResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserDetailsIntegrationTest {

    private static ApiClient apiClient;

    @BeforeAll
    static void setup() {
        apiClient = new ApiClient();
    }

    @Test
    @DisplayName("GET /getUserDetailByEmail returns 200 and correct user details")
    void testGetUserDetailsHappyPath() {

        String validEmail = "test@test.com";

        Response response = apiClient.getUserDetailByEmail(validEmail);

        Assertions.assertEquals(200, response.getStatusCode());

        UserDetailsResponse userDetailsResponse = response.as(UserDetailsResponse.class);

        Assertions.assertEquals(200, userDetailsResponse.getResponseCode());
        Assertions.assertNotNull(userDetailsResponse.getUser());
        Assertions.assertTrue(userDetailsResponse.getUser().getId() > 0);
        Assertions.assertEquals(validEmail, userDetailsResponse.getUser().getEmail());
        Assertions.assertNotNull(userDetailsResponse.getUser().getName());
        Assertions.assertNotNull(userDetailsResponse.getUser().getFirstName());
        Assertions.assertNotNull(userDetailsResponse.getUser().getLastName());
        Assertions.assertNotNull(userDetailsResponse.getUser().getCountry());
    }

    @Test
    @DisplayName("GET /getUserDetailByEmail returns payload 404 for an invalid email")
    void testGetUserDetailsSadPath() {

        String invalidEmail = "invalid-user-12345@test.com";

        Response response = apiClient.getUserDetailByEmail(invalidEmail);

        Assertions.assertEquals(200, response.getStatusCode());

        int payloadCode = response.jsonPath().getInt("responseCode");
        String message = response.jsonPath().getString("message");

        Assertions.assertEquals(404, payloadCode);
        Assertions.assertEquals(
                "Account not found with this email, try another email!",
                message
        );
    }
}