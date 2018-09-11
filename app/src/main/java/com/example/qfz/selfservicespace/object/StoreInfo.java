package com.example.qfz.selfservicespace.object;

import java.io.Serializable;
import java.util.HashMap;

//要实现Serializable接口才能在intent中传递
public class StoreInfo implements Serializable {

    public String storeID;
    public HashMap<String, String> allSeats;
    public int rowNum;
    public int columnNum;

    public StoreInfo(String storeID1, HashMap<String, String> seats, int rownum, int columnnum) {
        storeID = storeID1;
        allSeats = seats;
        rowNum = rownum;
        columnNum = columnnum;
    }
}
