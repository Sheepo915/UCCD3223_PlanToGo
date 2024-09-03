package com.utar.plantogo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.utar.plantogo.internal.tripadvisor.model.Location;
import com.utar.plantogo.ui.RecyclerViewItemDecoration;
import com.utar.plantogo.ui.attraction.AttractionListAdapter;
import com.utar.plantogo.ui.viewmodel.FragmentViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String SEARCH_QUERY = "search_query";
    private static final String  PRELOAD_DATA = "preload_data";

    // TODO: Rename and change types of parameters
    private String searchQuery;
    private List<Location> preloadData;

    private FragmentViewModel fragmentViewModel;
    private RecyclerView attractionListRecyclerView;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param searchQuery Search query that will be passed to Tripadvisor API to perform location search.
     * @return A new instance of fragment FragmentPlanner.
     */
    public static SearchFragment newInstance(String searchQuery) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(SEARCH_QUERY, searchQuery);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            searchQuery = getArguments().getString(SEARCH_QUERY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Get the data from view model
        fragmentViewModel = new ViewModelProvider(requireActivity()).get(FragmentViewModel.class);
        preloadData = fragmentViewModel.getPreloadData();

        attractionListRecyclerView = view.findViewById(R.id.rv_attraction_list);

        setupAttractionList(preloadData);

        configureToolbarForSearchFragment();
        configureBottomNavigationBarVisibility(false);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        configureBottomNavigationBarVisibility(true);
        configureToolbarOnDestroy();
    }

    private void setupAttractionList(List<Location> data) {
        // Set the LayoutManager
        attractionListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Create and set the adapter
        AttractionListAdapter adapter = new AttractionListAdapter(getContext(), data);
        attractionListRecyclerView.setAdapter(adapter);

        int itemSpacing = (int) (12 * getResources().getDisplayMetrics().density);
        int marginEnd = (int) (10 * getResources().getDisplayMetrics().density);

        attractionListRecyclerView.addItemDecoration(new RecyclerViewItemDecoration(0, marginEnd, itemSpacing, RecyclerViewItemDecoration.Direction.VERTICAL));
    }

    /**
     * Custom configuration for search fragment toolbar
     */
    private void configureToolbarForSearchFragment() {
        AppCompatActivity activity = (AppCompatActivity) requireActivity();

        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);

        Objects.requireNonNull(activity.getSupportActionBar()).setTitle("Search");
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(v -> {
            requireActivity().onBackPressed(); // Handle back press
        });

        View profileHeaderContainer = activity.findViewById(R.id.cl_profile_header_container);
        if (profileHeaderContainer != null) {
            profileHeaderContainer.setVisibility(View.GONE);
        }
    }

    /**
     * Configure the visibility of the bottom navigation bar during change in fragment
     * @param visible true if visible, false if gone
     */
    private void configureBottomNavigationBarVisibility(boolean visible) {
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation_bar);

        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * Configure the toolbar during fragment destroy in fragment lifecycle
     */
    private void configureToolbarOnDestroy() {
        AppCompatActivity activity = (AppCompatActivity) requireActivity();

        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);

        Objects.requireNonNull(activity.getSupportActionBar()).setDisplayShowTitleEnabled(false);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        View profileHeaderContainer = activity.findViewById(R.id.cl_profile_header_container);
        if (profileHeaderContainer != null) {
            profileHeaderContainer.setVisibility(View.VISIBLE);
        }
    }
}