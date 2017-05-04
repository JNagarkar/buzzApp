package com.group32.cse535.buzzapp.service.task;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.group32.cse535.buzzapp.MyAppConstants;
import com.group32.cse535.buzzapp.service.LocationUpdateService;

import org.springframework.http.HttpEntity;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by jaydatta on 4/13/17.
 */

public class HeartBeatTask extends AsyncTask<String, String, String> {

    //"http://192.168.1.10:8080"
   // private static final String BASE_URL = "http://192.168.0.110:8080";
     private static final String BASE_URL = "http://192.168.1.10:8080";

    private static final String TAG = "HeartBeat";

    LocationUpdateService.HeartBeat heartBeat=null;
    public HeartBeatTask(LocationUpdateService.HeartBeat beat){
        this.heartBeat=beat;
    }

    @Override
    protected String doInBackground(String... params) {
        String urlStr = MyAppConstants.BASE_URL+"/crud/users/update/location"; // URL to call
        Log.v(TAG,urlStr);

        String result=null;
        try
        {
            RestTemplate restTemplate = new RestTemplate();
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();

            headers.set("Content-type","application-json");
            HttpEntity<String> entity = new HttpEntity<String>("parameters",headers);
            restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
            result = restTemplate.postForObject(urlStr,heartBeat,String.class);

            Log.v(TAG,"expecting: "+result);

        }
        catch(Exception e){
            return new String("Exception: " + e.getMessage());
        }

        return result;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }
}
