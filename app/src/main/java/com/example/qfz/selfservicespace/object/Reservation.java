package com.example.qfz.selfservicespace.object;

import java.io.Serializable;

public class Reservation implements Serializable {
    private String reservation_id;
    private String reservation_shopname;
    private String reservation_state;
    public Reservation(String name,String state,String reservation_id) {
        this.reservation_id=reservation_id;
        this.reservation_shopname = name;
        this.reservation_state= state;
    }
    public String getReservation_name(){
        return reservation_shopname;
    }

    public String getReservation_state() {
        return reservation_state; }

    public String getReservation_id() {
        return reservation_id;
    }
}

