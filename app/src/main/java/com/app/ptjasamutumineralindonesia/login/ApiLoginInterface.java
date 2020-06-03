package com.app.ptjasamutumineralindonesia.login;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiLoginInterface {
    @POST("authenticate")
    Call<LoginResult> getStringScalar(@Body JsonObject body);
}