package com.app.ptjasamutumineralindonesia.login;

import com.google.gson.annotations.SerializedName;

public class LoginResult {

    private int status;
    private String message;

    @SerializedName("id_token")
    private String idToken;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

}
