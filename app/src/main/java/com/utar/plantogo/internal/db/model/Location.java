package com.utar.plantogo.internal.db.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.utar.plantogo.internal.db.typeconverter.LocationJSONConverter;

import java.util.Date;

@Entity
public class Location {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int locationId;

    @TypeConverters(LocationJSONConverter.class)
    @ColumnInfo(name = "location_details")
    public com.utar.plantogo.internal.tripadvisor.model.Location location;

    @ColumnInfo(name = "created_at")
    public String createdAt;

    public Location(com.utar.plantogo.internal.tripadvisor.model.Location location) {
        this.location = location;
        this.createdAt = new Date().toString();
    }
}
