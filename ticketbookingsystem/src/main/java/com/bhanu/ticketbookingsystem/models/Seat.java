package com.bhanu.ticketbookingsystem.models;

import javax.persistence.*;

//Entity object that will be stored in the database
@Entity
@Table(name = "seat")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "seat_id")
    private int seatId;

    @Column(name = "screen_name")
    private String screenName;

    @Column(name = "row_name")
    private String rowName;

    @Column(name = "seat_number")
    private int seatNumber;

    @Column(name = "reserved")
    private boolean reserved;

    @Column(name = "aisle")
    private boolean aisle;

    public Seat() {
    }

    public Seat(String screenName, String rowName, int seatNumber, boolean reserved, boolean aisle) {
        this.screenName = screenName;
        this.rowName = rowName;
        this.seatNumber = seatNumber;
        this.reserved = reserved;
        this.aisle = aisle;
    }

    public int getSeatId() {
        return seatId;
    }

    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getRowName() {
        return rowName;
    }

    public void setRowName(String rowName) {
        this.rowName = rowName;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public boolean isReserved() {
        return reserved;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    public boolean isAisle() {
        return aisle;
    }

    public void setAisle(boolean aisle) {
        this.aisle = aisle;
    }
}
