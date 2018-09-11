package com.example.qfz.selfservicespace.object;

import java.io.Serializable;

public class device implements Serializable {
    private String device_id;
    private String device_name;
    private String device_state;
    public device(String device_id, String device_name, String device_state) {
       this.device_id=device_id;
       this.device_name=device_name;
       this.device_state=device_state;
    }
    public String getDevice_id() {
        return device_id;
    }

    public String getDevice_name() {
        return device_name;
    }
    public String getDevice_state() {
        return device_state;
    }
}

