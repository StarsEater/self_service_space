package com.example.qfz.selfservicespace.object;

import java.io.Serializable;

public class User implements Serializable {
    private String userID;
    private String userName;
    private String password;

    public String getUserID(){
        return userID;
    }
    public void setUserID(String id){
        this.userID = id;
    }

    public String getUserName(){
        return userName;
    }
    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }
}
