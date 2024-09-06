package com.utar.plantogo.internal.tripadvisor;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.utar.plantogo.BuildConfig;
import com.utar.plantogo.internal.APIRequest;

import org.json.JSONObject;

public class TripAdvisor implements TripAdvisorAPI {

    private static final String API_KEY = BuildConfig.SUPABASE_API_KEY;

    @Override
    public void nearbyLocationSearch(@NonNull String latLong, @Nullable String category, @Nullable String phone, @Nullable String address, @Nullable String radius, @Nullable String radiusUnit, @Nullable String language, APIRequest.ResponseCallback callback) {
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("latLong", latLong);
            if (category != null) {
                jsonBody.put("category", category);
            }
            if (phone != null) {
                jsonBody.put("phone", phone);
            }
            if (address != null) {
                jsonBody.put("address", address);
            }
            if (radius != null) {
                jsonBody.put("radius", radius);
            }
            if (radiusUnit != null) {
                jsonBody.put("radiusUnit", radiusUnit);
            }
            if (language != null) {
                jsonBody.put("language", language);
            }

            Log.d("DEBUG", "nearbyLocationSearch: " + jsonBody.toString());

            APIRequest apiRequest = new APIRequest(BuildConfig.SUPABASE_EDGE_FETCH_NEARBY_LOCATION);
            apiRequest.addHeader("Authorization", "Bearer " + API_KEY);
            apiRequest.addHeader("Content-Type", "application/json");
            apiRequest.setRequestMethod(APIRequest.REQUEST_METHOD.POST);
            apiRequest.setRequestBody(jsonBody.toString());
            apiRequest.makeRequest(callback);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void locationSearch(@NonNull String searchQuery, @Nullable String latLong, @Nullable String category, @Nullable String phone, @Nullable String address, @Nullable String radius, @Nullable String radiusUnit, @Nullable String language, APIRequest.ResponseCallback callback) {
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("searchQuery", searchQuery);
            if (latLong != null) {
                jsonBody.put("latLong", latLong);
            }
            if (category != null) {
                jsonBody.put("category", category);
            }
            if (phone != null) {
                jsonBody.put("phone", phone);
            }
            if (address != null) {
                jsonBody.put("address", address);
            }
            if (radius != null) {
                jsonBody.put("radius", radius);
            }
            if (radiusUnit != null) {
                jsonBody.put("radiusUnit", radiusUnit);
            }
            if (language != null) {
                jsonBody.put("language", language);
            }

            APIRequest apiRequest = new APIRequest(BuildConfig.SUPABASE_EDGE_FETCH_LOCATION_SEARCH);
            apiRequest.addHeader("Authorization", "Bearer " + API_KEY);
            apiRequest.addHeader("Content-Type", "application/json");
            apiRequest.setRequestMethod(APIRequest.REQUEST_METHOD.POST);
            apiRequest.setRequestBody(jsonBody.toString());
            apiRequest.makeRequest(callback);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void locationDetails(@NonNull String locationId,  APIRequest.ResponseCallback callback) {
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("locationId", locationId);

            APIRequest apiRequest = new APIRequest(BuildConfig.SUPABASE_EDGE_FETCH_LOCATION_DETAILS);
            apiRequest.addHeader("Authorization", "Bearer " + API_KEY);
            apiRequest.addHeader("Content-Type", "application/json");
            apiRequest.setRequestMethod(APIRequest.REQUEST_METHOD.POST);
            apiRequest.setRequestBody(jsonBody.toString());
            apiRequest.makeRequest(callback);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
