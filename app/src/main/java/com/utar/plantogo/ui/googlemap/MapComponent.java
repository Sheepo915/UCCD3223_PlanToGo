package com.utar.plantogo.ui.googlemap;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.utar.plantogo.R;

public class MapComponent extends MapView implements OnMapReadyCallback {
    private final LatLng latLong;

    public MapComponent(@NonNull Context context, float latitude, float longitude) {
        super(context);
        this.latLong = new LatLng(latitude, longitude);

        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.component_map_view, this, true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        if (latLong != null) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLong, 10));
        }
    }

}
