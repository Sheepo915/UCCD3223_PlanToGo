package com.utar.plantogo.internal.db.typeconverter;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.utar.plantogo.internal.tripadvisor.model.Location;

import java.lang.reflect.Type;

public class LocationJSONConverter {
    @TypeConverter
    public static Location fromJson(String json) {
        Type type = new TypeToken<Location>() {
        }.getType();

        return new Gson().fromJson(json, type);
    }

    @TypeConverter
    public static String toJson(Location location) {
        return new Gson().toJson(location);
    }

}
