package com.sparta.endpointtesting.pojoconfig;

import com.sparta.endpointtesting.pojoconfig.pojos.ProductListResponse;
import com.sparta.endpointtesting.pojoconfig.pojos.ProductsItem;
import com.sparta.endpointtesting.utils.Helper;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SearchProductPojoTest {

    private static Response response;
    private static ProductListResponse searchResponse;

    @BeforeAll
    static void beforeAll() {
        RestAssured.registerParser("text/html", Parser.JSON);
        response = RestAssured
                .given()
                    .spec(Helper.searchProductsRequest("jean"))
                .when()
                    .post()
                .then()
                    .extract().response();

        searchResponse = response.as(ProductListResponse.class);
    }

    @Test
    @DisplayName("Verify search response POJO mapping succeeds and is not null")
    void testSearchResponsePojoMapping() {
        MatcherAssert.assertThat(searchResponse, Matchers.notNullValue());
    }

    @Test
    @DisplayName("Verify search response products mapping lists products items")
    void testProductsMappingListsItems() {
        MatcherAssert.assertThat(searchResponse.getProducts(), Matchers.notNullValue());
        for (ProductsItem item : searchResponse.getProducts()) {
            MatcherAssert.assertThat(item.isNotNull(), Matchers.is(true));
        }
    }
}
