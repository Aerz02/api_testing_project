package com.sparta.endpointtesting;

import com.sparta.endpointtesting.pojoconfig.pojos.ProductListResponse;
import com.sparta.endpointtesting.pojoconfig.pojos.ProductsItem;
import com.sparta.endpointtesting.utils.ApiConfig;
import com.sparta.endpointtesting.utils.Helper;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SearchProductUserStoryTest {

    private static Response happyResponse;
    private static Response sadResponse;
    private static ProductListResponse happySearchResponse;

    @BeforeAll
    static void beforeAll() {
        RestAssured.registerParser("text/html", Parser.JSON);

        // TC3.1 Happy Path - Search with a valid keyword
        happyResponse = RestAssured
                .given()
                    .spec(Helper.searchProductsRequest("jean"))
                .when()
                    .post()
                .then()
                    .extract().response();

        happySearchResponse = happyResponse.as(ProductListResponse.class);

        // TC3.3 Sad Path - Missing search parameter
        sadResponse = RestAssured
                .given()
                    .baseUri(ApiConfig.getBaseUri())
                    .when()
                    .post(ApiConfig.getSearchProducts())
                .then()
                    .extract().response();
    }

    @Test
    @DisplayName("TC3.1 Happy Path – Search with a valid keyword returns HTTP 200")
    void testHappyPathReturns200() {
        MatcherAssert.assertThat(happyResponse.statusCode(), Matchers.is(200));
        MatcherAssert.assertThat(happySearchResponse.getResponseCode(), Matchers.is(200));
    }

    @Test
    @DisplayName("TC3.2 Verify returned products match the search term")
    void testReturnedProductsMatchSearchTerm() {
        MatcherAssert.assertThat(happySearchResponse.getProducts(), Matchers.notNullValue());
        MatcherAssert.assertThat(happySearchResponse.getProducts().size(), Matchers.greaterThan(0));

        for (ProductsItem product : happySearchResponse.getProducts()) {
            String productName = product.getName().toLowerCase();
            MatcherAssert.assertThat(
                "Product name '" + product.getName() + "' should contain the search term 'jean'",
                productName,
                Matchers.containsString("jean")
            );
        }
    }

    @Test
    @DisplayName("TC3.3 Sad Path – Missing search parameter returns response code 400")
    void testSadPathMissingParamReturns400() {
        // The server returns HTTP 200 OK, but lists responseCode 400 inside the JSON body
        MatcherAssert.assertThat(sadResponse.statusCode(), Matchers.is(200));
        MatcherAssert.assertThat(sadResponse.jsonPath().getInt("responseCode"), Matchers.is(400));
    }

    @Test
    @DisplayName("TC3.4 Verify the correct error message is returned")
    void testCorrectErrorMessageReturned() {
        String expectedMessage = "Bad request, search_product parameter is missing in POST request.";
        MatcherAssert.assertThat(sadResponse.jsonPath().getString("message"), Matchers.is(expectedMessage));
    }
}
