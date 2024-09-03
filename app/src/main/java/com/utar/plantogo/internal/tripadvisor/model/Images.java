package com.utar.plantogo.internal.tripadvisor.model;

public class Images {
    private Image thumbnail;
    private Image small;
    private Image medium;
    private Image large;
    private Image original;


    public Image getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Image thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Image getSmall() {
        return small;
    }

    public void setSmall(Image small) {
        this.small = small;
    }

    public Image getMedium() {
        return medium;
    }

    public void setMedium(Image medium) {
        this.medium = medium;
    }

    public Image getLarge() {
        return large;
    }

    public void setLarge(Image large) {
        this.large = large;
    }

    public Image getOriginal() {
        return original;
    }

    public void setOriginal(Image original) {
        this.original = original;
    }

    /**
     * Get the first available image from the specified sizes in order of preference.
     *
     * @return The URL of the first available image, or null if none are available.
     */
    public Image getFallbackImage() {
        if (original != null) {
            return original;
        } else if (large != null) {
            return large;
        } else if (medium != null) {
            return medium;
        } else if (small != null) {
            return small;
        } else if (thumbnail != null) {
            return thumbnail;
        }
        return null; // Return null if no image is available
    }
}