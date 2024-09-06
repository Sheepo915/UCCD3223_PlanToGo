package com.utar.plantogo.ui.planner;

import android.content.Context;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.utar.plantogo.MainActivity;
import com.utar.plantogo.R;
import com.utar.plantogo.internal.APIRequest;
import com.utar.plantogo.internal.db.AppDatabase;
import com.utar.plantogo.internal.db.model.Location;
import com.utar.plantogo.internal.db.model.PlannedTripsDetails;
import com.utar.plantogo.internal.tripadvisor.TripAdvisor;

import java.util.List;
import java.util.Map;

public class PlannerEditComponent extends ConstraintLayout {
    private final Context context;
    TextView index, locationName, address, notes, date;
    ImageView image;
    ImageButton deleteButton;
    private PlannedTripsDetails plannedTripsDetails;

    public PlannerEditComponent(@NonNull Context context) {
        super(context);
        this.context = context;
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.component_plan_edit_overview, this);

        index = findViewById(R.id.tv_index);
        locationName = findViewById(R.id.tv_location_name);
        address = findViewById(R.id.tv_address);
        notes = findViewById(R.id.tv_notes);
        date = findViewById(R.id.tv_date);
        image = findViewById(R.id.iv_trip_image);
        deleteButton = findViewById(R.id.ib_delete);

        deleteButton.setOnClickListener(v -> {
            new Thread(() -> {
                AppDatabase db = AppDatabase.getInstance(context);
                db.plannedTripsDao().deleteLocationInTrip(plannedTripsDetails);
            }).start();
        });
    }

    public void setContent(PlannedTripsDetails plannedTripsDetails) {
        this.plannedTripsDetails = plannedTripsDetails;

        index.setText(String.valueOf(plannedTripsDetails.index));
        notes.setText(plannedTripsDetails.notes != null ? plannedTripsDetails.notes : "");
        date.setText(plannedTripsDetails.plannedTimestamp != null ? plannedTripsDetails.plannedTimestamp : "");

        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(context);
            Location location = db.locationDao().getLocationById(plannedTripsDetails.locationId);

            if (location == null) {
                new TripAdvisor().locationDetails(String.valueOf(plannedTripsDetails.locationId), new APIRequest.ResponseCallback() {
                    @Override
                    public void onSuccess(Map<String, Object> responseMap) {
                        com.utar.plantogo.internal.tripadvisor.model.Location locationData = new Gson().fromJson(new Gson().toJson(responseMap), com.utar.plantogo.internal.tripadvisor.model.Location.class);

                        // Insert Location into database
                        db.locationDao().insertLocation(new Location(locationData));

                        // Update UI on the main thread
                        ((MainActivity) context).runOnUiThread(() -> updateLocationUI(locationData));
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.d("DEBUG", e.toString());
                    }
                });
            } else {
                // Location found in DB, update UI on the main thread
                ((MainActivity) context).runOnUiThread(() -> updateLocationUI(location.location));
            }

        }).start();
    }

    private void updateLocationUI(com.utar.plantogo.internal.tripadvisor.model.Location locationData) {
        if (locationName != null) {
            locationName.setText(locationData.getName());
        } else {
            Log.d("PlannerEditComponent", "locationName is null");
        }

        if (address != null) {
            address.setText(locationData.getAddressObj().getAddressString());
        } else {
            Log.d("PlannerEditComponent", "address is null");
        }

        if (image != null) {
            List<com.utar.plantogo.internal.tripadvisor.model.Photo> photos = locationData.getPhotos();

            if (photos != null && !photos.isEmpty()) {
                String imageUrl = String.valueOf(photos.get(0).getImages().getFallbackImage().getUrl());
                Glide.with(context).load(imageUrl).centerCrop().placeholder(R.drawable.placeholder_image).error(R.drawable.placeholder_image).into(image);
            } else {
                image.setImageResource(R.drawable.placeholder_image);
            }
        } else {
            Log.d("PlannerEditComponent", "image is null");
        }
    }
}

