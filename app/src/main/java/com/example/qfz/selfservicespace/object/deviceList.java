package com.example.qfz.selfservicespace.object;

import java.io.Serializable;
import java.util.ArrayList;

public class deviceList implements Serializable {
    public ArrayList<device> devices;
    public int devicesnum;
    public String qrcode;
    public String reservationID;
}
