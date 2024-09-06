package com.utar.plantogo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.utar.plantogo.internal.db.AppDatabase;
import com.utar.plantogo.internal.db.model.PlannedTripsWithDetails;

public class PlannerEditFragment extends Fragment {

    private static final String PLANNED_TRIP_ID = "ID";

    private PlannedTripsWithDetails plannedTripsWithDetails;

    public PlannerEditFragment() {
    }

    public static PlannerEditFragment newInstance(int id) {
        PlannerEditFragment fragment = new PlannerEditFragment();
        Bundle args = new Bundle();
        args.putInt(PLANNED_TRIP_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppDatabase db = AppDatabase.getInstance(requireContext());

        int tripId;
        if (savedInstanceState != null) {
            tripId = savedInstanceState.getInt(PLANNED_TRIP_ID, -1);
        } else if (getArguments() != null) {
            tripId = getArguments().getInt(PLANNED_TRIP_ID, -1);
        } else {
            throw new IllegalStateException("PLANNED_TRIP_ID not found");
        }

        if (tripId != -1) {
            new Thread(() -> {
                plannedTripsWithDetails = db.plannedTripsDao().getPlannedTripsWithDetailsById(tripId);
            }).start();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_planner_details, container, false);

        TextView tripName = view.findViewById(R.id.tv_trip_name);
        tripName.setText(plannedTripsWithDetails.plannedTrip.tripName);

        TextView tripDate = view.findViewById(R.id.tv_date);
        String startToEndDate = plannedTripsWithDetails.plannedTrip.startDate + " - " + plannedTripsWithDetails.plannedTrip.endDate;
        tripDate.setText(startToEndDate);

        // Set default activated content
        TextView overview = view.findViewById(R.id.tv_overview);
        overview.setText(R.string.overview_active);



        return view;
    }


}

