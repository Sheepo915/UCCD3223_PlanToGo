package com.utar.plantogo.ui.attraction;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.bumptech.glide.Glide;
import com.utar.plantogo.R;
import com.utar.plantogo.internal.tripadvisor.model.Image;

import org.w3c.dom.Text;

public class AttractionCarouselComponent extends AttractionComponent {
    private ImageView attractionShowcaseImage;
    private TextView attractionName;

    public AttractionCarouselComponent(@NonNull Context context) {
        super(context);
        init(context);
    }

    private void init(@NonNull Context context) {
        // Layout parameters for the parent ConstraintLayout
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(layoutParams);

        // Attraction carousel showcase image
        ImageView imageView = new ImageView(context);

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
