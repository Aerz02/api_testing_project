package com.sparta.api_testing_project.client;

import com.sparta.endpointtesting.utils.ApiConfig;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;

public class ApiClient {

    private static final String BASE_URL = ApiConfig.getBaseUri();

    static {
        RestAssured.registerParser("text/html", Parser.JSON);
    }

    public Response getAllProducts() {
        return RestAssured
                .given()
                .baseUri(BASE_URL)
                .when()
                .get(ApiConfig.getProductsList());
    }

    public Response postAllProducts() {
        return RestAssured
                .given()
                .baseUri(BASE_URL)
                .when()
                .post(ApiConfig.getProductsList());
    }

    public Response getAllBrands() {
        return RestAssured
                .given()
                .baseUri(BASE_URL)
                .when()
                .get(ApiConfig.getBrandsList());
    }

    public Response putAllBrands() {
        return RestAssured
                .given()
                .baseUri(BASE_URL)
                .when()
                .put(ApiConfig.getBrandsList());
    }

    public Response searchProduct(String query) {
        return RestAssured
                .given()
                .baseUri(BASE_URL)
                .formParam("search_product", query)
                .when()
                .post(ApiConfig.getSearchProducts());
    }

    public Response searchProductMissingParam() {
        return RestAssured
                .given()
                .baseUri(BASE_URL)
                .when()
                .post(ApiConfig.getSearchProducts());
    }

    public Response getUserDetailByEmail(String email) {
        return RestAssured
                .given()
                .baseUri(BASE_URL)
                .queryParam("email", email)
                .when()
                .get(ApiConfig.getUserDetailByEmail());
    }
}
