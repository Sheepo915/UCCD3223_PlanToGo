package com.utar.plantogo.internal.db.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class PlannedTrips {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "trip_name")
    public String tripName;

    @ColumnInfo(name = "trip_location")
    public String tripLocation;

    @ColumnInfo(name = "start_date")
    public String startDate;

    @ColumnInfo(name = "end_date")
    public String endDate;

    @ColumnInfo(name = "notes")
    public String notes;

    @ColumnInfo(name = "created_at")
    public String createdAt;

    public PlannedTrips(String tripName, String tripLocation, String startDate, String endDate, String notes) {
        this.tripName = tripName;
        this.tripLocation = tripLocation;
        this.startDate = startDate;
        this.endDate = endDate;
        this.notes = notes;
        this.createdAt = new Date().toString();
    }
}
