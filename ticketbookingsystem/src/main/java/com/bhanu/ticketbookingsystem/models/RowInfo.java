package com.bhanu.ticketbookingsystem.models;

import java.util.ArrayList;

public class RowInfo{
    int numberOfSeats;
    ArrayList<Integer> aisleSeats;

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public ArrayList<Integer> getAisleSeats() {
        return aisleSeats;
    }

    public void setAisleSeats(ArrayList<Integer> aisleSeats) {
        this.aisleSeats = aisleSeats;
    }
}