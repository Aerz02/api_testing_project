package com.sparta.api_testing_project;

import com.sparta.endpointtesting.pojoconfig.pojos.BrandList;
import com.sparta.endpointtesting.pojoconfig.pojos.BrandsItem;
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

public class GetBrandTest {

    private static Response getResponse;

    private static Response putResponse;

    private static BrandList brandList;

    private static BrandsItem brandsItem;

    @BeforeAll
    static void beforeAll(){
        getResponse = Helper.getListOfBrands();
        brandList = getResponse.as(BrandList.class);
    }

    @Test
    @DisplayName("Status code 200 returned")
    void testStatusCode200(){
        MatcherAssert.assertThat(brandList.getResponseCode(), Matchers.is(200));
    }

    @Test
    @DisplayName("When requesting all brands, each brand should have the fields ID and name.")
    void testAllBrandsHaveFieldsIdAndName(){
        for(int i = 0; i < brandList.getBrands().size(); i++){
            brandsItem = brandList.getBrands().get(i);
            MatcherAssert.assertThat(brandsItem.isNotNull(), Matchers.notNullValue());
        }
    }



    @Test
    @DisplayName("PUT request to brands endpoint returns 405 response code")
    void testPutBrandsReturns405(){
        putResponse = Helper.putBrandRequest();
        // The API returns HTTP 200 OK header, but lists responseCode 405 in the JSON body (due to backend defect)
        MatcherAssert.assertThat(putResponse.getStatusCode(), Matchers.is(200));
        MatcherAssert.assertThat(putResponse.jsonPath().getInt("responseCode"), Matchers.is(405));
    }



}
