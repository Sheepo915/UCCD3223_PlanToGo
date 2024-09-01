package com.utar.plantogo.example.nearbysearch;


import android.content.Context;
import android.util.Log;

import com.utar.plantogo.internal.APIRequest;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class NearbySearchExample {

    public final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Context context;

    public NearbySearchExample(Context context) {
        this.context = context;
    }

    public Future<Map<String, Object>> loadExampleResponse() {
        String fileName = "nearbyLocationSearch.json";

        Callable<Map<String, Object>> callable = () -> {
            try {
                // Simulate network delay
                TimeUnit.SECONDS.sleep(2); // 2 seconds delay
                String jsonContent;

                // Load the JSON file from the assets directory
                try (InputStream inputStream = context.getAssets().open(fileName);
                     BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

                    jsonContent = reader.lines().collect(Collectors.joining(System.lineSeparator()));
                    Log.d("File", jsonContent);
                }

                // Parse the JSON content
                JSONObject jsonObject = new JSONObject(jsonContent);
                Log.d("File", jsonObject.toString());

                // Convert the JSONObject to a Map
                APIRequest apiRequest = new APIRequest("https://example.com"); // Dummy APIRequest for parsing
                return apiRequest.jsonToMap(jsonObject);
            } catch (Exception e) {
                Log.e("File", "Failed to load or parse JSON", e);
                throw new RuntimeException(e);
            }
        };

        return executorService.submit(callable);
    }

    // Ensure proper shutdown of the ExecutorService
    public void shutdown() {
        executorService.shutdown();
    }

    private String readStream(InputStream rawResponse) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = rawResponse.read();
            while (i != -1) {
                bo.write(i);
                i = rawResponse.read();
            }
            return bo.toString();
        } catch (IOException e) {
            return "";
        }
    }
}
