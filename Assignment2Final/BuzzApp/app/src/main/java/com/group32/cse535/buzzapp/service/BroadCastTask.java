package com.group32.cse535.buzzapp.service;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.group32.cse535.buzzapp.BroadCastEvent;
import com.group32.cse535.buzzapp.BroadCastMessageResponse;
import com.group32.cse535.buzzapp.EventRecievedActivity;
import com.group32.cse535.buzzapp.MainActivity;

import org.springframework.http.HttpEntity;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by jaydatta on 4/13/17.
 */

public class BroadCastTask extends AsyncTask<String, String, BroadCastMessageResponse> {

    //"http://192.168.1.10:8080"
   // private static final String BASE_URL = "http://192.168.0.110:8080";
     private static final String BASE_URL = "http://192.168.1.10:8080";

    BroadCastEvent broadCastEvent=null;
    public BroadCastTask(BroadCastEvent broadCastEvent){
        this.broadCastEvent=broadCastEvent;
    }

    @Override
    protected BroadCastMessageResponse doInBackground(String... params) {
        String urlStr = BASE_URL+"/crud/users/broadcast"; // URL to call
        System.out.println(urlStr);

        BroadCastMessageResponse response=null;
        try
        {
            RestTemplate restTemplate = new RestTemplate();
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();

            headers.set("Content-type","application-json");
            HttpEntity<String> entity = new HttpEntity<String>("parameters",headers);
            restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
            response = restTemplate.postForObject(urlStr,broadCastEvent,BroadCastMessageResponse.class);

            System.out.println("Response for Broadcast:"+response);
        }
        catch(Exception e){
            Log.e("BroadCast-Task","Error:"+e.getMessage());
            return null;
        }
        return response;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(final BroadCastMessageResponse s) {

    }
}
