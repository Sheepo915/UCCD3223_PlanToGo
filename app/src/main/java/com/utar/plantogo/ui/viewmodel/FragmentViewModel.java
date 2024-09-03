package com.utar.plantogo.ui.viewmodel;

import androidx.lifecycle.ViewModel;

import com.utar.plantogo.internal.tripadvisor.model.Location;

import java.util.List;

/**
 *
 */
public class FragmentViewModel extends ViewModel {
    private List<Location> preloadData = null;

    public void setPreloadData(List<Location> preloadData) {
        this.preloadData = preloadData;
    }

    public List<Location> getPreloadData() {
        return preloadData;
    }
}
