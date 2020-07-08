package com.app.ptjasamutumineralindonesia.login;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiLoginInterface {
    @POST("authenticate")
    Call<LoginResult> getStringScalar(@Body JsonObject body);
    @GET("account")
    Call<MeResult> getMeData(@Header("Authorization") String token);
}