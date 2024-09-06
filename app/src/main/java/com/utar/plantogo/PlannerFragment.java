package com.utar.plantogo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PlannerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentPlanner.
     */
    // TODO: Rename and change types and number of parameters
    public static PlannerFragment newInstance(String param1, String param2) {
        PlannerFragment fragment = new PlannerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_planner, container, false);

        ImageButton newTripBtn = view.findViewById(R.id.ib_add_new_trip);
        newTripBtn.setOnClickListener(v -> {
            navigateToPlannerAddFragment();
        });

        RecyclerView plannedTripsContainer = view.findViewById(R.id.rv_planned_trip_list);
        plannedTripsContainer.setLayoutManager(new LinearLayoutManager(getContext()));

        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(requireContext());

            List<PlannedTripsWithDetails> plannedTrips = db.plannedTripsDao().getPlannedTripsWithDetails();

            requireActivity().runOnUiThread(() -> {
                PlannedTripsAdapter adapter = new PlannedTripsAdapter(plannedTrips, requireContext(), getParentFragmentManager());
                plannedTripsContainer.setAdapter(adapter);
            });

        }).start();

        return view;
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