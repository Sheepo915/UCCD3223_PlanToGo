package com.utar.plantogo.example.locationsearch;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.utar.plantogo.internal.tripadvisor.model.Location;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class LocationSearchExample {
    public final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Context context;

    public LocationSearchExample(Context context) {
        this.context = context;
    }

    public Future<List<Location>> loadExampleResponse() {
        String fileName = "nearbyLocationSearch.json";

        Callable<List<Location>> callable = () -> {
            try {
                // Simulate network delay
//                TimeUnit.SECONDS.sleep(2); // 2 seconds delay
                String jsonContent;

                // Load the JSON file from the assets directory
                try (InputStream inputStream = context.getAssets().open(fileName);
                     BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                    jsonContent = reader.lines().collect(Collectors.joining(System.lineSeparator()));
                }

                Type listType = new TypeToken<List<Location>>() {
                }.getType();
                List<Location> locations = new Gson().fromJson(jsonContent, listType);

                Log.d("NearbySearch", "loadExampleResponse: " + locations);

                return locations;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };

        return executorService.submit(callable);
    }

    // Ensure proper shutdown of the ExecutorService
    public void shutdown() {
        executorService.shutdown();
    }
}
