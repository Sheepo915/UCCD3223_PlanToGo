package com.utar.plantogo;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.utar.plantogo.internal.db.AppDatabase;
import com.utar.plantogo.internal.db.model.Location;
import com.utar.plantogo.internal.db.model.PlannedTripsDetails;
import com.utar.plantogo.ui.googlemap.RouteFetcher;
import com.utar.plantogo.ui.googlemap.RouteParser;
import com.utar.plantogo.ui.viewmodel.FragmentViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class MapItineraryFragment extends Fragment implements OnMapReadyCallback {

    private FragmentViewModel fragmentViewModel;
    private GoogleMap mMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentViewModel = new ViewModelProvider(requireActivity()).get(FragmentViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment_itinerary);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        List<LatLng> latLngs = extractLocationsFromDetails(fragmentViewModel.getPlannedTripsDetails());

        new Thread(() -> {
            for (int i = 0; i < latLngs.size() - 1; i++) {
                LatLng start = latLngs.get(i);
                LatLng end = latLngs.get(i + 1);

                try {
                    String response = RouteFetcher.getDirections(start, end);
                    List<LatLng> routePoints = RouteParser.parseRoute(response);

                    int finalI = i;
                    requireActivity().runOnUiThread(() -> {
                        mMap.addPolyline(new PolylineOptions().addAll(routePoints).color(Color.BLUE).width(5));

                        // Add markers with labels
                        mMap.addMarker(new MarkerOptions()
                                .position(start)
                                .title(String.valueOf(finalI + 1))); // Label for the start
                        mMap.addMarker(new MarkerOptions()
                                .position(end)
                                .title(String.valueOf(finalI + 2))); // Label for the end
                    });

                    if (i == 0) {
                        requireActivity().runOnUiThread(() -> mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start, 20)));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private List<LatLng> extractLocationsFromDetails(List<PlannedTripsDetails> details) {
        details.sort(Comparator.comparingInt(detail -> detail.index));
        List<LatLng> locations = Collections.synchronizedList(new ArrayList<>());
        CountDownLatch latch = new CountDownLatch(details.size());

        for (PlannedTripsDetails detail : details) {
            // Use a new thread to fetch location data for each detail
            new Thread(() -> {
                try {
                    // Fetch location from database
                    Location location = AppDatabase.getInstance(requireContext()).locationDao().getLocationById(detail.locationId);

                    if (location != null) {
                        String latitudeStr = location.location.getDetails().getLatitude();
                        String longitudeStr = location.location.getDetails().getLongitude();

                        try {
                            // Convert latitude and longitude to double
                            double latitude = Double.parseDouble(latitudeStr);
                            double longitude = Double.parseDouble(longitudeStr);

                            // Add LatLng to list
                            locations.add(new LatLng(latitude, longitude));
                        } catch (NumberFormatException e) {
                            e.printStackTrace(); // Handle parsing error
                        }
                    }
                } finally {
                    latch.countDown(); // Decrement latch count when done
                }
            }).start();
        }

        // Wait for all threads to finish
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return locations;
    }


}
