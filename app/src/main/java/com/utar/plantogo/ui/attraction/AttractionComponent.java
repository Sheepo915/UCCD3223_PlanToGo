package com.utar.plantogo.ui.attraction;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;

import com.utar.plantogo.AttractionFragment;
import com.utar.plantogo.R;
import com.utar.plantogo.internal.tripadvisor.model.Location;
import com.utar.plantogo.ui.viewmodel.FragmentViewModel;

public class AttractionComponent extends ConstraintLayout {

    protected Location location;
    protected FragmentManager fragmentManager;
    protected FragmentViewModel fragmentViewModel;

    public AttractionComponent(@NonNull Context context, FragmentManager fragmentManager, FragmentViewModel fragmentViewModel) {
        super(context);
        this.fragmentManager = fragmentManager;
        this.fragmentViewModel = fragmentViewModel;
        init(context);
    }

    private void init(Context context) {
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (location != null && fragmentManager != null) {
                    fragmentViewModel.setSelectedLocation(location);

                    // Navigate to the AttractionFragment
                    moveToAttractionFragment();
                }
            }
        });
    }

    protected Location getAttraction() {
        return location;
    }

    protected void setAttraction(Location location) {
        this.location = location;
    }

    private void moveToAttractionFragment() {
        AttractionFragment attractionFragment = new AttractionFragment();

        fragmentManager.beginTransaction().replace(R.id.root_fragment_container, attractionFragment).addToBackStack(null).commit();
    }
}
