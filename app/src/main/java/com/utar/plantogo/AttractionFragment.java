package com.utar.plantogo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.utar.plantogo.internal.tripadvisor.model.Location;
import com.utar.plantogo.internal.tripadvisor.model.Photo;
import com.utar.plantogo.ui.RecyclerViewItemDecoration;
import com.utar.plantogo.ui.carousel.CarouselPhotoAdapter;
import com.utar.plantogo.ui.viewmodel.FragmentViewModel;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

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

        ConstraintLayout address = createOverviewInfoCard("Address", location.getAddressObj().getAddressString());
        ConstraintLayout website = null;
        try {
            website = createOverviewInfoCard("Website", URLDecoder.decode(location.getDetails().getWebsite(), StandardCharsets.UTF_8.name()));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        // Initiate photo carousel
        carouselContainer = view.findViewById(R.id.fl_carousel_container);
        carouselRecyclerView = new RecyclerView(requireContext());
        setupCarousel(location.getPhotos());

        // Setup content
        contentContainer.addView(address);
        if (website != null) {
            contentContainer.addView(website);
        }

        overview.setOnClickListener(v -> {
            overview.setText(R.string.overview_active);
            review.setText(R.string.reviews);
        });

        review.setOnClickListener(v -> {
            overview.setText(R.string.overview);
            review.setText(R.string.reviews_active);
        });

        return view;
    }

    private void setupCarousel(List<Photo> data) {
        // Initialize and set up the carousel RecyclerView
        carouselRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        CarouselPhotoAdapter adapter = new CarouselPhotoAdapter(getContext(), data, getParentFragmentManager(), fragmentViewModel);
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

        cardTitle.setText(title);
        cardContent.setText(content);

        return constraintLayout;
    }
}