package com.utar.plantogo.internal.db.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.utar.plantogo.internal.db.typeconverter.LocationJSONConverter;

@Entity
public class Location {

    @PrimaryKey
    @ColumnInfo(name = "id")
    public int id;

    @TypeConverters(LocationJSONConverter.class)
    @ColumnInfo(name = "location_details")
    public com.utar.plantogo.internal.tripadvisor.model.Location location;

    @ColumnInfo(name = "created_at")
    public String createdAt;
}
