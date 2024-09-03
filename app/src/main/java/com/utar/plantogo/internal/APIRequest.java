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

/**
 * A utility class for making HTTP requests with support for headers, request body, and different HTTP methods.
 * <pre>
 *   try {
 *     // Create an APIRequest instance with the URL
 *     APIRequest apiRequest = new APIRequest("https://jsonplaceholder.typicode.com/posts");
 *
 *     // Set the request method to POST
 *     apiRequest.setRequestMethod(APIRequest.REQUEST_METHOD.POST);
 *
 *     // Set a sample request body
 *     apiRequest.setRequestBody("{\"title\": \"foo\", \"body\": \"bar\", \"userId\": 1}");
 *
 *     // Add a header
 *     apiRequest.addHeader("Content-Type", "application/json; charset=UTF-8");
 *
 *     // Make the request with a callback
 *     apiRequest.makeRequest(new APIRequest.ResponseCallback() {
 *        \@Override
 *        public void onSuccess(Map<String, Object> responseMap) {
 *          System.out.println("Response: " + responseMap);
 *        }
 *
 *        \@Override
 *         public void onError(Exception e) {
 *           System.err.println("Error: " + e.getMessage());
 *         }
 *      });
 *   } catch (Exception e) {
 *     e.printStackTrace();
 *   }
 * </pre>
 */
public class APIRequest {
    private final URL url;
    private final Map<String, String> headers = new HashMap<>();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private String requestMethod = "GET";
    private String requestBody = null;

    /**
     * Constructs an APIRequest object with the specified URL.
     *
     * @param urlString The URL string for the API request.
     * @throws Exception if the URL does not start with "https://".
     */
    public APIRequest(String urlString) throws Exception {
        Pattern urlPattern = Pattern.compile("^https://.+");
        Matcher matcher = urlPattern.matcher(urlString);

        if (!matcher.matches()) {
            throw new Exception("Url needs to start with https://");
        } else {
            url = new URL(urlString);
        }
    }

    /**
     * Sets the request body for the API request.
     *
     * @param requestBody The request body as a String.
     */
    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    /**
     * Sets the HTTP request method for the API request.
     *
     * @param method The HTTP method, e.g., GET, POST, PUT, DELETE.
     */
    public void setRequestMethod(REQUEST_METHOD method) {
        this.requestMethod = method.name();
    }

    /**
     * Adds a header to the API request.
     *
     * @param key   The header name.
     * @param value The header value.
     */
    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    /**
     * Executes the API request asynchronously and handles the response via a callback.
     *
     * @param callback The callback to handle success or error.
     */
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

    /**
     * Reads the input stream and converts it to a String.
     *
     * @param rawResponse The raw input stream from the HTTP connection.
     * @return The response content as a String.
     */
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

    /**
     * Parses a JSON string to a Map.
     *
     * @param jsonString The JSON string.
     * @return A map representing the JSON structure.
     */
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

    /**
     * Converts a JSONObject to a Map.
     *
     * @param jsonObject The JSONObject to convert.
     * @return A map representing the JSON object.
     * @throws Exception if an error occurs during parsing.
     */
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

    /**
     * Converts a JSONArray to a Map.
     *
     * @param jsonArray The JSONArray to convert.
     * @return A map representing the JSON array.
     * @throws Exception if an error occurs during parsing.
     */
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

    /**
     * A callback interface for handling API responses.
     */
    public interface ResponseCallback {
        /**
         * Called when the API request is successful.
         *
         * @param responseMap The parsed response as a map.
         */
        void onSuccess(Map<String, Object> responseMap);

        /**
         * Called when an error occurs during the API request.
         *
         * @param e The exception that occurred.
         */
        void onError(Exception e);
    }

    /**
     * An enum representing the possible HTTP request methods.
     */
    public enum REQUEST_METHOD {
        GET,
        POST,
        PUT,
        DELETE
    }
}
