package com.sparta.endpointtesting.pojoconfig.pojos;

public class UserDetailsResponse {

    private int responseCode;
    private UserDetails user;

    public UserDetailsResponse() {}

    public int getResponseCode() {
        return responseCode;
    }

    public UserDetails getUser() {
        return user;
    }
}