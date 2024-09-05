package com.utar.plantogo.internal.db.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = PlannedTrips.class, parentColumns = "id", childColumns = "planner_id", onDelete = ForeignKey.CASCADE))
public class PlannedTripsDetails {
    @PrimaryKey
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "planner_id")
    public int plannerId;

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

}
