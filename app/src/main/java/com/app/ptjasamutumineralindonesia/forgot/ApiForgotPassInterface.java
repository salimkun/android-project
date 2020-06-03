package com.app.ptjasamutumineralindonesia.forgot;

import com.app.ptjasamutumineralindonesia.login.LoginResult;
import com.google.gson.JsonObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiForgotPassInterface {
    @POST("account/reset-password/init")
    Call<Void> sendResetPasscode(@Body RequestBody body);
}
