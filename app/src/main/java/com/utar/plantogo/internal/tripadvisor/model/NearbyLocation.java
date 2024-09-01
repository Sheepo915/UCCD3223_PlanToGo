package com.utar.plantogo.internal.tripadvisor.model;

import java.util.List;

public class NearbyLocation {
    private List<Location> locations;

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }
}