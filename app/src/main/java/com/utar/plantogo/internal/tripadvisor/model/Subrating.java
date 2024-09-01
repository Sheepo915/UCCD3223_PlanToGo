package com.utar.plantogo.internal.tripadvisor.model;

import com.google.gson.annotations.SerializedName;

public class Subrating {
    private String name;
    private String ratingImageUrl;
    private Double value;
    private String localizedName;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRatingImageUrl() {
        return ratingImageUrl;
    }

    public void setRatingImageUrl(String ratingImageUrl) {
        this.ratingImageUrl = ratingImageUrl;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getLocalizedName() {
        return localizedName;
    }

    public void setLocalizedName(String localizedName) {
        this.localizedName = localizedName;
    }
}

