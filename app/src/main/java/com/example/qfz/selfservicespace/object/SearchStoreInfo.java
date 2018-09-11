package com.example.qfz.selfservicespace.object;

import com.google.gson.Gson;
import java.io.Serializable;

public class SearchStoreInfo implements Serializable {
    public String searchKey;
    public String beginTime;
    public String endTime;
    public int peopleNum;

    public String getJson() {
        return new Gson().toJson(this);
    }
}
