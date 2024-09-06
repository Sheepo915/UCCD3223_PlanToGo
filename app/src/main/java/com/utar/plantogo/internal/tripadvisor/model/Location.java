package com.utar.plantogo.internal.tripadvisor.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Location {
    @SerializedName("location_id")
    private String locationId;
    private String name;
    private String distance;
    private String bearing;
    @SerializedName("address_obj")
    private Address addressObj;
    @SerializedName("details")
    private LocationDetails details;
    @SerializedName("photos")
    private List<Photo> photos;
    @SerializedName("reviews")
    private List<Review> reviews;

    public String getName() {
        return name;
    }

    public Location(String locationId, String name, String distance, String bearing, Address addressObj, LocationDetails details, List<Photo> photos, List<Review> reviews) {
        this.locationId = locationId;
        this.name = name;
        this.distance = distance;
        this.bearing = bearing;
        this.addressObj = addressObj;
        this.details = details;
        this.photos = photos;
        this.reviews = reviews;
    }

    public Address getAddressObj() {
        return addressObj;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public LocationDetails getDetails() {
        return details;
    }

    public String getBearing() {
        return bearing;
    }

    public String getDistance() {
        return distance;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddressObj(Address addressObj) {
        this.addressObj = addressObj;
    }

    public void setBearing(String bearing) {
        this.bearing = bearing;
    }

    public void setDetails(LocationDetails details) {
        this.details = details;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
