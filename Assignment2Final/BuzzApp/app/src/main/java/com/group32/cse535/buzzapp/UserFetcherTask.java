package com.group32.cse535.buzzapp;

import android.os.AsyncTask;

import com.group32.cse535.buzzapp.service.Event;
import com.group32.cse535.buzzapp.service.EventList;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.util.Iterator;

/**
 * Created by jaydatta on 4/17/17.
 */
public class UserFetcherTask extends AsyncTask<String, String, UserList> {

    //"http://192.168.1.10:8080"
    private static final String BASE_URL = "http://192.168.0.110:8080";
  //   private static final String BASE_URL = "http://192.168.1.10:8080";

    UserFetcher userFetcher=null;
    public UserFetcherTask(UserFetcher userFetcher) {
        this.userFetcher=userFetcher;
    }

    @Override
    protected UserList doInBackground(String... params) {

        String urlStr = BASE_URL+"/crud/users/checkStatus/"+userFetcher.getSenderID()+"/"+userFetcher.getEventID(); // URL to call
        System.out.println(urlStr);
        UserList userList = null;

        try
        {
            RestTemplate restTemplate = new RestTemplate();
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();

            headers.set("Content-type","application-json");
            HttpEntity<String> entity = new HttpEntity<String>("parameters",headers);
            restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
            userList = restTemplate.postForObject(urlStr,userFetcher,UserList.class);

            if(userList==null){
                System.out.println("userList is null");
            }
            else if(userList.getUserList()==null){
                System.out.println("userlist not null but no users");
            }
            else{
                for(User obj : userList.getUserList()) {
                    //Event  event = (Event) obj;
                    System.out.println(obj.getName());
                }
            }


        }
        catch(Exception e){

            e.printStackTrace();
//            return new String("Exception: " + e.getMessage());
        }
        return userList;
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
