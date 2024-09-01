package com.utar.plantogo.ui.attraction;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.utar.plantogo.internal.tripadvisor.model.Location;

import org.w3c.dom.Text;

import java.util.Map;

public class AttractionWithDescriptionComponent extends AttractionComponent {
    public AttractionWithDescriptionComponent(@NonNull Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        Location attraction = super.getAttraction();

        // Initializing layout parameter for the Constraint Layout
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(layoutParams);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this);


        // Image View for showcasing attraction
        ImageView attractionShowcaseImage = new ImageView(context);
        attractionShowcaseImage.setId(ImageView.generateViewId());

        // Text View for attraction name
        TextView attractionName = new TextView(context);
        attractionName.setId(TextView.generateViewId());

        // Text View for location of the attraction
        TextView locationName = new TextView(context);
        locationName.setId(TextView.generateViewId());

        // Review stars


        // Review count
        TextView reviewCounts = new TextView(context);

        // Constraint for attraction showcase image
        constraintSet.connect(attractionShowcaseImage.getId(), ConstraintSet.START,
                ConstraintSet.PARENT_ID, ConstraintSet.START, 8);

        // Constraint for attraction name
        constraintSet.connect(attractionName.getId(), ConstraintSet.START, attractionShowcaseImage.getId(), ConstraintSet.END);
        constraintSet.connect(attractionName.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(attractionName.getId(), ConstraintSet.BOTTOM, locationName.getId(), ConstraintSet.TOP);

        // Constraint for location name
        constraintSet.connect(locationName.getId(), ConstraintSet.START, attractionShowcaseImage.getId(), ConstraintSet.END);
        constraintSet.connect(locationName.getId(), ConstraintSet.TOP, attractionName.getId(), ConstraintSet.BOTTOM);
//        constraintSet.connect(attractionName.getId(), ConstraintSet.BOTTOM, locationName.getId(),
//                ConstraintSet.START);
    }
}
