package com.group32.cse535.buzzapp;

import com.group32.cse535.buzzapp.service.Event;

/**
 * Created by jaydatta on 4/17/17.
 */

public class YesResponseEvent {

    String senderID;
    Event event;
    String responderID;
    //Stored the time at which event was broadcasted : absolutely required.
    Long broadCastTime;

    public Long getBroadCastTime() {
        return broadCastTime;
    }

    public void setBroadCastTime(Long broadCastTime) {
        this.broadCastTime = broadCastTime;
    }



    public YesResponseEvent(){

    }

    public YesResponseEvent(String senderID, Event event, String responderID,Long broadCastTime) {
        this.senderID = senderID;
        this.event = event;
        this.responderID = responderID;
        this.broadCastTime = broadCastTime;
    }


    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getResponderID() {
        return responderID;
    }

    public void setResponderID(String responderID) {
        this.responderID = responderID;
    }
}
