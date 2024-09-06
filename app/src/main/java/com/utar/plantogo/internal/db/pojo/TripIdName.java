package com.utar.plantogo.internal.db.pojo;

import androidx.room.ColumnInfo;

public class TripIdName {
    @ColumnInfo(name = "id")
    public int id;
    @ColumnInfo(name = "trip_name")
    public String tripName;

    public TripIdName(int id, String tripName) {
        this.id = id;
        this.tripName = tripName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

}
