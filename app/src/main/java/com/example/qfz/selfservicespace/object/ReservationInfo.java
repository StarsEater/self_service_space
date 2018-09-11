package com.example.qfz.selfservicespace.object;

import java.io.Serializable;
import java.util.ArrayList;

public class ReservationInfo implements Serializable {
    public String reservationID;
    public String storeName;
    public String time;
    public ArrayList<Integer> tables;
    public Double discount;
    public Double bail;
    public Double total;
    public String phoneNumber;
}