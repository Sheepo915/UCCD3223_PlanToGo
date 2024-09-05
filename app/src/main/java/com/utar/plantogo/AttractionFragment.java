package com.utar.plantogo;

import android.content.Intent;
import android.graphics.Typeface;
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
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.utar.plantogo.internal.db.AppDatabase;
import com.utar.plantogo.internal.tripadvisor.model.Location;
import com.utar.plantogo.internal.tripadvisor.model.Photo;
import com.utar.plantogo.internal.tripadvisor.model.Review;
import com.utar.plantogo.ui.RecyclerViewItemDecoration;
import com.utar.plantogo.ui.carousel.CarouselPhotoAdapter;
import com.utar.plantogo.ui.googlemap.MapComponent;
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
        new Thread(() -> {
            int tripCount = db.plannedTripsDao().getTripsCount();
            ScrollView scrollView = tripsDialog.findViewById(R.id.sv_planner_list_modal_container);

            // Run on the main thread to update UI
            requireActivity().runOnUiThread(() -> {
                if (tripCount == 0) {
                    View noTripsLayout = LayoutInflater.from(requireContext()).inflate(R.layout.component_bottom_sheet_no_planned_trips, null);

                    if (scrollView != null) {
                        scrollView.addView(noTripsLayout);
                    }
                } else {
                    List<String> plannedTrips = db.plannedTripsDao().getAllTripsName();

                    for (String name : plannedTrips) {
                        TextView textView = new TextView(requireContext());
                        textView.setText(name);

                        if (scrollView != null) {
                            scrollView.addView(textView);
                        }
                    }
                }
            });
        }).start();


        tripsDialog.show();
    }

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

        // Instantiate static map
        contentContainer.addView(createOverviewStaticMapInfoItem(location.getDetails().getLatitude(), location.getDetails().getLongitude()));
    }

    private void instantiateReviewContent(LinearLayout contentContainer) {
        List<Review> reviews = location.getReviews();

        if (!reviews.isEmpty()) {
            for (Review review : reviews) {
                ConstraintLayout reviewCard = createReviewCard(review.getUser().getUsername(), review.getRating(), review.getText());
                contentContainer.addView(reviewCard);
            }
        }
    }

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

    protected ConstraintLayout createOverviewStaticMapInfoItem(String latitude, String longitude) {
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ConstraintLayout constraintLayout = new ConstraintLayout(requireContext());
        constraintLayout.setLayoutParams(layoutParams);
        constraintLayout.setPadding(6, 6, 6, 6);

        TextView tvCardTitle = new TextView(requireContext());
        tvCardTitle.setId(ConstraintLayout.generateViewId());
        tvCardTitle.setTypeface(null, Typeface.BOLD);
        tvCardTitle.setText(R.string.location);
        constraintLayout.addView(tvCardTitle);

        MapComponent mapComponent = new MapComponent(requireContext(), Float.parseFloat(latitude), Float.parseFloat(longitude));
        mapComponent.setId(MapComponent.generateViewId());
        constraintLayout.addView(mapComponent);

        // Set LayoutParams
        ConstraintLayout.LayoutParams titleParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        tvCardTitle.setLayoutParams(titleParams);

        ConstraintLayout.LayoutParams contentParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, 0);
        contentParams.topToBottom = tvCardTitle.getId();
        contentParams.topMargin = 6;
        mapComponent.setLayoutParams(contentParams);

        // Apply constraints
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);

        constraintSet.connect(tvCardTitle.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        constraintSet.connect(tvCardTitle.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(tvCardTitle.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);

        constraintSet.connect(mapComponent.getId(), ConstraintSet.TOP, tvCardTitle.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(mapComponent.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(mapComponent.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        constraintSet.connect(mapComponent.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
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