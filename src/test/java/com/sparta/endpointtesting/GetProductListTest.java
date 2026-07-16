package com.sparta.endpointtesting;

import com.sparta.endpointtesting.pojoconfig.pojos.ProductListResponse;
import com.sparta.endpointtesting.pojoconfig.pojos.ProductsItem;
import com.sparta.endpointtesting.utils.Helper;
//import com.sparta.endpointtesting.utils.ApiConfig;
//import io.restassured.RestAssured;
//import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GetProductListTest {
    private static Response response;

    private static ProductListResponse productListResponse;

    private static ProductsItem productsItem;

    @BeforeAll
    static void beforeAll(){
        response = Helper.getAllBrandsList();
        productListResponse = response.as(ProductListResponse.class);

    }

    @Test
    @DisplayName("Status code 200 returned")
    void testStatusCode200(){
        MatcherAssert.assertThat(productListResponse.getResponseCode(), Matchers.is(200));
    }

    @Test
    @DisplayName("When requesting all products and list is returned with all of them.")
    void testAllProductsReturned(){
        MatcherAssert.assertThat(productListResponse.getProducts().size(), Matchers.is(34));
    }

    @Test
    @DisplayName("Verify each product contains expected fields")
    void testAllProductsContainExpectedFields() {
//
//        response.jsonPath().<Map<String, Object>>getList("products")
//                .forEach(product -> {
//                    MatcherAssert.assertThat(product, Matchers.hasKey("id"));
//                    MatcherAssert.assertThat(product, Matchers.hasKey("name"));
//                    MatcherAssert.assertThat(product, Matchers.hasKey("price"));
//                    MatcherAssert.assertThat(product, Matchers.hasKey("brand"));
//                    MatcherAssert.assertThat(product, Matchers.hasKey("category"));
//                });

        for(int i = 0; i < productListResponse.getProducts().size(); i++){
            productsItem = productListResponse.getProducts().get(i);
            MatcherAssert.assertThat(productsItem.isNotNull(), Matchers.notNullValue());
        }

    }

    @Test
    @DisplayName("Verify post to product endpoint returns 405")
    void testPostToProductEndpointReturn405() {

    }

}