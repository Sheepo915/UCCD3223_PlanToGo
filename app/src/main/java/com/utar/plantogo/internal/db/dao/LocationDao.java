package com.utar.plantogo.internal.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.utar.plantogo.internal.db.model.Location;

import java.util.List;

@Dao
public interface LocationDao {

    @Insert
    void insertLocation(Location location);

    @Query("SELECT * FROM Location")
    List<Location> getAllLocations();

    @Query("SELECT * FROM Location WHERE id = :locationId")
    Location getLocationById(int locationId);
}
