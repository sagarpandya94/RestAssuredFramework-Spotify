package com.spotify.oauth2.api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.HashMap;

import static com.spotify.oauth2.api.SpecBuilder.getResponseSpec;
import static io.restassured.RestAssured.given;

public class TokenManager {
    public static String renewToken(){
        HashMap<String,String> formParams = new HashMap<>();
        formParams.put("client_id","10cc81c7b9bd4e1e8e61ffe7e637fc04");
        formParams.put("client_secret","f8f8152a11234c66aeaf38701e87802a");
        formParams.put("refresh_token","AQAjFBF7kGuN_POBEoZCKxxfzQQCF8D4GRBc62rw5ajW4AksTv376byCTws75xysyABAoCOGA3Xaxp-XUeyd9K3p9_257Ueqre9_c21b1STQ735I-acHLPWXM439SbbFhAI");
        formParams.put("grant_type","refresh_token");

        Response response =
        given().
                basePath("https://accounts.spotify.com").
                formParams(formParams).
                contentType(ContentType.URLENC).
        when().
                post("/api/token").
        then().spec(getResponseSpec()).extract().response();

        if(response.statusCode() != 200)
            throw new RuntimeException("Renewal failed");

        return response.path("access_token");
    }

}
