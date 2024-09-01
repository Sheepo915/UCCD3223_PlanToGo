package com.utar.plantogo.internal.tripadvisor;

import androidx.annotation.NonNull;

import com.utar.plantogo.BuildConfig;
import com.utar.plantogo.internal.APIRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

public class TripAdvisor implements TripAdvisorAPI {

    private static final String FETCH_NEARBY_LOCATION = BuildConfig.SUPABASE_EDGE_FETCH_NEARBY_LOCATION;
    private static final String API_KEY = BuildConfig.SUPABASE_API_KEY;

    @Override
    public void nearbyLocationSearch(@NonNull String latLong, Optional<String> category, Optional<String> phone, Optional<String> address, Optional<String> radius, Optional<String> radiusUnit, Optional<String> language, APIRequest.ResponseCallback callback) {
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("latLong", latLong);
            category.ifPresent(value -> {
                try {
                    jsonBody.put("category", value);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            });
            phone.ifPresent(value -> {
                try {
                    jsonBody.put("phone", value);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            });
            address.ifPresent(value -> {
                try {
                    jsonBody.put("address", value);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            });
            radius.ifPresent(value -> {
                try {
                    jsonBody.put("radius", value);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            });
            radiusUnit.ifPresent(value -> {
                try {
                    jsonBody.put("radiusUnit", value);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            });
            language.ifPresent(value -> {
                try {
                    jsonBody.put("language", value);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            });

            APIRequest apiRequest = new APIRequest(FETCH_NEARBY_LOCATION);
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
