package com.utar.plantogo.internal.tripadvisor.model;

public class TripType {
    private String name;
    private String localizedName;
    private String value;

    public String getLocalizedName() {
        return localizedName;
    }

    public String getName() {
        return name;
    }

    public void setLocalizedName(String localizedName) {
        this.localizedName = localizedName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}