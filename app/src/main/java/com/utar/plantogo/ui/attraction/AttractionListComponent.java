package com.utar.plantogo.ui.attraction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.utar.plantogo.R;
import com.utar.plantogo.internal.tripadvisor.model.Location;
import com.utar.plantogo.ui.viewmodel.FragmentViewModel;

public class AttractionListComponent extends AttractionComponent {
    ImageView attractionShowcaseImage;
    TextView attractionName, rating, location;
    RatingBar ratingBar;

    public AttractionListComponent(@NonNull Context context, FragmentManager fragmentManager, FragmentViewModel fragmentViewModel) {
        super(context, fragmentManager, fragmentViewModel);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.component_attraction_list, this);

        attractionShowcaseImage = findViewById(R.id.iv_trip_image);
        attractionName = findViewById(R.id.tv_trip_name);
        rating = findViewById(R.id.tv_review_count);
        ratingBar = findViewById(R.id.rb_rating);
        location = findViewById(R.id.tv_trip_location);
    }

    @Override
    public void setAttraction(Location location) {
        super.setAttraction(location);

        if (location != null) {
            setAttractionName(location.getName());
            setRating(location.getDetails().getNumReviews());

            ratingBar.setRating(location.getDetails().getRating());

            String locationString = location.getAddressObj().getCity() + ", " + location.getAddressObj().getState();
            this.location.setText(locationString);

            String imageUrl = null;
            if (location.getPhotos() != null && !location.getPhotos().isEmpty()) {
                imageUrl = location.getPhotos().get(0).getImages().getFallbackImage().getUrl();
            }
            setImage(imageUrl);
        }
    }

    public void setImage(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(getContext()).load(imageUrl).centerCrop().into(attractionShowcaseImage);
        } else {
            attractionShowcaseImage.setImageResource(R.drawable.placeholder_image);
        }
    }

    public void setAttractionName(String attractionName) {
        if (attractionName != null && !attractionName.isEmpty()) {
            this.attractionName.setText(attractionName);
        } else {
            this.attractionName.setText(R.string.loading);
        }
    }

    @SuppressLint("SetTextI18n")
    public void setRating(String rating) {
        if (rating != null) {
            this.rating.setText("(" + rating + ")");
        }
    }
}
