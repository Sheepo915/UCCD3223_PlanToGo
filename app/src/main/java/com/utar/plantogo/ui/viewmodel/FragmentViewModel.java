package com.utar.plantogo.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.utar.plantogo.internal.tripadvisor.model.Location;

import java.util.List;

/**
 *
 */
public class FragmentViewModel extends ViewModel {
    private List<Location> preloadData = null;
    private final MutableLiveData<Location> selectedLocation = new MutableLiveData<>();

    public void setSelectedLocation(Location location) {
        selectedLocation.setValue(location);
    }

    public LiveData<Location> getSelectedLocation() {
        return selectedLocation;
    }

    public List<Location> getPreloadData() {
        return preloadData;
    }

    public void setPreloadData(List<Location> preloadData) {
        this.preloadData = preloadData;
    }
}
