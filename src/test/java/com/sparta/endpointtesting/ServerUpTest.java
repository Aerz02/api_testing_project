package com.sparta.endpointtesting;

import com.sparta.endpointtesting.utils.ApiConfig;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class ServerUpTest {
    private static Response response;

    @BeforeAll
    static void beforeAll(){
        response = RestAssured
                .given()
                .baseUri(ApiConfig.getBaseUri())
                .when()
                .get(ApiConfig.getProductsList())
                .then()
                .log().all()
                .extract().response();
    }

    @Test
    @DisplayName("Status code 200 returned")
    void testStatusCode200(){

        System.out.println(response.jsonPath().getList("products"));
        MatcherAssert.assertThat(response.statusCode(), Matchers.is(200));
    }

    @Test
    @DisplayName("When requesting all products and list is returned with all of them.")
    void testAllProductsReturned(){
        System.out.println(response.jsonPath().getList("products"));
        MatcherAssert.assertThat(response.jsonPath().getList("products").size(), Matchers.is(34));
    }

    @Test
    @DisplayName("Verify each product contains expected fields")
    void testAllProductsContainExpectedFields() {

        response.jsonPath().<Map<String, Object>>getList("products")
                .forEach(product -> {
                    MatcherAssert.assertThat(product, Matchers.hasKey("id"));
                    MatcherAssert.assertThat(product, Matchers.hasKey("name"));
                    MatcherAssert.assertThat(product, Matchers.hasKey("price"));
                    MatcherAssert.assertThat(product, Matchers.hasKey("brand"));
                    MatcherAssert.assertThat(product, Matchers.hasKey("category"));
                });
    }
    }

