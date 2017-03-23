package com.group32.cse535.buzzapp.service;

import android.os.AsyncTask;

import com.group32.cse535.buzzapp.User;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.util.Iterator;

/**
 * Created by jaydatta on 3/18/17.
 */

public class LocationPostTask extends AsyncTask<String, String, String> {



    public LocationPostTask() {
        //set context variables if required
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected String doInBackground(String... params) {

/*        String urlStr = params[0]; // URL to call
        System.out.println(urlStr);

        String result=null;
        try
        {
            RestTemplate restTemplate = new RestTemplate();
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();

            headers.set("Content-type","application-json");
            HttpEntity<String> entity = new HttpEntity<String>("parameters",headers);
            restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
            result = restTemplate.postForObject(urlStr,user,String.class);

        }
        catch(Exception e){
            return new String("Exception: " + e.getMessage());
        }

        return result;*/

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
