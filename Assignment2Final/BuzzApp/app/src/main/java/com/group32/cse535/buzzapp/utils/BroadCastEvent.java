package com.group32.cse535.buzzapp.utils;

import com.group32.cse535.buzzapp.models.Event;

import java.util.Date;

/**
 * Created by jaydatta on 4/17/17.
 */

public class BroadCastEvent {

    public String userID;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Event event;

    public Date currentTime;

    public Date getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Date currentTime) {
        this.currentTime = currentTime;
    }

    public String personalMessage;

    public String getPersonalMessage() {
        return personalMessage;
    }

    public void setPersonalMessage(String personalMessage) {
        this.personalMessage = personalMessage;
    }

    public BroadCastEvent(String userID, Event event, Date currentTime) {
        this.userID = userID;
        this.event = event;
        this.currentTime = currentTime;
    }
}