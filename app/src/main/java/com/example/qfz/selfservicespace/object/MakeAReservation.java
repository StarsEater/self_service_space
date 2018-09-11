package com.example.qfz.selfservicespace.object;

import java.util.ArrayList;

public class MakeAReservation {
    public ArrayList<Seat> orderedSeats;
    public String userID;
    public String storeID;
    public String beginTime;
    public String endTime;

    public MakeAReservation(String userID1, String storeID1, String beginTime1, String endTime1) {
        userID = userID1;
        storeID = storeID1;
        beginTime = beginTime1;
        endTime = endTime1;
        orderedSeats = new ArrayList<Seat>();
    }
}
