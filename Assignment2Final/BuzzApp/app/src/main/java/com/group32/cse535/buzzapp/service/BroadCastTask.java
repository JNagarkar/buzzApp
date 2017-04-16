package com.group32.cse535.buzzapp.service;

import android.os.AsyncTask;

import com.group32.cse535.buzzapp.MainActivity;

import org.springframework.http.HttpEntity;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by jaydatta on 4/13/17.
 */

public class BroadCastTask extends AsyncTask<String, String, String> {

    //"http://192.168.1.10:8080"
//    private static final String BASE_URL = "http://192.168.0.107:8080";
    private static final String BASE_URL = "http://192.168.1.10:8080";


    MainActivity.BroadCastEvent broadCastEvent=null;
    public BroadCastTask(MainActivity.BroadCastEvent broadCastEvent){
        this.broadCastEvent=broadCastEvent;
    }

    @Override
    protected String doInBackground(String... params) {
        String urlStr = BASE_URL+"/crud/users/broadcast"; // URL to call
        System.out.println(urlStr);

        String result=null;
        try
        {
            RestTemplate restTemplate = new RestTemplate();
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();

            headers.set("Content-type","application-json");
            HttpEntity<String> entity = new HttpEntity<String>("parameters",headers);
            restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
            result = restTemplate.postForObject(urlStr,broadCastEvent,String.class);

            System.out.println("BroadCasted "+result);
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
