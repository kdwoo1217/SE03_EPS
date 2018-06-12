package com.software.pld.pld;

public class ParkingLot {
    private String name;
    private double latitude, longtitude;
    private int totalSpace, currentSpace;
    private boolean isFree;
    private String note;

    public ParkingLot(String name, double latitude, double longtitude, int totalSpace, int currentSpace, int isFree, String note) {
        this.name = name;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.totalSpace = totalSpace;
        this.currentSpace = currentSpace;
        if(isFree == 1) {
            this.isFree = true;
        }
        else {
            this.isFree = false;
        }
        this.note = note;
    }

    public int getTotalSpace() {
        return totalSpace;
    }

    public int getCurrentSpace() {
        return currentSpace;
    }

    public String getName() {
        return name;
    }

    public boolean getIsFree() {
        return isFree;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public String getNote() {
        return note;
    }
}
