package com.utar.plantogo.internal.db.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = PlannedTrips.class, parentColumns = "id", childColumns = "planner_id", onDelete = ForeignKey.CASCADE))
public class PlannedTripsDetails {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int plannedTripsDetailsId;

    @ColumnInfo(name = "planner_id")
    public int plannedTripId;

    @ColumnInfo(name = "location_id")
    public int locationId;

    @ColumnInfo(name = "index")
    public int index;

    @ColumnInfo(name = "planned_timestamp")
    public String plannedTimestamp;

    @ColumnInfo(name = "notes")
    public String notes;

    @ColumnInfo(name = "created_at")
    public String createdAt;

    public PlannedTripsDetails() {
    }

    public PlannedTripsDetails(int plannedTripId, int locationId, int index, String plannedTimestamp, String notes, String createdAt) {
        this.plannedTripId = plannedTripId;
        this.locationId = locationId;
        this.index = index;
        this.plannedTimestamp = plannedTimestamp;
        this.notes = notes;
        this.createdAt = createdAt;
    }


    public PlannedTripsDetails(int plannedTripsDetailsId, int plannedTripId, int locationId, int index, String plannedTimestamp, String notes, String createdAt) {
        this.plannedTripsDetailsId = plannedTripsDetailsId;
        this.plannedTripId = plannedTripId;
        this.locationId = locationId;
        this.index = index;
        this.plannedTimestamp = plannedTimestamp;
        this.notes = notes;
        this.createdAt = createdAt;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}
