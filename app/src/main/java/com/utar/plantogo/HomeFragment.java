package com.utar.plantogo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.utar.plantogo.example.nearbysearch.NearbySearchExample;
import com.utar.plantogo.internal.tripadvisor.model.Location;
import com.utar.plantogo.ui.attraction.AttractionListAdapter;
import com.utar.plantogo.ui.carousel.CarouselAdapter;
import com.utar.plantogo.ui.RecyclerViewItemDecoration;

import java.util.List;
import java.util.concurrent.Future;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private FrameLayout carouselContainer;
    private RecyclerView carouselRecyclerView, attractionListRecyclerView;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Attraction Carousel
        carouselContainer = view.findViewById(R.id.fl_carousel_container);
        carouselRecyclerView = new RecyclerView(requireContext());

        attractionListRecyclerView = view.findViewById(R.id.rv_attraction_list);

        // Create an instance of NearbySearchExample
        NearbySearchExample example = new NearbySearchExample(getContext());
        Future<List<Location>> futureNearbyLocations = example.loadExampleResponse();

        // Update the TextView once the future is done
        example.executorService.submit(() -> {
            try {
                List<Location> nearbyLocations = futureNearbyLocations.get();

                requireActivity().runOnUiThread(() -> {
                    setupCarousel(nearbyLocations);
                    setupRecyclerView(nearbyLocations);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return view;
    }

    private void setupCarousel(List<Location> data) {
        // Initialize and set up the carousel RecyclerView
        carouselRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        CarouselAdapter adapter = new CarouselAdapter(getContext(), data);
        carouselRecyclerView.setAdapter(adapter);

        // Define margins and spacing
        int startMargin = (int) (14 * getResources().getDisplayMetrics().density);
        int endMargin = (int) (14 * getResources().getDisplayMetrics().density);
        int itemSpacing = (int) (12 * getResources().getDisplayMetrics().density);

        carouselRecyclerView.addItemDecoration(new RecyclerViewItemDecoration(startMargin, endMargin, itemSpacing, RecyclerViewItemDecoration.Direction.HORIZONTAL));

        // Add the carousel RecyclerView to the container
        carouselContainer.addView(carouselRecyclerView);
    }

    private void setupRecyclerView(List<Location> data) {
        // Set the LayoutManager
        attractionListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Create and set the adapter
        AttractionListAdapter adapter = new AttractionListAdapter(getContext(), data);
        attractionListRecyclerView.setAdapter(adapter);

        int itemSpacing = (int) (12 * getResources().getDisplayMetrics().density);

        attractionListRecyclerView.addItemDecoration(new RecyclerViewItemDecoration(0, 0, itemSpacing, RecyclerViewItemDecoration.Direction.VERTICAL));
    }

}