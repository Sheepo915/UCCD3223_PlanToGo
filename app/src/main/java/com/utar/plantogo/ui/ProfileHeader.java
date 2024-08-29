package com.utar.plantogo.ui;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.utar.plantogo.R;

// Maybe change to XML
public class ProfileHeader extends ConstraintLayout {

    public ProfileHeader(@NonNull Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        // Set the layout parameters for the ConstraintLayout
        ConstraintLayout.LayoutParams constriantLayout_layoutParams = new ConstraintLayout.LayoutParams(LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(constriantLayout_layoutParams);

        // Profile Picture
        ImageView profileImageView = new ImageView(context);
        profileImageView.setId(View.generateViewId());
        profileImageView.setLayoutParams(new ConstraintLayout.LayoutParams(120, 120));
        profileImageView.setImageResource(R.drawable.ic_account_circle);
        profileImageView.setContentDescription(context.getString(R.string.profile_picture));
        this.addView(profileImageView);

        // Username
        TextView guestTextView = new TextView(context);
        guestTextView.setId(View.generateViewId());
        guestTextView.setText(R.string.profile_header_default_name);
        guestTextView.setTextSize(16);
        this.addView(guestTextView);

        // Login / Register Text Button
        TextView loginRegisterTextView = new TextView(context);
        loginRegisterTextView.setId(View.generateViewId());
        loginRegisterTextView.setText(R.string.profile_header_action);
        loginRegisterTextView.setTextSize(14);
        this.addView(loginRegisterTextView);

        // Creating Constraint Set for the Constraint Layout
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this);

        // Constraints for ImageView
        constraintSet.connect(profileImageView.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 12);
        constraintSet.connect(profileImageView.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 10);
        constraintSet.connect(profileImageView.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);

        // Constraints for username TextView
        constraintSet.connect(guestTextView.getId(), ConstraintSet.START, profileImageView.getId(), ConstraintSet.END, 8);
        constraintSet.connect(guestTextView.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);

        // Constraints for login / register TextView
        constraintSet.connect(loginRegisterTextView.getId(), ConstraintSet.START, profileImageView.getId(), ConstraintSet.END, 8);
        constraintSet.connect(loginRegisterTextView.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);

        constraintSet.applyTo(this);
    }
}
