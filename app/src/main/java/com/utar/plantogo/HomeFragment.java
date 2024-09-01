package com.utar.plantogo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.utar.plantogo.example.nearbysearch.NearbySearchExample;
import com.utar.plantogo.internal.tripadvisor.model.Location;
import com.utar.plantogo.internal.tripadvisor.model.NearbyLocation;
import com.utar.plantogo.ui.carousel.CarouselAdapter;

import java.util.List;
import java.util.concurrent.Future;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private FrameLayout carouselContainer;
    private RecyclerView carouselRecyclerView;

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

        // Create Skeleton UI for carousel
//        View skeletonView = inflater.inflate(R.layout.carousel_skeleton, container, false);
//        carouselContainer.addView(skeletonView);

        // Create an instance of NearbySearchExample
        NearbySearchExample example = new NearbySearchExample(getContext());
        Future<List<Location>> futureNearbyLocations = example.loadExampleResponse();

        // Update the TextView once the future is done
        example.executorService.submit(() -> {
            try {
                // Get the response map, this blocks until the future is done
                List<Location> nearbyLocations = futureNearbyLocations.get();

                requireActivity().runOnUiThread(() -> {
//                    carouselContainer.removeView(skeletonView);

                    setupCarousel(nearbyLocations);
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

        // Add the carousel RecyclerView to the container
        carouselContainer.addView(carouselRecyclerView);
    }
}