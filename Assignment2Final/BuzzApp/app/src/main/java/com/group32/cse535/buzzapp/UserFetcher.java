package com.group32.cse535.buzzapp;

/**
 * Created by jaydatta on 4/17/17.
 */
public class UserFetcher {

    String senderID;
    String senderToken;
    String eventID;

    //This is the time at which the broadcaster has broadcasted the event
    Long broadCastTime;

    public Long getBroadCastTime() {
        return broadCastTime;
    }

    public void setBroadCastTime(Long broadCastTime) {
        this.broadCastTime = broadCastTime;
    }



    public UserFetcher(String senderID, String senderToken, String eventID, Long broadCastTime) {
        this.senderID = senderID;
        this.senderToken = senderToken;
        this.eventID = eventID;
        this.broadCastTime = broadCastTime;
    }

    public UserFetcher(){}



    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getSenderToken() {
        return senderToken;
    }

    public void setSenderToken(String senderToken) {
        this.senderToken = senderToken;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }



}
