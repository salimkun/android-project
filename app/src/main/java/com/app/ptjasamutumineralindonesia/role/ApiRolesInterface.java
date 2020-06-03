package com.app.ptjasamutumineralindonesia.role;

import com.app.ptjasamutumineralindonesia.login.LoginResult;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ApiRolesInterface {
    @GET("account")
    Call<RoleResult> getRoles(@Header("Authorization") String token);
}
