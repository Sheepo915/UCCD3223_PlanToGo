package com.utar.plantogo.ui.googlemap;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.MapView;

public class MapComponent extends MapView {
    public MapComponent(@NonNull Context context) {
        super(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}
