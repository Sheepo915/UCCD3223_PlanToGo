package com.utar.plantogo.internal.tripadvisor;

import androidx.annotation.NonNull;

import com.utar.plantogo.internal.APIRequest;

import java.util.Optional;
import java.util.OptionalInt;

public interface TripAdvisorAPI {
    /**
     * A Location Details request returns comprehensive information about a location (hotel, restaurant, or an attraction) such as name, address, rating, and URLs for the listing on Tripadvisor.
     * @param locationId A unique identifier for a location on Tripadvisor. The location ID can be obtained using the Location Search.
     * @param language The language in which to return results (e.g. "en" for English or "es" for Spanish) from the list of our Supported Languages
     * @param currency The currency code to use for request and response (should follow ISO 4217).
     */
    void getLocationDetails(@NonNull Integer locationId, Optional<String> language, Optional<String> currency, APIRequest.ResponseCallback callback);

    /**
     * <p>The Location Photos request returns up to 5 high-quality photos for a specific location.</p>
     * <p>The photos are ordered by recency.</p>
     *
     * @param locationId A unique identifier for a location on Tripadvisor. The location ID can be obtained using the Location Search.
     * @param language   The language in which to return results (e.g. "en" for English or "es" for Spanish) from the list of our Supported Languages.
     * @param limit      The number of results to return
     * @param offset     The index of the first result
     * @param source     A comma-separated list of allowed photo sources. Allowed values are 'Expert', 'Management', 'Traveler'. If not specified, allow photos from all sources.
     */
    void searchLocationPhoto(@NonNull Integer locationId, Optional<String> language, OptionalInt limit, OptionalInt offset, Optional<String> source, APIRequest.ResponseCallback callback);

    /**
     * The Location Reviews request returns up to 5 of the most recent reviews for a specific location.
     * @param locationId A unique identifier for a location on Tripadvisor. The location ID can be obtained using the Location Search.
     * @param language The language in which to return results (e.g. "en" for English or "es" for Spanish) from the list of our Supported Languages.
     * @param limit The number of results to return
     * @param offset The index of the first result
     */
    void searchLocationReview(@NonNull Integer locationId, Optional<String> language, OptionalInt limit, OptionalInt offset, APIRequest.ResponseCallback callback);

    /**
     * The Nearby Location Search request returns up to 10 locations found near the given latitude/longtitude.
     * @param latLong Latitude/Longitude pair to scope down the search around a specifc point - eg. "42.3455,-71.10767"
     * @param category Filters result set based on property type. Valid options are "hotels", "attractions", "restaurants", and "geos"
     * @param phone Phone number to filter the search results by (this can be in any format with spaces and dashes but without the "+" sign at the beginning)
     * @param address Address to filter the search results by
     * @param radius Length of the radius from the provided latitude/longitude pair to filter results.
     * @param radiusUnit Unit for length of the radius. Valid options are "km", "mi", "m" (km=kilometers, mi=miles, m=meters)
     * @param language The language in which to return results (e.g. "en" for English or "es" for Spanish) from the list of our Supported Languages.
     */
    void nearbyLocationSearch(@NonNull String latLong, Optional<String> category, Optional<String> phone, Optional<String> address, Optional<String> radius, Optional<String> radiusUnit, Optional<String> language, APIRequest.ResponseCallback callback);

}
