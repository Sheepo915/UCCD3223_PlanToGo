package com.utar.plantogo.internal.tripadvisor.model;

public class Subcategory {
    private String name;
    private String localizedName;

    public String getLocalizedName() {
        return localizedName;
    }

    public void setLocalizedName(String localizedName) {
        this.localizedName = localizedName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}