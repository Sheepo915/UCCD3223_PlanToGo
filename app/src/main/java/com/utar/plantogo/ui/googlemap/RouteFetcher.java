package com.utar.plantogo.ui.googlemap;

import com.google.android.gms.maps.model.LatLng;
import com.utar.plantogo.BuildConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RouteFetcher {

    private static final String API_KEY = BuildConfig.GOOGLE_API_KEY;
    private static final String DIRECTIONS_URL = "https://maps.googleapis.com/maps/api/directions/json";

    public static String getDirections(LatLng origin, LatLng destination) throws IOException {
        String urlString = String.format("%s?origin=%f,%f&destination=%f,%f&key=%s", DIRECTIONS_URL, origin.latitude, origin.longitude, destination.latitude, destination.longitude, API_KEY);

        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            connection.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } finally {
            connection.disconnect();
        }
    }
}
