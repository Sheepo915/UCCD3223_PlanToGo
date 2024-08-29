package com.utar.plantogo.internal.tripadvisor;

import androidx.annotation.NonNull;

import com.utar.plantogo.internal.APIRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;

public class TripAdvisor implements TripAdvisorAPI{

    private static final String BASE_URL = "https://api.content.tripadvisor.com/api/v1/location";
    private final String API_KEY;

    public TripAdvisor(String apiKey) {
        this.API_KEY = apiKey;
    }

    @NonNull
    private String buildUrl(@NonNull HashMap<String, String> queryParams, String pathParams) throws UnsupportedEncodingException {
        StringBuilder builder = new StringBuilder(BASE_URL).append(pathParams).append("?");

        for (Map.Entry<String, String> parameter: queryParams.entrySet()) {
            builder.append(URLEncoder.encode(parameter.getKey(), StandardCharsets.UTF_8.name()))
                    .append("=")
                    .append(URLEncoder.encode(parameter.getValue(), StandardCharsets.UTF_8.name()))
                    .append("&");
        }

        return builder.substring(0, builder.length() - 1);
    }

    @Override
    public void getLocationDetails(@NonNull Integer locationId, Optional<String> language, Optional<String> currency, APIRequest.ResponseCallback callback) {
        HashMap<String, String> requestParams = new HashMap<>();

        String lang = language.orElse("en");
        String cur = currency.orElse("MYR");

        requestParams.put("key", API_KEY);
        requestParams.put("language", lang);
        requestParams.put("currency", cur);

        try {
            // Generated URL should be as follow
            // https://api.content.tripadvisor.com/api/v1/location/{locationId}/details
            String url = buildUrl(requestParams, locationId + "/details");

            APIRequest apiRequest = new APIRequest(url);
            apiRequest.makeRequest(callback);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void searchLocationPhoto(@NonNull Integer locationId, Optional<String> language, OptionalInt limit, OptionalInt offset, Optional<String> source, APIRequest.ResponseCallback callback) {
        HashMap<String, String> requestParams = new HashMap<>();

        String lang = language.orElse("en");
        int num = limit.orElse(5);

        requestParams.put("key", API_KEY);
        requestParams.put("language", lang);
        requestParams.put("limit", Integer.toString(num));
        offset.ifPresent(off -> requestParams.put("offset", Integer.toString(off)));

        try {
            // Generated URL should be as follow
            // https://api.content.tripadvisor.com/api/v1/location/{locationId}/photos
            String url = buildUrl(requestParams, locationId + "/photos");

            APIRequest apiRequest = new APIRequest(url);
            apiRequest.makeRequest(callback);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void searchLocationReview(@NonNull Integer locationId, Optional<String> language, OptionalInt limit, OptionalInt offset, APIRequest.ResponseCallback callback) {
        HashMap<String, String> requestParams = new HashMap<>();

        String lang = language.orElse("en");
        int lim = limit.orElse(5);

        requestParams.put("key", API_KEY);
        requestParams.put("language", lang);
        requestParams.put("limit", Integer.toString(lim));
        offset.ifPresent(off -> requestParams.put("offset", Integer.toString(off)));

        try {
            // Generated URL should be as follow
            // https://api.content.tripadvisor.com/api/v1/location/{locationId}/reviews
            String url = buildUrl(requestParams, locationId + "/reviews");

            APIRequest apiRequest = new APIRequest(url);
            apiRequest.makeRequest(callback);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void nearbyLocationSearch(@NonNull String latLong, Optional<String> category, Optional<String> phone, Optional<String> address, Optional<String> radius, Optional<String> radiusUnit, Optional<String> language, APIRequest.ResponseCallback callback) {
        HashMap<String, String> requestParams = new HashMap<>();

        String lang = language.orElse("MYR");
        String rad = radius.orElse("");
        String radUnit = radiusUnit.orElse("km");

        requestParams.put("latLong", latLong);
        requestParams.put("key", API_KEY);
        category.ifPresent(str -> requestParams.put("category", str));
        phone.ifPresent(str -> requestParams.put("phone", str));
        address.ifPresent(str -> requestParams.put("address", str));
        requestParams.put("radius", rad);
        requestParams.put("radiusUnit", radUnit);
        requestParams.put("language", lang);

        try {
            // Generated URL should be as follow
            // https://api.content.tripadvisor.com/api/v1/location/nearby_search
            String url = buildUrl(requestParams, "nearby_search");

            APIRequest apiRequest = new APIRequest(url);
            apiRequest.makeRequest(callback);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
