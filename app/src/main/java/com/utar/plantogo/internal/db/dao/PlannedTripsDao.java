package com.utar.plantogo.internal.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.utar.plantogo.internal.db.model.PlannedTrips;
import com.utar.plantogo.internal.db.model.PlannedTripsDetails;
import com.utar.plantogo.internal.db.model.PlannedTripsWithDetails;
import com.utar.plantogo.internal.db.pojo.TripIdName;

import java.util.List;

@Dao
public interface PlannedTripsDao {

    @Insert
    void insertPlannedTrip(PlannedTrips plannedTrips);

    @Insert
    void insertPlannedTripDetails(PlannedTripsDetails tripDetails);

    @Update
    void updatePlannedTrip(PlannedTrips plannedTrips);

    @Update
    void updatePlannedTripDetails(PlannedTripsDetails tripDetails);

    @Delete
    void deleteTrip(PlannedTrips plannedTrip);

    @Delete
    void deleteLocationInTrip(PlannedTripsDetails plannedTripsDetail);

    @Query("SELECT COUNT(id) FROM plannedtrips")
    int getTripsCount();

    @Query("SELECT id, trip_name FROM plannedtrips")
    List<TripIdName> getAllTripsName();

    @Query("SELECT * FROM PlannedTrips WHERE id = :id")
    PlannedTrips getTripById(int id);

    @Query("SELECT MAX(`index`) FROM PlannedTripsDetails WHERE planner_id = :plannerId")
    int getMaxIndex(int plannerId);

    @Transaction
    @Query("SELECT * FROM PlannedTrips WHERE id = :id")
    PlannedTripsWithDetails getPlannedTripsWithDetailsById(int id);

    @Transaction
    @Query("SELECT * FROM PlannedTrips")
    List<PlannedTripsWithDetails> getPlannedTripsWithDetails();
}
