package com.group32.cse535.buzzapp;

import com.group32.cse535.buzzapp.service.Event;

/**
 * Created by jaydatta on 4/17/17.
 */

public class YesResponseEvent {

    String senderID;

    public YesResponseEvent(){

    }

    public YesResponseEvent(String senderID, Event event, String responderID) {
        this.senderID = senderID;
        this.event = event;
        this.responderID = responderID;
    }

    Event event;
    String responderID;

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
