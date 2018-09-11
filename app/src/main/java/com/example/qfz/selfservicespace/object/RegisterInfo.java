package com.example.qfz.selfservicespace.object;

import com.google.gson.Gson;

public class RegisterInfo {
    public String userName;
    public String password;
    public String phoneNum;

    public RegisterInfo(String userName1, String phoneNum1, String password1) {
        userName = userName1;
        password = password1;
        phoneNum = phoneNum1;
    }

    public String getJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
