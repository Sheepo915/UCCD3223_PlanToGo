package com.utar.plantogo.internal.db.pojo;

import androidx.room.ColumnInfo;

public class TripIdName {
    @ColumnInfo(name = "id")
    public int plannedTripId;
    @ColumnInfo(name = "trip_name")
    public String tripName;

    public TripIdName() {
    }

    public TripIdName(int plannedTripId, String tripName) {
        this.plannedTripId = plannedTripId;
        this.tripName = tripName;
    }

    public int getId() {
        return plannedTripId;
    }

    public void setId(int id) {
        this.plannedTripId = id;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

}
