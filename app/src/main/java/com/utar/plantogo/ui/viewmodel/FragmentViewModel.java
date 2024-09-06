package com.utar.plantogo.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.utar.plantogo.internal.db.AppDatabase;
import com.utar.plantogo.internal.tripadvisor.model.Location;

import java.util.List;

/**
 *
 */
public class FragmentViewModel extends ViewModel {
    private final MutableLiveData<Location> selectedLocation = new MutableLiveData<>();
    private List<Location> preloadData = null;
    private float latitude, longitude;

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public LiveData<Location> getSelectedLocation() {
        return selectedLocation;
    }

    public void setSelectedLocation(Location location) {
        selectedLocation.setValue(location);
    }

    public List<Location> getPreloadData() {
        return preloadData;
    }

    public void setPreloadData(List<Location> preloadData) {
        this.preloadData = preloadData;
    }
}
