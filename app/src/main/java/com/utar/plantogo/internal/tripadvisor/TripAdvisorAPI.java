package com.utar.plantogo.internal.tripadvisor;

import androidx.annotation.NonNull;

import com.utar.plantogo.internal.APIRequest;

import java.util.Optional;

/**
 * TripAdvisor API requires a secure domain to be able to access their API content
 * Thus, using Supabase as intermediary; Supabase Edge Function is used to perform such action and act as a proxy.
 */
public interface TripAdvisorAPI {

    /**
     * The Nearby Location Search request returns up to 10 locations found near the given latitude/longtitude.
     *
     * @param latLong    Latitude/Longitude pair to scope down the search around a specifc point - eg. "42.3455,-71.10767"
     * @param category   Filters result set based on property type. Valid options are "hotels", "attractions", "restaurants", and "geos"
     * @param phone      Phone number to filter the search results by (this can be in any format with spaces and dashes but without the "+" sign at the beginning)
     * @param address    Address to filter the search results by
     * @param radius     Length of the radius from the provided latitude/longitude pair to filter results.
     * @param radiusUnit Unit for length of the radius. Valid options are "km", "mi", "m" (km=kilometers, mi=miles, m=meters)
     * @param language   The language in which to return results (e.g. "en" for English or "es" for Spanish) from the list of our Supported Languages.
     * @param callback   Callback function for handling data returned from the API
     */
    void nearbyLocationSearch(@NonNull String latLong, Optional<String> category, Optional<String> phone, Optional<String> address, Optional<String> radius, Optional<String> radiusUnit, Optional<String> language, APIRequest.ResponseCallback callback);

    /**
     * Location Search request that returns up to 10 location found based on the provided search query and with nearby location set to default on
     * @param searchQuery   Search query that will be send to TripAdvisor to perform location search
     * @param latLong       Latitude/Longitude pair to scope down the search around a specifc point - eg. "42.3455,-71.10767"
     * @param category      Filters result set based on property type. Valid options are "hotels", "attractions", "restaurants", and "geos"
     * @param phone         Phone number to filter the search results by (this can be in any format with spaces and dashes but without the "+" sign at the beginning)
     * @param address       Address to filter the search results by
     * @param radius        Length of the radius from the provided latitude/longitude pair to filter results.
     * @param radiusUnit    Unit for length of the radius. Valid options are "km", "mi", "m" (km=kilometers, mi=miles, m=meters)
     * @param language      The language in which to return results (e.g. "en" for English or "es" for Spanish) from the list of our Supported Languages.
     * @param callback      Callback function for handling data returned from the API
     */
    void locationSearch(@NonNull String searchQuery, Optional<String> latLong, Optional<String> category, Optional<String> phone, Optional<String> address, Optional<String> radius, Optional<String> radiusUnit, Optional<String> language, APIRequest.ResponseCallback callback);
}
