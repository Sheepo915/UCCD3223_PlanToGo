package com.utar.plantogo.internal.db.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class PlannedTripsWithDetails {
    @Embedded
    public PlannedTrips plannedTrip;

    @Relation(parentColumn = "id", entityColumn = "planner_id")
    public List<PlannedTripsDetails> tripsDetails;
}
