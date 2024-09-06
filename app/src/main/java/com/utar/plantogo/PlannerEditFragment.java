package com.utar.plantogo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.utar.plantogo.internal.db.AppDatabase;
import com.utar.plantogo.internal.db.model.PlannedTripsWithDetails;
import com.utar.plantogo.ui.planner.PlannerDetailsOverviewAdapter;

public class PlannerEditFragment extends Fragment {

    private static final String PLANNED_TRIP_ID = "ID";
    private int tripId;

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

        if (getArguments() != null) {
            tripId = getArguments().getInt(PLANNED_TRIP_ID);
        }

        AppDatabase db = AppDatabase.getInstance(requireContext());
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
        tripName.setText(plannedTripsWithDetails.plannedTrips.tripName);

        TextView tripDate = view.findViewById(R.id.tv_date);
        String startToEndDate = plannedTripsWithDetails.plannedTrips.startDate + " - " + plannedTripsWithDetails.plannedTrips.endDate;
        tripDate.setText(startToEndDate);

        // Set default activated content
        TextView overview = view.findViewById(R.id.tv_overview);

        TextView itinerary = view.findViewById(R.id.tv_itinerary);

        RecyclerView contentContainer = view.findViewById(R.id.rv_planned_trip_container);
        contentContainer.setLayoutManager(new LinearLayoutManager(requireContext()));

        TextView noTripsMessage = view.findViewById(R.id.tv_no_trips_message);


        // Default view
        instantiateOverviewListener(overview, itinerary, noTripsMessage, contentContainer);

        overview.setOnClickListener(v -> {
            instantiateOverviewListener(overview, itinerary, noTripsMessage, contentContainer);
        });

        itinerary.setOnClickListener(v -> {
            overview.setText(R.string.overview);
            itinerary.setText(R.string.itinerary_active);
        });

        return view;
    }

    private void instantiateOverviewListener(TextView overview, TextView itinerary, TextView noTripsMessage, RecyclerView contentContainer) {
        overview.setText(R.string.overview_active);
        itinerary.setText(R.string.itinerary);

        if (plannedTripsWithDetails.tripsDetails == null || plannedTripsWithDetails.tripsDetails.isEmpty()) {
            noTripsMessage.setVisibility(View.VISIBLE);
            contentContainer.setVisibility(View.GONE);
        } else {
            noTripsMessage.setVisibility(View.GONE);
            contentContainer.setVisibility(View.VISIBLE);

            PlannerDetailsOverviewAdapter adapter = new PlannerDetailsOverviewAdapter(requireContext(), plannedTripsWithDetails.tripsDetails);
            contentContainer.swapAdapter(adapter, true);
        }
    }

}

