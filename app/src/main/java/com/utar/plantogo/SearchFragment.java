package com.utar.plantogo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.utar.plantogo.internal.tripadvisor.TripAdvisor;
import com.utar.plantogo.internal.tripadvisor.model.Location;
import com.utar.plantogo.ui.RecyclerViewItemDecoration;
import com.utar.plantogo.ui.attraction.AttractionListAdapter;
import com.utar.plantogo.ui.viewmodel.FragmentViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String SEARCH_QUERY = "search_query";
    private static final String PRELOAD_DATA = "preload_data";

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