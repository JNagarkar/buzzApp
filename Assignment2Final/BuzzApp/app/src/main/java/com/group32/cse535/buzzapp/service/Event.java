package com.group32.cse535.buzzapp.service;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jaydatta on 4/13/17.
 */

public class Event implements Parcelable{
    String id;
    String name;
    String imageURL;
    String eventURL;

    String startDate;
    String startTime;

    String venue;

    public String category;
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Event(){

    }

    public Event(String id) {
        this.id = id;
    }

    public String getEventURL() {
        return eventURL;
    }

    public void setEventURL(String eventURL) {
        this.eventURL = eventURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return new StringBuilder().append(name).toString();
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Event other = (Event) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(eventURL);
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(imageURL);
        dest.writeString(startDate);
        dest.writeString(startTime);
        dest.writeString(venue);
    }

    public static final Parcelable.Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel source) {
            return new Event(source);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[0];
        }

    };

    public Event(Parcel in)
    {
        eventURL=in.readString();
        id = in.readString();
        name=in.readString();
        imageURL=in.readString();
        startDate=in.readString();
        startTime=in.readString();
        venue=in.readString();
    }
}
