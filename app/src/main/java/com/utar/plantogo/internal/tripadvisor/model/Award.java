package com.utar.plantogo.internal.tripadvisor.model;

public class Award {
    private String awardType;
    private int year;
    private Images images;
    private String[] categories;
    private  String displayName;

    public void setAwardType(String awardType) {
        this.awardType = awardType;
    }

    public void setCategories(String[] categories) {
        this.categories = categories;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Images getImages() {
        return images;
    }

    public int getYear() {
        return year;
    }

    public String getAwardType() {
        return awardType;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String[] getCategories() {
        return categories;
    }
}
