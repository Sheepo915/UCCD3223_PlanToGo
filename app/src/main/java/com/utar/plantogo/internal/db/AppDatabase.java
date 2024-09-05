package com.utar.plantogo.internal.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.utar.plantogo.internal.db.dao.LocationDao;
import com.utar.plantogo.internal.db.dao.PlannedTripsDao;
import com.utar.plantogo.internal.db.model.Location;
import com.utar.plantogo.internal.db.model.PlannedTrips;
import com.utar.plantogo.internal.db.model.PlannedTripsDetails;
import com.utar.plantogo.internal.db.typeconverter.LocationJSONConverter;

@Database(entities = {Location.class, PlannedTripsDetails.class, PlannedTrips.class}, version = 1)
@TypeConverters({LocationJSONConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract LocationDao locationDao();
    public abstract PlannedTripsDao plannedTripsDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "plantogo_database")
                            .fallbackToDestructiveMigration() // Optional: for schema changes without migration
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
