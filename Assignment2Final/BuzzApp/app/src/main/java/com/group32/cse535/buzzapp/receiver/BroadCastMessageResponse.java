package com.group32.cse535.buzzapp.receiver;

/**
 * Created by jaydatta on 4/17/17.
 */

public class BroadCastMessageResponse {
    public int expectedTime;
    public long broadCastEventCurrentTime;

    public BroadCastMessageResponse(int expectedTime, long broadCastEventCurrentTime) {
        this.expectedTime = expectedTime;
        this.broadCastEventCurrentTime = broadCastEventCurrentTime;
    }

    public BroadCastMessageResponse(){

    }

    public int getExpectedTime() {
        return expectedTime;
    }

    public void setExpectedTime(int expectedTime) {
        this.expectedTime = expectedTime;
    }

    public long getBroadCastEventCurrentTime() {
        return broadCastEventCurrentTime;
    }

    public void setBroadCastEventCurrentTime(long broadCastEventCurrentTime) {
        this.broadCastEventCurrentTime = broadCastEventCurrentTime;
    }

}
