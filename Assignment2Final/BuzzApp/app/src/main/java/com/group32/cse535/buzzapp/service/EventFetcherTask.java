package com.group32.cse535.buzzapp.service;

import android.os.AsyncTask;

import com.group32.cse535.buzzapp.EventFetcher;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by jaydatta on 4/15/17.
 */

public class EventFetcherTask extends AsyncTask<String, String, String> {

    //"http://192.168.1.10:8080"
//    private static final String BASE_URL = "http://192.168.0.107:8080";
    private static final String BASE_URL = "http://192.168.1.10:8080";

    EventFetcher eventFetcher=null;
    public EventFetcherTask(EventFetcher eventFetcher) {
        //set context variables if required
        this.eventFetcher = eventFetcher;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected String doInBackground(String... params) {

        String urlStr = BASE_URL+"/crud/events/fetch"; // URL to call
        System.out.println(urlStr);

        try
        {
            RestTemplate restTemplate = new RestTemplate();
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();

            headers.set("Content-type","application-json");
            HttpEntity<String> entity = new HttpEntity<String>("parameters",headers);
            restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
            EventList eList = restTemplate.postForObject(urlStr,eventFetcher,EventList.class);

            for(Event obj : eList.getEventList()) {
                //Event  event = (Event) obj;
                System.out.println(obj.getName());
            }
        }
        catch(Exception e){

            e.printStackTrace();
            return new String("Exception: " + e.getMessage());
        }
        return "";
    }

    @Override
    protected void onPostExecute(String result) {
        //Update the UI
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

}
