package com.utar.plantogo.internal.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.utar.plantogo.internal.db.model.PlannedTrips;
import com.utar.plantogo.internal.db.model.PlannedTripsDetails;

import java.util.List;

@Dao
public interface PlannedTripsDao {

    @Insert
    void insertPlannedTrip(PlannedTrips plannedTrips);

    @Insert
    void insertPlannedTripDetails(PlannedTripsDetails tripDetails);

    @Delete
    void delete(int id);

    @Query("SELECT * FROM plannedtrips WHERE id = :id")
    PlannedTrips getTripById(int id);

    @Transaction
    @Query("SELECT * FROM plannedtrips")
    List<PlannedTrips> getPlannedTripsWithDetails();
}
