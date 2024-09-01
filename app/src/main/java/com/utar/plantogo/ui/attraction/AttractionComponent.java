package com.utar.plantogo.ui.attraction;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.utar.plantogo.internal.tripadvisor.model.Location;

import java.util.Map;

public class AttractionComponent extends ConstraintLayout {

    private Location location;

    public AttractionComponent(@NonNull Context context) {
        super(context);
    }

    private void setAttraction(Location location) {
        this.location = location;
    }

    protected Location getAttraction() {
        return location;
    }
}
