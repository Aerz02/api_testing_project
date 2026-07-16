package com.sparta.endpointtesting.utils;

import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import java.util.Map;

public class Helper {
    private static final String BASE_URI = ApiConfig.getBaseUri();
    private static final String PRODUCTS_LIST_PATH = ApiConfig.getProductsList();
    private static final String BRAND_PATH = ApiConfig.getBrandsList();
    private static final String SEARCH_PRODUCTS_PATH = ApiConfig.getSearchProducts();
    private static final String VERIFY_LOGIN_PATH = ApiConfig.getVerifyLogin();
    private static final String CREATE_ACCOUNT_PATH = ApiConfig.getCreateAccount();
    private static final String DELETE_ACCOUNT_PATH = ApiConfig.getDeleteAccount();
    private static final String UPDATE_ACCOUNT_PATH = ApiConfig.getUpdateAccount();
    private static final String USER_DETAIL_BY_EMAIL_PATH = ApiConfig.getUserDetailByEmail();
    private static Response response;

    private static RequestSpecBuilder getBaseSpecBuilder(String path) {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .setBasePath(path);
    }
    public static RequestSpecification getBrandRequests() {
        return getBaseSpecBuilder(BRAND_PATH)
                .build();
    }
    public static RequestSpecification getProductsListRequest() {
        return getBaseSpecBuilder(PRODUCTS_LIST_PATH)
                .build();
    }

    public static RequestSpecification searchProductsRequest(String searchTerm) {
        return getBaseSpecBuilder(SEARCH_PRODUCTS_PATH)
                .addFormParam("search_product", searchTerm)
                .build();
    }

    public static RequestSpecification verifyLoginRequest(String email, String password) {
        return getBaseSpecBuilder(VERIFY_LOGIN_PATH)
                .addFormParams(Map.of(
                        "email", email,
                        "password", password
                )).build();
    }

    public static RequestSpecification createAccountRequest(String email, String password) {
        return getBaseSpecBuilder(CREATE_ACCOUNT_PATH)
                .addFormParams(Map.of(
                        "email", email,
                        "password", password
                )).build();
    }

// Don't know parameters for these will update later
    public static RequestSpecification deleteAccountRequest(String email, String password) {
        return getBaseSpecBuilder(DELETE_ACCOUNT_PATH)
                .addFormParams(Map.of(
                        "email", email,
                        "password", password
                )).build();
    }
/*
    public static RequestSpecification updateAccountRequest(String email, String password) {
        return getBaseSpecBuilder(UPDATE_ACCOUNT_PATH)
                .addFormParams(Map.of(
                        "email", email,
                        "password", password
                )).build();
    } */

    public static RequestSpecification getUserDetailByEmailRequest(String email) {
        return getBaseSpecBuilder(USER_DETAIL_BY_EMAIL_PATH)
                .addQueryParam("email", email)
                .build();
    }

    public static Response getAllBrandsList() {
        RestAssured.registerParser("text/html", Parser.JSON);
        return response = RestAssured
                .given()
                .spec(getProductsListRequest())
                .when()
                .get()
                .then()
                .log().all()
                .extract().response();

    }

    public static Response getListOfBrands() {
        RestAssured.registerParser("text/html", Parser.JSON);

        return response = RestAssured
                .given()
                .baseUri(ApiConfig.getBaseUri())
                .when()
                .get(ApiConfig.getBrandsList())
                .then()
                .log().all()
                .extract().response();
    }

    public static Response putBrandRequest() {
        return RestAssured
                .given()
                .baseUri(ApiConfig.getBaseUri())
                .header("Content-Type", "application/json")
                .body("""
                {
                    "id": 1,
                    "brand": "Updated Brand"
                }
                """)
                .when()
                .put(ApiConfig.getBrandsList())
                .then()
                .log().all()
                .extract().response();

    }

    public static RequestSpecification createAccountRequest(String name, String email, String password) {
        return getBaseSpecBuilder(CREATE_ACCOUNT_PATH)
                .addFormParam("name", name)
                .addFormParam("email", email)
                .addFormParam("password", password)
                .addFormParam("title", "Mr")
                .addFormParam("birth_date", "10")
                .addFormParam("birth_month", "5")
                .addFormParam("birth_year", "1995")
                .addFormParam("firstname", "Test")
                .addFormParam("lastname", "User")
                .addFormParam("company", "Sparta")
                .addFormParam("address1", "1 Test Street")
                .addFormParam("address2", "London")
                .addFormParam("country", "United Kingdom")
                .addFormParam("zipcode", "SW1A 1AA")
                .addFormParam("state", "London")
                .addFormParam("city", "London")
                .addFormParam("mobile_number", "07123456789")
                .build();
    }

    public static RequestSpecification createAccountWithoutEmailRequest(String name, String password) {
        return getBaseSpecBuilder(CREATE_ACCOUNT_PATH)
                .addFormParam("name", name)
                .addFormParam("password", password)
                .addFormParam("title", "Mr")
                .addFormParam("birth_date", "10")
                .addFormParam("birth_month", "5")
                .addFormParam("birth_year", "1995")
                .addFormParam("firstname", "Test")
                .addFormParam("lastname", "User")
                .addFormParam("company", "Sparta")
                .addFormParam("address1", "1 Test Street")
                .addFormParam("address2", "London")
                .addFormParam("country", "United Kingdom")
                .addFormParam("zipcode", "SW1A 1AA")
                .addFormParam("state", "London")
                .addFormParam("city", "London")
                .addFormParam("mobile_number", "07123456789")
                .build();
    }
}

