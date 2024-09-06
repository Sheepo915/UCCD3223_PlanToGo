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
    private ImageView tripImage;
    private TextView tripName, tripLocation, notes, date;
    private final FragmentManager fragmentManager;

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
        ImageButton editButton = findViewById(R.id.ib_edit);

        editButton.setOnClickListener(v -> {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.root_fragment_container, new PlannerEditFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

    }

    public void setContent(@Nullable String imageUrl, String tripName, String tripLocation, String notes, String startDate, String endDate) {
        Glide.with(getContext()).load(imageUrl).fallback(R.drawable.placeholder_image).centerCrop().into(tripImage);
        this.tripName.setText(tripName);
        this.tripLocation.setText(tripLocation);
        this.notes.setText(notes);
        this.date.setText(startDate + " - " + endDate);
    }
}
