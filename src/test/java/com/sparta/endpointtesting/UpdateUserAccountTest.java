package com.sparta.endpointtesting;
import com.sparta.endpointtesting.utils.Helper;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UpdateUserAccountTest {
    private static Response updateHappyPath;

    @BeforeAll
    static void setUp() {
        RestAssured.registerParser("text/html", Parser.JSON);

        updateHappyPath = Helper.putUpdateAccountRequest(
                "Salah",
                "salah@salah.com",
                "password123",
                "Salah",
                "ExampleLastName"
        );
        System.out.println(updateHappyPath);
    }

    @Test
    @DisplayName("Check status code after updating User last name")
    void checkStatusCodeAfterUpdatingUserLastName() {
        MatcherAssert.assertThat(updateHappyPath.getStatusCode(), Matchers.is(200));
    }

    @Test
    @DisplayName("Check if the correct message is returned after updating User last name")
    void checkCorrectMessageAfterUpdatingUserLastName() {
        MatcherAssert.assertThat(updateHappyPath.jsonPath().getString("message"), Matchers.is("User updated!"));
    }

    @Test
    @DisplayName("Check if the details have been correctly updated with a get request")
    void checkDetails() {
    }
}