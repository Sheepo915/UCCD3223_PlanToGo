package com.utar.plantogo.internal.tripadvisor.model;

import java.util.List;

public class Location {
    private String locationId;
    private String name;
    private String distance;
    private String bearing;
    private Address addressObj;
    private LocationDetails details;
    private List<Photo> photos;
    private List<Review> reviews;

    public String getName() {
        return name;
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
