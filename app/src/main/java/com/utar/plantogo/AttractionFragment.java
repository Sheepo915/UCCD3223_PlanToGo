package com.utar.plantogo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.utar.plantogo.internal.db.AppDatabase;
import com.utar.plantogo.internal.db.pojo.TripIdName;
import com.utar.plantogo.internal.tripadvisor.model.Location;
import com.utar.plantogo.internal.tripadvisor.model.Photo;
import com.utar.plantogo.internal.tripadvisor.model.Review;
import com.utar.plantogo.ui.RecyclerViewItemDecoration;
import com.utar.plantogo.ui.bottomsheet.PlannedTripBottomSheetAdapter;
import com.utar.plantogo.ui.carousel.CarouselPhotoAdapter;
import com.utar.plantogo.ui.viewmodel.FragmentViewModel;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class AttractionFragment extends Fragment {

    private static final String LOCATION = "location";

    private Location location;

    private FrameLayout carouselContainer;
    private RecyclerView carouselRecyclerView;
    private FragmentViewModel fragmentViewModel;

    public AttractionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            location = getArguments().getParcelable(LOCATION);
        }
        fragmentViewModel = new ViewModelProvider(requireActivity()).get(FragmentViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attraction, container, false);
        location = fragmentViewModel.getSelectedLocation().getValue();

        LinearLayout contentContainer = view.findViewById(R.id.ll_content_container);

        TextView attractionName = view.findViewById(R.id.tv_attraction_name);
        attractionName.setText(location.getName());

        RatingBar ratingBar = view.findViewById(R.id.rb_rating);
        ratingBar.setRating(location.getDetails().getRating());

        TextView ratingCount = view.findViewById(R.id.tv_rating_count);
        String numberOfReviews = location.getDetails().getNumReviews() == null ? "0" : location.getDetails().getNumReviews();
        ratingCount.setText(String.format(Locale.ENGLISH, "(%s)", numberOfReviews));

        TextView overview = view.findViewById(R.id.tv_overview);
        overview.setText(R.string.overview_active);

        TextView review = view.findViewById(R.id.tv_review);

        Button addToTrip = view.findViewById(R.id.btn_add_to_trip);
        addToTrip.setOnClickListener(v -> showBottomSheet());

        // Initiate photo carousel
        carouselContainer = view.findViewById(R.id.fl_carousel_container);
        carouselRecyclerView = new RecyclerView(requireContext());
        setupCarousel(location.getPhotos());

        // Default content showed on start
        instantiateOverviewContent(contentContainer);

        overview.setOnClickListener(v -> {
            overview.setText(R.string.overview_active);
            review.setText(R.string.reviews);

            contentContainer.removeAllViews();

            instantiateOverviewContent(contentContainer);
        });

        review.setOnClickListener(v -> {
            overview.setText(R.string.overview);
            review.setText(R.string.reviews_active);

            contentContainer.removeAllViews();

            instantiateReviewContent(contentContainer);
        });

        return view;
    }

    private void showBottomSheet() {
        BottomSheetDialog tripsDialog = new BottomSheetDialog(requireContext());
        tripsDialog.setContentView(R.layout.layout_trips_bottom_sheet);

        ImageButton modalCollapseButton = tripsDialog.findViewById(R.id.ib_modal_collapse);
        Objects.requireNonNull(modalCollapseButton).setOnClickListener(x -> {
            tripsDialog.dismiss();
        });

        AppDatabase db = AppDatabase.getInstance(requireContext());
        RecyclerView recyclerView = tripsDialog.findViewById(R.id.rv_planner_list_modal_container);
        Objects.requireNonNull(recyclerView).setLayoutManager(new LinearLayoutManager(requireContext()));

        // Get all the saved trips
        new Thread(() -> {
            List<TripIdName> plannedTrips = db.plannedTripsDao().getAllTripsName();

            // Run on the main thread to update UI
            requireActivity().runOnUiThread(() -> {
                PlannedTripBottomSheetAdapter adapter = new PlannedTripBottomSheetAdapter(requireContext(), plannedTrips, fragmentViewModel, tripsDialog);
                recyclerView.setAdapter(adapter);
            });
        }).start();

        tripsDialog.show();
    }

    /**
     * Instantiate the overview content for the attraction
     * Showcasing the details of the attraction
     * @param contentContainer View container that the component should inflate.
     */
    private void instantiateOverviewContent(LinearLayout contentContainer) {
        ConstraintLayout address = createOverviewInfoCard("Address", location.getAddressObj().getAddressString());
        ConstraintLayout website = null;
        try {
            String url = location.getDetails().getWebsite();

            if (url != null) {
                website = createOverviewInfoCard("Website", URLDecoder.decode(url, StandardCharsets.UTF_8.name()));
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        // Setup content
        contentContainer.addView(address);
        if (website != null) {
            contentContainer.addView(website);
        }

        View mapView = createMapView(Double.parseDouble(location.getDetails().getLatitude()), Double.parseDouble(location.getDetails().getLongitude()));
        ConstraintLayout map = createOverviewInfoCard("Location", mapView);

        contentContainer.addView(map);

    }

    /**
     * Instantiate the map view for each attraction with Google Map SDK
     * @param latitude latitude of the location
     * @param longitude longitude of the location
     * @return Google Map View as a component
     */
    private View createMapView(double latitude, double longitude) {
        MapView mapView = new MapView(requireContext());
        mapView.onCreate(null);
        mapView.onResume();
        mapView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mapView.getMapAsync(googleMap -> {
            LatLng location = new LatLng(latitude, longitude);
            googleMap.addMarker(new MarkerOptions().position(location));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
            mapView.post(mapView::requestLayout);
        });
        return mapView;
    }

    /**
     * Instantiate the review content
     * @param contentContainer Root container for the layout to be sit on
     */
    private void instantiateReviewContent(LinearLayout contentContainer) {
        List<Review> reviews = location.getReviews();

        if (!reviews.isEmpty()) {
            for (Review review : reviews) {
                ConstraintLayout reviewCard = createReviewCard(review.getUser().getUsername(), review.getRating(), review.getText());
                contentContainer.addView(reviewCard);
            }
        }
    }

    /**
     * Setup function fot the photo carousel
     * @param data List of photo of the location
     */
    private void setupCarousel(List<Photo> data) {
        // Initialize and set up the carousel RecyclerView
        carouselRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        CarouselPhotoAdapter adapter = new CarouselPhotoAdapter(getContext(), data, getParentFragmentManager(), fragmentViewModel, true);
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

    protected ConstraintLayout createOverviewInfoCard(String title, String content) {
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        ConstraintLayout constraintLayout = (ConstraintLayout) inflater.inflate(R.layout.component_attraction_info_card, null);

        TextView cardTitle = constraintLayout.findViewById(R.id.tv_card_title);
        TextView cardContent = constraintLayout.findViewById(R.id.tv_card_content);

        Pattern urlPattern = Pattern.compile("^http(s?)://.+");
        Matcher matcher = urlPattern.matcher(content);

        cardTitle.setText(title);
        if (matcher.matches()) {
            SpannableString url = new SpannableString(content);
            url.setSpan(new URLSpan(content), 0, url.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            cardContent.setText(url);
            cardContent.setOnClickListener(v -> {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(content));
                startActivity(browserIntent);
            });
        } else {
            cardContent.setText(content);
        }

        return constraintLayout;
    }

    protected ConstraintLayout createOverviewInfoCard(String title, View component) {
        ConstraintLayout constraintLayout = new ConstraintLayout(requireContext());
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        constraintLayout.setLayoutParams(layoutParams);
        constraintLayout.setPadding(6, 6, 6, 6);

        // Create and configure the title TextView
        TextView titleTextView = new TextView(requireContext());
        titleTextView.setId(View.generateViewId());
        titleTextView.setText(title);
        titleTextView.setTypeface(null, android.graphics.Typeface.BOLD);

        // Add the title TextView and component to the ConstraintLayout
        constraintLayout.addView(titleTextView);
        component.setId(View.generateViewId());
        constraintLayout.addView(component);

        // Set layout parameters for the TextView and component
        ConstraintLayout.LayoutParams titleParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        titleTextView.setLayoutParams(titleParams);

        ConstraintLayout.LayoutParams componentParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, (int) (200 * getResources().getDisplayMetrics().density));
        component.setMinimumHeight((int) (200 * getResources().getDisplayMetrics().density));
        component.setLayoutParams(componentParams);

        // Apply constraints using ConstraintSet
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);

        // Title TextView constraints
        constraintSet.connect(titleTextView.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        constraintSet.connect(titleTextView.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(titleTextView.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);

        // Component constraints
        constraintSet.connect(component.getId(), ConstraintSet.TOP, titleTextView.getId(), ConstraintSet.BOTTOM, (int) (6 * getResources().getDisplayMetrics().density));
        constraintSet.connect(component.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(component.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        constraintSet.connect(component.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);

        // Apply the constraints
        constraintSet.applyTo(constraintLayout);

        return constraintLayout;
    }

    protected ConstraintLayout createReviewCard(String username, float rating, String reviews) {
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        ConstraintLayout constraintLayout = (ConstraintLayout) inflater.inflate(R.layout.component_attraction_review_card, null);

        TextView cardUsername = constraintLayout.findViewById(R.id.tv_username);
        TextView cardReview = constraintLayout.findViewById(R.id.tv_review);
        RatingBar ratingBar = constraintLayout.findViewById(R.id.rb_rating);

        cardUsername.setText(username);
        cardReview.setText(reviews);
        ratingBar.setRating(rating);

        return constraintLayout;
    }
}