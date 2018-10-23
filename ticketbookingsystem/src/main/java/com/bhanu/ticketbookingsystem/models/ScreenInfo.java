package com.bhanu.ticketbookingsystem.models;

import java.util.ArrayList;
import java.util.HashMap;

public class ScreenInfo {
    private String name;
    private HashMap<String,RowInfo> seatInfo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, RowInfo> getSeatInfo() {
        return seatInfo;
    }

    public void setSeatInfo(HashMap<String, RowInfo> seatInfo) {
        this.seatInfo = seatInfo;
    }
}


