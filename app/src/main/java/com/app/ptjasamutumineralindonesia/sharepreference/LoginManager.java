package com.app.ptjasamutumineralindonesia.sharepreference;

import android.content.Context;
import android.content.SharedPreferences;

public class LoginManager {

    public static final String APPS = "spMahasiswaApp";

    public static final String SP_NAMA = "spNama";

    public static final String USER_ROLES = "userRoles";

    public static final String ACCESS_TOKEN = "accessToken";

    public static final String STATUS = "spSudahLogin";

    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    public LoginManager(Context context) {
        sp = context.getSharedPreferences(APPS, Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public void saveSPString(String keySP, String value) {
        spEditor.putString(keySP, value);
        spEditor.commit();
    }

    public void saveSPInt(String keySP, int value) {
        spEditor.putInt(keySP, value);
        spEditor.commit();
    }

    public void saveSPBoolean(String keySP, boolean value) {
        spEditor.putBoolean(keySP, value);
        spEditor.commit();
    }

    public String getSPNama() {
        return sp.getString(SP_NAMA, "");
    }

    public String getUserRoles() {
        return sp.getString(USER_ROLES, "");
    }

    public String getAccessToken() {
        return sp.getString(ACCESS_TOKEN, "");
    }

    public Boolean getSPSudahLogin() {
        return sp.getBoolean(STATUS, false);
    }

}