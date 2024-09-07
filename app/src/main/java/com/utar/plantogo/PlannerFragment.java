package com.utar.plantogo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.utar.plantogo.internal.db.AppDatabase;
import com.utar.plantogo.internal.db.model.PlannedTripsWithDetails;
import com.utar.plantogo.ui.planner.PlannedTripsAdapter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlannerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlannerFragment extends Fragment {

    private PlannedTripsAdapter adapter;
    private List<PlannedTripsWithDetails> plannedTrips;

    public PlannerFragment() {
        // Required empty public constructor
    }

    public static PlannerFragment newInstance(String param1, String param2) {
        return new PlannerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_planner, container, false);
        setupUI(view);
        return view;
    }

    private void setupUI(View view) {
        ImageButton newTripBtn = view.findViewById(R.id.ib_add_new_trip);
        newTripBtn.setOnClickListener(v -> navigateToPlannerAddFragment());

        SearchView searchView = view.findViewById(R.id.sv_attraction_search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter != null) {
                    adapter.getFilter().filter(newText);
                }
                return true;
            }
        });

        RecyclerView plannedTripsContainer = view.findViewById(R.id.rv_planned_trip_list);
        plannedTripsContainer.setLayoutManager(new LinearLayoutManager(getContext()));

        loadPlannedTrips(plannedTripsContainer);
    }

    private void loadPlannedTrips(RecyclerView plannedTripsContainer) {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(requireContext());
            plannedTrips = db.plannedTripsDao().getPlannedTripsWithDetails();

            requireActivity().runOnUiThread(() -> {
                adapter = new PlannedTripsAdapter(plannedTrips, requireContext(), getParentFragmentManager());
                plannedTripsContainer.setAdapter(adapter);
            });
        }).start();
    }


    private void navigateToPlannerAddFragment() {
        // Navigate to the SearchFragment
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.root_fragment_container, new PlannerAddFragment());
        transaction.addToBackStack(null);
        // Commit the transaction
        transaction.commit();
    }

}