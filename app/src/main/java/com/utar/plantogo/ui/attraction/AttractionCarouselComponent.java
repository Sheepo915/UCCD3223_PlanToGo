package com.utar.plantogo.ui.attraction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.utar.plantogo.R;

public class AttractionCarouselComponent extends AttractionComponent {
    private ImageView attractionShowcaseImage;
    private TextView attractionName;
    private TextView rating;

    public AttractionCarouselComponent(@NonNull Context context) {
        super(context);
        init(context);
    }

    private void init(@NonNull Context context) {
        // Inflate the XML layout
        inflate(context, R.layout.component_attraction_carousel, this);

        // Find views
        attractionShowcaseImage = findViewById(R.id.attractionShowcaseImage);
        attractionName = findViewById(R.id.attractionName);
        rating = findViewById(R.id.rating);
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
    public void setRating(Double rating) {
        if (rating != null) {
            this.rating.setText(rating.toString());
        }
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }
}
