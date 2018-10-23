package com.bhanu.ticketbookingsystem.models;

import java.util.ArrayList;
import java.util.HashMap;

public class Seats {

    private HashMap<String, ArrayList<Integer>> seats;

    public HashMap<String, ArrayList<Integer>> getSeats() {
        return seats;
    }

    public void setSeats(HashMap<String, ArrayList<Integer>> seats) {
        this.seats = seats;
    }
}
