package com.sparta.endpointtesting.pojoconfig.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Shared response shape for account-management endpoints that return
 * only a responseCode and message (createAccount, deleteAccount,
 * updateAccount, verifyLogin).
 */
public class AccountResponse {

    @JsonProperty("responseCode")
    private int responseCode;

    @JsonProperty("message")
    private String message;

    public int getResponseCode(){
        return responseCode;
    }

    public String getMessage(){
        return message;
    }
}