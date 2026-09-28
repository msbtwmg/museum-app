package com.example.a2_museum;

import java.io.Serializable;

public class Artwork implements Serializable {
    private String title, artist, year, url, medium, dimensions, type, place, description ;
    private boolean isZoomable;
    public Artwork(String title, String artist, String year, String url, String medium, String dimensions, String type, String place, String description, boolean isZoomable) {
        this.title = title;
        this.artist = artist;
        this.year = year;
        this.url = url;
        this.medium = medium;
        this.dimensions = dimensions;
        this.type = type;
        this.place = place;
        this.description = description;
        this.isZoomable = isZoomable;
    }

    public String getArtist() {
        return artist;
    }

    public String getDescription() {
        return description;
    }

    public String getDimensions() {
        return dimensions;
    }

    public String getMedium() {
        return medium;
    }

    public String getPlace() {
        return place;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return year;
    }

    public String getUrl() {
        return url;
    }

    public boolean isZoomable() {
        return isZoomable;
    }


}
