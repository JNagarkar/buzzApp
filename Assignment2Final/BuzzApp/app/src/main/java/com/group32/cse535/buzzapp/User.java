package com.group32.cse535.buzzapp;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by jaydatta on 3/18/17.
 */


public class User implements Parcelable{

    private String name;

    private String email;

    private Double latitude;

    protected User(Parcel in) {
        name = in.readString();
        email = in.readString();
        token = in.readString();
        contactNumber = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(token);
        dest.writeString(contactNumber);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public Date getLatestUpdated() {
        return latestUpdated;
    }

    public void setLatestUpdated(Date latestUpdated) {
        this.latestUpdated = latestUpdated;
    }

    private Date latestUpdated;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private String token;

    public User(String name, String email, String contactNumber, Double latitude, Double longitude , Integer expectedTime, Integer radius, Date latestUpdated,String token) {

        System.out.println("thiii\n\n\n\n\n\n");
        this.name = name;
        this.email = email;
        this.latitude = latitude;
        this.longitude = longitude;
        this.contactNumber = contactNumber;
        this.expectedTime = expectedTime;
        this.radius = radius;
        this.latestUpdated = latestUpdated;
        this.token=token;
    }
    public User(){

    }


    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    private Double longitude;

    private String contactNumber;

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Integer getExpectedTime() {
        return expectedTime;
    }

    public void setExpectedTime(Integer expectedTime) {
        this.expectedTime = expectedTime;
    }

    public Integer getRadius() {
        return radius;
    }

    public void setRadius(Integer radius) {
        this.radius = radius;
    }

    private Integer expectedTime;

    private Integer radius;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public String toString() {
        return "User{" +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", contactNumber='" + contactNumber + '\'' +
                ", expectedTime=" + expectedTime +
                ", radius=" + radius +
                '}';
    }
}

