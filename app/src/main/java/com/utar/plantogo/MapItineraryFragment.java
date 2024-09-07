package com.utar.plantogo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MapItineraryFragment extends Fragment implements OnMapReadyCallback {

    private FragmentViewModel fragmentViewModel;
    private GoogleMap mMap;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

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
        loadMapData();
    }

    private void loadMapData() {
        executorService.submit(() -> {
            List<PlannedTripsDetails> details = fragmentViewModel.getPlannedTripsDetails();
            List<LatLng> latLngs = extractLocationsFromDetails(details);

            requireActivity().runOnUiThread(() -> {
                for (int i = 0; i < latLngs.size() - 1; i++) {
                    LatLng start = latLngs.get(i);
                    LatLng end = latLngs.get(i + 1);

                    try {
                        String response = RouteFetcher.getDirections(start, end);
                        List<LatLng> routePoints = RouteParser.parseRoute(response);

                        mMap.addPolyline(new PolylineOptions().addAll(routePoints).color(Color.BLUE).width(5));

                        addNumberedMarker(start, i + 1);

                        if (i == latLngs.size() - 2) {
                            addNumberedMarker(end, i + 2);
                        }

                        if (i == 0) {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start, 50));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        });
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

    private void addNumberedMarker(LatLng position, int number) {
        // Create a custom marker icon with the number
        Bitmap bitmap = createNumberedMarkerBitmap(number);
        BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(bitmap);

        // Add the marker to the map
        mMap.addMarker(new MarkerOptions().position(position).icon(icon).title("Stop " + number));
    }

    private Bitmap createNumberedMarkerBitmap(int number) {
        int width = 48;
        int height = 48;
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // Draw circle background
        Paint circlePaint = new Paint();
        circlePaint.setColor(Color.RED);
        canvas.drawCircle(width / 2f, height / 2f, width / 2f, circlePaint);

        // Draw number
        Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(24);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);

        // Calculate the position to center the text
        float x = width / 2f;
        float y = height / 2f - ((textPaint.descent() + textPaint.ascent()) / 2);

        canvas.drawText(String.valueOf(number), x, y, textPaint);

        return bitmap;
    }
}
