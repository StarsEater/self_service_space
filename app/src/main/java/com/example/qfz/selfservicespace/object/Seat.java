package com.example.qfz.selfservicespace.object;

public class Seat {
    public int row;
    public int column;
    public String state;

    public Seat(int rowNo, int columnNo, String stateT) {
        row = rowNo;
        column = columnNo;
        state = stateT;
    }

    public boolean equal(Seat x) {
        if(x.row == this.row && x.column == this.column && x.state.equals(this.state))
            return true;
        return false;
    }
}
