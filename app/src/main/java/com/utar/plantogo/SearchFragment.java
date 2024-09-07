package com.utar.plantogo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.utar.plantogo.internal.APIRequest;
import com.utar.plantogo.internal.tripadvisor.TripAdvisor;
import com.utar.plantogo.internal.tripadvisor.model.Location;
import com.utar.plantogo.ui.RecyclerViewItemDecoration;
import com.utar.plantogo.ui.attraction.AttractionListAdapter;
import com.utar.plantogo.ui.viewmodel.FragmentViewModel;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    private static final String SEARCH_QUERY = "search_query";
    private static final String PRELOAD_DATA = "preload_data";

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

        fragmentViewModel = new ViewModelProvider(this).get(FragmentViewModel.class);

        if (getArguments() != null) {
            searchQuery = getArguments().getString(SEARCH_QUERY);
            String latLong = fragmentViewModel.getLatitude() + ", " + fragmentViewModel.getLongitude();
            new Thread(() -> {
                new TripAdvisor().locationSearch(searchQuery, latLong, null, null, null, null, null, null, new APIRequest.ResponseCallback() {
                    @Override
                    public void onSuccess(Map<String, Object> responseMap) {
                        Gson gson = new Gson();
                        String jsonString = gson.toJson(responseMap);

                        // Convert JSON to the desired object or handle it as needed
                        // Example: Convert JSON to a list of Location objects
                        Type locationListType = new TypeToken<List<Location>>() {
                        }.getType();
                        List<com.utar.plantogo.internal.tripadvisor.model.Location> locations = gson.fromJson(jsonString, locationListType);

                        // Use the location data (e.g., update UI or process data)
                        // Example: Log location names
                        for (com.utar.plantogo.internal.tripadvisor.model.Location location : locations) {
                            Log.d("LocationSearch", "Location Name: " + location.getName());
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("LocationSearch", "Error: " + e.getMessage());
                    }
                });
            }).start();
        }
        fragmentViewModel = new ViewModelProvider(requireActivity()).get(FragmentViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Get the data from view model
        preloadData = fragmentViewModel.getPreloadData();

        attractionListRecyclerView = view.findViewById(R.id.rv_attraction_list);

        setupAttractionList(preloadData);

        return view;
    }

    private void setupAttractionList(List<Location> data) {
        // Set the LayoutManager
        attractionListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Create and set the adapter
        AttractionListAdapter adapter = new AttractionListAdapter(getContext(), data, getParentFragmentManager(), fragmentViewModel);
        attractionListRecyclerView.setAdapter(adapter);

        int itemSpacing = (int) (12 * getResources().getDisplayMetrics().density);
        int marginEnd = (int) (10 * getResources().getDisplayMetrics().density);

        attractionListRecyclerView.addItemDecoration(new RecyclerViewItemDecoration(0, marginEnd, itemSpacing, RecyclerViewItemDecoration.Direction.VERTICAL));
    }

}