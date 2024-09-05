package com.utar.plantogo.ui.planner;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.utar.plantogo.R;

public class PlannerAddComponent extends ConstraintLayout {
    public PlannerAddComponent(@NonNull Context context) {
        super(context);

        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.component_plan_adding, this);
    }
}

