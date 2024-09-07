package com.utar.plantogo.ui.googlemap;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class RouteParser {

    public static List<LatLng> parseRoute(String jsonResponse) {
        List<LatLng> routePoints = new ArrayList<>();
        Gson gson = new Gson();

        JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);
        JsonArray routes = jsonObject.getAsJsonArray("routes");
        if (routes.size() == 0) return routePoints;

        JsonObject route = routes.get(0).getAsJsonObject();
        JsonArray legs = route.getAsJsonArray("legs");
        if (legs.size() == 0) return routePoints;

        JsonObject leg = legs.get(0).getAsJsonObject();
        JsonArray steps = leg.getAsJsonArray("steps");

        for (JsonElement step : steps) {
            JsonObject polyline = step.getAsJsonObject().getAsJsonObject("polyline");
            String points = polyline.get("points").getAsString();
            List<LatLng> decodedPoints = decodePolyline(points);
            routePoints.addAll(decodedPoints);
        }

        return routePoints;
    }

    private static List<LatLng> decodePolyline(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng(((lat / 1E5)), ((lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }
}
