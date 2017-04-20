package com.group32.cse535.buzzapp;

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.HttpEntity;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by jaydatta on 4/17/17.
 */

public class YesResponseTask extends AsyncTask<String, String, String> {

    private static String TAG="YesResponseTask";

    //"http://192.168.1.10:8080"
//    private static final String BASE_URL = "http://192.168.0.110:8080";
     private static final String BASE_URL = "http://192.168.1.10:8080";


    YesResponseEvent yesResponseEvent;

    public YesResponseTask(YesResponseEvent yesResponseEvent) {
        this.yesResponseEvent = yesResponseEvent;
    }

    @Override
    protected String doInBackground(String... params) {
        String urlStr = BASE_URL+"/crud/users/confirmGoing/"+yesResponseEvent.getSenderID()+"/"+yesResponseEvent.getEvent().getId(); // URL to call
        System.out.println(urlStr);

        String result=null;
        try
        {
            RestTemplate restTemplate = new RestTemplate();
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();

            headers.set("Content-type","application-json");
            HttpEntity<String> entity = new HttpEntity<String>("parameters",headers);
            restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
            result = restTemplate.postForObject(urlStr,yesResponseEvent,String.class);

            Log.d(TAG,"Sent Yes Response");
        }
        catch(Exception e){
            Log.d(TAG,"Fatal:"+e);
            return new String("Exception: " + e.getMessage());
        }
        return result;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }
}
