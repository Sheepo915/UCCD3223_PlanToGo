package com.utar.plantogo.ui.planner;

import android.content.Context;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.utar.plantogo.PlannerEditFragment;
import com.utar.plantogo.R;

public class PlannerItemComponent extends ConstraintLayout {
    private final FragmentManager fragmentManager;
    private ImageView tripImage;
    private TextView tripName, tripLocation, notes, date;
    private ImageButton editButton;

    public PlannerItemComponent(@NonNull Context context, FragmentManager fragmentManager) {
        super(context);
        this.fragmentManager = fragmentManager;
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.component_planner_item, this);

        tripImage = findViewById(R.id.iv_trip_image);
        tripName = findViewById(R.id.tv_trip_name);
        tripLocation = findViewById(R.id.tv_trip_location);
        notes = findViewById(R.id.tv_notes);
        date = findViewById(R.id.tv_date);
        editButton = findViewById(R.id.ib_edit);
    }

    public void setContent(@Nullable String imageUrl, String tripName, String tripLocation, String notes, String startDate, String endDate) {
        Glide.with(getContext()).load(imageUrl).fallback(R.drawable.placeholder_image).centerCrop().into(tripImage);
        String dateText = startDate + " - " + endDate;

        this.tripName.setText(tripName);
        if (tripLocation != null && !tripLocation.isEmpty()) {
            tripLocation = tripLocation.substring(0, 1).toUpperCase() + tripLocation.substring(1).toLowerCase();
        }
        this.tripLocation.setText(tripLocation);
        this.notes.setText(notes);
        this.date.setText(dateText);
    }

    public void instantiateListener(int id) {
        editButton.setOnClickListener(v -> {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.root_fragment_container, PlannerEditFragment.newInstance(id));
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });
    }
}
