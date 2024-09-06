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

/**
 * <h2>Application Database with Room database</h2>
 * <p>Singleton implementation of the database.</p>
 * <p>Use getInstance(Context context) to retrieve the database instance</p>
 */
@Database(entities = {Location.class, PlannedTripsDetails.class, PlannedTrips.class}, version = 3)
@TypeConverters({LocationJSONConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    /**
     * Singleton implementation of application database.
     * <p>Use getInstance to initialize the database in MainActivity</p>
     *
     * @param context Application context, use MainActivity
     * @return Database instance
     */
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "plantogo_database").fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract LocationDao locationDao();

    public abstract PlannedTripsDao plannedTripsDao();
}
