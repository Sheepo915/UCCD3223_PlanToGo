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

public class AttractionCarouselComponent extends AttractionComponent {
    private ImageView attractionShowcaseImage;
    private TextView attractionName;
    private RatingBar rating;

    public AttractionCarouselComponent(@NonNull Context context, FragmentManager fragmentManager, FragmentViewModel fragmentViewModel) {
        super(context, fragmentManager, fragmentViewModel);
        init(context);
    }

    private void init(@NonNull Context context) {
        // Inflate the XML layout
        inflate(context, R.layout.component_attraction_carousel_list, this);

        // Find views
        attractionShowcaseImage = findViewById(R.id.iv_attraction_img);
        attractionName = findViewById(R.id.tv_attraction_name);
        rating = findViewById(R.id.rb_rating);
    }

    @Override
    public void setAttraction(Location location) {
        super.setAttraction(location);

        if (location != null) {
            setAttractionName(location.getName());
            rating.setRating(location.getDetails().getRating());

            String imageUrl = null;
            if (location.getPhotos() != null && !location.getPhotos().isEmpty()) {
                imageUrl = location.getPhotos().get(0).getImages().getMedium().getUrl();
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
}
