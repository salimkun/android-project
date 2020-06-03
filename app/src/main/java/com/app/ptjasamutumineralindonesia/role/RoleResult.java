package com.app.ptjasamutumineralindonesia.role;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RoleResult {

    @SerializedName("authorities")
    private ArrayList<String> rolesList;

    public ArrayList<String> getRolesList() {
        return rolesList;
    }

    public void setRolesList(ArrayList<String>  rolesList) {
        this.rolesList = rolesList;
    }
}
