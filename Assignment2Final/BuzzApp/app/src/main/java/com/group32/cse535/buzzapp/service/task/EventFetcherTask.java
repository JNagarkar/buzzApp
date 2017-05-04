package com.group32.cse535.buzzapp.service.task;

import android.os.AsyncTask;
import android.util.Log;

import com.group32.cse535.buzzapp.utils.EventList;
import com.group32.cse535.buzzapp.utils.EventFetcher;
import com.group32.cse535.buzzapp.MyAppConstants;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.util.Iterator;

/**
 * Created by jaydatta on 4/15/17.
 */

public class EventFetcherTask extends AsyncTask<String, String, EventList> {

    //"http://192.168.1.10:8080"
//    private static final String BASE_URL = "http://192.168.0.110:8080";
     private static final String BASE_URL = "http://192.168.1.10:8080";

    private static final String TAG = "EventFetcherTask:";
    
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
    protected EventList doInBackground(String... params) {

        String urlStr = MyAppConstants.BASE_URL+"/crud/events/fetch"; // URL to call
        Log.v(TAG,urlStr);
        EventList eventList=null;
        try
        {
            RestTemplate restTemplate = new RestTemplate();
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();

            headers.set("Content-type","application-json");
            HttpEntity<String> entity = new HttpEntity<String>("parameters",headers);
            restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
            eventList = restTemplate.postForObject(urlStr,eventFetcher,EventList.class);

        }
        catch(Exception e){

            e.printStackTrace();
//            return new String("Exception: " + e.getMessage());
        }
        return eventList;
    }

    @Override
    protected void onPostExecute(EventList result) {
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
