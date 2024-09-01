package com.utar.plantogo.ui.attraction;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Map;

public class AttractionComponent extends ConstraintLayout {

    private Map<String, Object> attraction;

    public AttractionComponent(@NonNull Context context) {
        super(context);
    }

    private void setAttraction(Map<String, Object> attraction) {
        this.attraction = attraction;
    }

    protected Map<String, Object> getAttraction() {
        return attraction;
    }
}
