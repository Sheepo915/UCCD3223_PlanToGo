package com.utar.plantogo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.utar.plantogo.example.nearbysearch.NearbySearchExample;
import com.utar.plantogo.internal.APIRequest;
import com.utar.plantogo.internal.db.AppDatabase;
import com.utar.plantogo.internal.tripadvisor.TripAdvisor;
import com.utar.plantogo.internal.tripadvisor.model.Location;
import com.utar.plantogo.ui.RecyclerViewItemDecoration;
import com.utar.plantogo.ui.attraction.AttractionListAdapter;
import com.utar.plantogo.ui.carousel.CarouselLocationAdapter;
import com.utar.plantogo.ui.viewmodel.FragmentViewModel;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private FrameLayout carouselContainer;
    private RecyclerView carouselRecyclerView, attractionListRecyclerView;
    private FragmentViewModel fragmentViewModel;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentViewModel = new ViewModelProvider(requireActivity()).get(FragmentViewModel.class);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        View searchViewLayout = inflater.inflate(R.layout.component_search, container, false);
        SearchView searchView = searchViewLayout.findViewById(R.id.sv_attraction_search);

        TextView showAllAttraction = view.findViewById(R.id.tv_show_all_attraction);
        showAllAttraction.setOnClickListener(v -> {
            navigateToSearchFragment("");
        });

        // Find the container in fragment_home where you want to add the searchViewLayout
        FrameLayout searchContainer = view.findViewById(R.id.fl_search_container);
        searchContainer.addView(searchViewLayout);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                navigateToSearchFragment(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // Attraction Carousel
        carouselContainer = view.findViewById(R.id.fl_carousel_container);
        carouselRecyclerView = new RecyclerView(requireContext());

        // Attraction Recycler View
        attractionListRecyclerView = view.findViewById(R.id.rv_attraction_list);

//        String latLong = fragmentViewModel.getLatitude() + ", " + fragmentViewModel.getLongitude();
//        new TripAdvisor().nearbyLocationSearch(latLong, null, null, null, null, null, null, new APIRequest.ResponseCallback() {
//            @Override
//            public void onSuccess(Map<String, Object> responseMap) {
//                Gson gson = new Gson();
//                String jsonString = gson.toJson(responseMap);
//
//                Type listType = new TypeToken<Location>() {
//                }.getType();
//                List<Location> locations = new Gson().fromJson(jsonString, listType);
//
//                fragmentViewModel.setPreloadData(locations);
//                new Thread(() -> {
//                    AppDatabase db = AppDatabase.getInstance(requireContext());
//
//                    for (Location location : locations) {
//                        db.locationDao().insertLocation(new com.utar.plantogo.internal.db.model.Location(location));
//                    }
//
//                    requireActivity().runOnUiThread(() -> {
//                        setupCarousel(locations);
//                        setupAttractionList(locations);
//                    });
//                }).start();
//            }
//
//            @Override
//            public void onError(Exception e) {
//                Log.d("DEBUG", e.toString());
//                Toast.makeText(requireContext(), "Unable to load nearby data", Toast.LENGTH_SHORT).show();
//            }
//        });

        // Create an instance of NearbySearchExample
        NearbySearchExample example = new NearbySearchExample(getContext());
        Future<List<Location>> futureNearbyLocations = example.loadExampleResponse();

        // Update the TextView once the future is done
        example.executorService.submit(() -> {
            try {
                List<Location> nearbyLocations = futureNearbyLocations.get();

                fragmentViewModel.setPreloadData(nearbyLocations);

                requireActivity().runOnUiThread(() -> {
                    setupCarousel(nearbyLocations);
                    setupAttractionList(nearbyLocations);
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
        CarouselLocationAdapter adapter = new CarouselLocationAdapter(getContext(), data, getParentFragmentManager(), fragmentViewModel);
        carouselRecyclerView.setAdapter(adapter);

        // Define margins and spacing
        int startMargin = (int) (14 * getResources().getDisplayMetrics().density);
        int endMargin = (int) (14 * getResources().getDisplayMetrics().density);
        int itemSpacing = (int) (12 * getResources().getDisplayMetrics().density);

        carouselRecyclerView.addItemDecoration(new RecyclerViewItemDecoration(startMargin, endMargin, itemSpacing, RecyclerViewItemDecoration.Direction.HORIZONTAL));

        // Ensure carouselRecyclerView does not have a parent before adding it
        if (carouselRecyclerView.getParent() != null) {
            ((ViewGroup) carouselRecyclerView.getParent()).removeView(carouselRecyclerView);
        }

        // Add the carousel RecyclerView to the container
        carouselContainer.addView(carouselRecyclerView);
    }

    private void setupAttractionList(List<Location> data) {
        // Set the LayoutManager
        attractionListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Create and set the adapter
        AttractionListAdapter adapter = new AttractionListAdapter(getContext(), data, getParentFragmentManager(), fragmentViewModel);
        attractionListRecyclerView.setAdapter(adapter);

        int itemSpacing = (int) (12 * getResources().getDisplayMetrics().density);

        attractionListRecyclerView.addItemDecoration(new RecyclerViewItemDecoration(0, 0, itemSpacing, RecyclerViewItemDecoration.Direction.VERTICAL));
    }

    private void navigateToSearchFragment(String searchQuery) {
        // Navigate to the SearchFragment
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.root_fragment_container, SearchFragment.newInstance(searchQuery));
        transaction.addToBackStack(null);
        // Commit the transaction
        transaction.commit();
    }
}