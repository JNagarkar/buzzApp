package com.group32.cse535.buzzapp.utils;

/**
 * Created by jaydatta on 4/15/17.
 */

public class EventFetcher {

    String latitude;
    String longitude;
    String radius;
    String userID;

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

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }


    public EventFetcher(String latitude, String longitude, String radius,String userID) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.userID=userID;
    }

}
