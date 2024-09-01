package com.utar.plantogo.internal;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class APIRequest {
    private final URL url;
    private final Map<String, String> headers = new HashMap<>();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private String requestMethod = "GET";
    private String requestBody = null;

    public APIRequest(String urlString) throws Exception {
        Pattern urlPattern = Pattern.compile("^https://.+");
        Matcher matcher = urlPattern.matcher(urlString);

        if (!matcher.matches()) {
            throw new Exception("Url needs to start with https://");
        } else {
            url = new URL(urlString);
        }
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public void setRequestMethod(REQUEST_METHOD method) {
        this.requestMethod = method.name();
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public void makeRequest(ResponseCallback callback) {
        Callable<Map<String, Object>> callable = () -> {
            HttpURLConnection httpURLConnection = null;

            try {
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod(requestMethod);
                httpURLConnection.setDoOutput(true);

                for (Map.Entry<String, String> header : headers.entrySet()) {
                    httpURLConnection.setRequestProperty(header.getKey(), header.getValue());
                }

                if (requestBody != null) {
                    try (OutputStream os = httpURLConnection.getOutputStream()) {
                        byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
                        os.write(input, 0, input.length);
                    }
                }

                InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                String parsedResponse = readStream(inputStream);

                return parseJsonToMap(parsedResponse);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                assert httpURLConnection != null;
                httpURLConnection.disconnect();
            }
        };

        Future<Map<String, Object>> future = executorService.submit(callable);

        executorService.submit(() -> {
            try {
                Map<String, Object> responseMap = future.get(); // Blocking call, waits for the result
                callback.onSuccess(responseMap);
            } catch (Exception e) {
                callback.onError(e);
            }
        });

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

    private Map<String, Object> parseJsonToMap(String jsonString) {
        try {
            // If the response is a JSONObject
            JSONObject jsonObject = new JSONObject(jsonString);
            return jsonToMap(jsonObject);
        } catch (Exception e) {
            try {
                // If the response is a JSONArray
                JSONArray jsonArray = new JSONArray(jsonString);
                return jsonToMap(jsonArray);
            } catch (Exception ex) {
                return new HashMap<>();
            }
        }
    }

    public Map<String, Object> jsonToMap(JSONObject jsonObject) throws Exception {
        Map<String, Object> map = new HashMap<>();
        Iterator<String> keys = jsonObject.keys();

        while (keys.hasNext()) {
            String key = keys.next();
            Object value = jsonObject.get(key);

            if (value instanceof JSONObject) {
                value = jsonToMap((JSONObject) value);
            } else if (value instanceof JSONArray) {
                value = jsonToMap((JSONArray) value);
            }

            map.put(key, value);
        }

        return map;
    }

    private Map<String, Object> jsonToMap(JSONArray jsonArray) throws Exception {
        Map<String, Object> map = new HashMap<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            Object value = jsonArray.get(i);

            if (value instanceof JSONObject) {
                map.put(String.valueOf(i), jsonToMap((JSONObject) value));
            } else if (value instanceof JSONArray) {
                map.put(String.valueOf(i), jsonToMap((JSONArray) value));
            } else {
                map.put(String.valueOf(i), value);
            }
        }

        return map;
    }

    public interface ResponseCallback {
        void onSuccess(Map<String, Object> responseMap);

        void onError(Exception e);
    }

    public enum REQUEST_METHOD {
        GET,
        POST,
        PUT,
        DELETE
    }
}
