package com.utar.plantogo.internal.tripadvisor.model;

import java.util.List;
import java.util.Map;

public class LocationDetails {
    private String locationId;
    private String name;
    private String webUrl;
    private Address addressObj;
    private List<Ancestor> ancestors;
    private String latitude;
    private String longitude;
    private String timezone;
    private String email;
    private String phone;
    private String website;
    private float rating;
    private String writeReview;
    private String numReviews;
    private Map<String, Subrating> subratings;
    private String photoCount;
    private String seeAllPhotos;
    private String priceLevel;
    private Hours hours;
    private List<String> features;
    private List<Cuisine> cuisine;
    private Category category;
    private List<Subcategory> subcategory;
    private List<TripType> tripTypes;
    private List<Award> awards;

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public Address getAddressObj() {
        return addressObj;
    }

    public void setAddressObj(Address addressObj) {
        this.addressObj = addressObj;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Hours getHours() {
        return hours;
    }

    public void setHours(Hours hours) {
        this.hours = hours;
    }

    public List<Ancestor> getAncestors() {
        return ancestors;
    }

    public void setAncestors(List<Ancestor> ancestors) {
        this.ancestors = ancestors;
    }

    public List<Award> getAwards() {
        return awards;
    }

    public void setAwards(List<Award> awards) {
        this.awards = awards;
    }

    public List<Cuisine> getCuisine() {
        return cuisine;
    }

    public void setCuisine(List<Cuisine> cuisine) {
        this.cuisine = cuisine;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }

    public List<Subcategory> getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(List<Subcategory> subcategory) {
        this.subcategory = subcategory;
    }

    public List<TripType> getTripTypes() {
        return tripTypes;
    }

    public void setTripTypes(List<TripType> tripTypes) {
        this.tripTypes = tripTypes;
    }

    public Map<String, Subrating> getSubratings() {
        return subratings;
    }

    public void setSubratings(Map<String, Subrating> subratings) {
        this.subratings = subratings;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getNumReviews() {
        return numReviews;
    }

    public void setNumReviews(String numReviews) {
        this.numReviews = numReviews;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhotoCount() {
        return photoCount;
    }

    public void setPhotoCount(String photoCount) {
        this.photoCount = photoCount;
    }

    public String getPriceLevel() {
        return priceLevel;
    }

    public void setPriceLevel(String priceLevel) {
        this.priceLevel = priceLevel;
    }

    public String getSeeAllPhotos() {
        return seeAllPhotos;
    }

    public void setSeeAllPhotos(String seeAllPhotos) {
        this.seeAllPhotos = seeAllPhotos;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getWriteReview() {
        return writeReview;
    }

    public void setWriteReview(String writeReview) {
        this.writeReview = writeReview;
    }
}
