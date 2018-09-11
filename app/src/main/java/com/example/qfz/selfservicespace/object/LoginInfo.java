package com.example.qfz.selfservicespace.object;

public class LoginInfo {
    public String userID;
    public String password;
    public Double latitude;
    public Double longitude;

    public LoginInfo(String userID1, String password1, Double latitude1, Double longitude1) {
        userID = userID1;
        password = password1;
        latitude = latitude1;
        longitude = longitude1;
    }
}
