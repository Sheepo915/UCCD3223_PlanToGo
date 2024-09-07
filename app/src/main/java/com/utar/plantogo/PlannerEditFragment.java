package com.utar.plantogo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.utar.plantogo.internal.db.AppDatabase;
import com.utar.plantogo.internal.db.model.PlannedTripsWithDetails;
import com.utar.plantogo.ui.DragAndDropCallback;
import com.utar.plantogo.ui.planner.PlannerDetailsItineraryAdapter;
import com.utar.plantogo.ui.planner.PlannerDetailsOverviewAdapter;
import com.utar.plantogo.ui.viewmodel.FragmentViewModel;

public class PlannerEditFragment extends Fragment {

    private static final String PLANNED_TRIP_ID = "ID";
    private int tripId;

    private PlannedTripsWithDetails plannedTripsWithDetails;
    private FragmentViewModel fragmentViewModel;

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

        fragmentViewModel = new ViewModelProvider(requireActivity()).get(FragmentViewModel.class);

        if (getArguments() != null) {
            tripId = getArguments().getInt(PLANNED_TRIP_ID);
        }

        loadPlannedTripsWithDetails(tripId);
    }

    private void loadPlannedTripsWithDetails(int tripId) {
        AppDatabase db = AppDatabase.getInstance(requireContext());
        new Thread(() -> {
            plannedTripsWithDetails = db.plannedTripsDao().getPlannedTripsWithDetailsById(tripId);

            // Post results to the main thread
            requireActivity().runOnUiThread(() -> {
                if (getView() != null) {
                    updateUIWithData(getView(), plannedTripsWithDetails);
                }
            });
        }).start();
    }

    private void updateUIWithData(View view, PlannedTripsWithDetails plannedTripsWithDetails) {
        TextView tripName = view.findViewById(R.id.tv_trip_name);
        TextView tripDate = view.findViewById(R.id.tv_date);
        TextView overview = view.findViewById(R.id.tv_overview);
        TextView itinerary = view.findViewById(R.id.tv_itinerary);
        RecyclerView contentContainer = view.findViewById(R.id.rv_planned_trip_container);
        TextView noTripsMessage = view.findViewById(R.id.tv_no_trips_message);
        ImageButton mapButton = view.findViewById(R.id.ib_show_map);

        tripName.setText(plannedTripsWithDetails.plannedTrips.tripName);
        String startToEndDate = plannedTripsWithDetails.plannedTrips.startDate + " - " + plannedTripsWithDetails.plannedTrips.endDate;
        tripDate.setText(startToEndDate);

        // Decide which view to show
        instantiateOverviewListener(overview, itinerary, noTripsMessage, contentContainer, mapButton);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_planner_details, container, false);

        ImageButton viewMap = view.findViewById(R.id.ib_show_map);
        TextView tripName = view.findViewById(R.id.tv_trip_name);
        TextView tripDate = view.findViewById(R.id.tv_date);
        TextView overview = view.findViewById(R.id.tv_overview);
        TextView itinerary = view.findViewById(R.id.tv_itinerary);
        RecyclerView contentContainer = view.findViewById(R.id.rv_planned_trip_container);
        TextView noTripsMessage = view.findViewById(R.id.tv_no_trips_message);
        ImageButton mapButton = view.findViewById(R.id.ib_show_map);

        contentContainer.setLayoutManager(new LinearLayoutManager(requireContext()));

        instantiateOverviewListener(overview, itinerary, noTripsMessage, contentContainer, mapButton);

        overview.setOnClickListener(v -> {
            instantiateOverviewListener(overview, itinerary, noTripsMessage, contentContainer, mapButton);
        });

        itinerary.setOnClickListener(v -> {
            instantiateItineraryListener(overview, itinerary, contentContainer, mapButton);
        });

        viewMap.setOnClickListener(v -> {
            handleMapNavigation();
        });

        if (plannedTripsWithDetails != null) {
            updateUIWithData(view, plannedTripsWithDetails);
        }

        return view;
    }

    private void handleMapNavigation() {
        fragmentViewModel.setPlannedTripsDetails(plannedTripsWithDetails.tripsDetails);

        MapItineraryFragment mapFragment = new MapItineraryFragment();
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.root_fragment_container, mapFragment).addToBackStack(null);
        fragmentTransaction.commit();
    }


    private void instantiateItineraryListener(TextView overview, TextView itinerary, RecyclerView contentContainer, ImageButton mapButton) {
        overview.setText(R.string.overview);
        itinerary.setText(R.string.itinerary_active);
        mapButton.setVisibility(View.VISIBLE);

        if (plannedTripsWithDetails.tripsDetails != null) {
            PlannerDetailsItineraryAdapter adapter = new PlannerDetailsItineraryAdapter(requireContext(), plannedTripsWithDetails.tripsDetails);
            contentContainer.setAdapter(adapter);

            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new DragAndDropCallback(adapter));
            itemTouchHelper.attachToRecyclerView(contentContainer);
        }
    }

    private void instantiateOverviewListener(TextView overview, TextView itinerary, TextView noTripsMessage, RecyclerView contentContainer, ImageButton mapButton) {
        overview.setText(R.string.overview_active);
        itinerary.setText(R.string.itinerary);
        mapButton.setVisibility(View.GONE);

        if (plannedTripsWithDetails.tripsDetails == null || plannedTripsWithDetails.tripsDetails.isEmpty()) {
            noTripsMessage.setVisibility(View.VISIBLE);
            contentContainer.setVisibility(View.GONE);
        } else {
            noTripsMessage.setVisibility(View.GONE);
            contentContainer.setVisibility(View.VISIBLE);

            contentContainer.clearOnChildAttachStateChangeListeners();

            PlannerDetailsOverviewAdapter adapter = new PlannerDetailsOverviewAdapter(requireContext(), plannedTripsWithDetails.tripsDetails);
            contentContainer.setAdapter(adapter);

        }
    }

}

