package com.example.qfz.selfservicespace.object;

import java.io.Serializable;

public class shop implements Serializable {
    private String shopID;
    private String shopName;

    public shop(String name) {
        this.shopName = name;
    }

    public String getName() {
            return shopName;
    }

    public String getShopID() {
        return shopID;
    }

    public void setShopID(String shopID) {
        this.shopID = shopID;
    }
}
