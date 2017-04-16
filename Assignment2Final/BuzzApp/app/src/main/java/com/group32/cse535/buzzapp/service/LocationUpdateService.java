package com.group32.cse535.buzzapp.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.group32.cse535.buzzapp.HeartBeatTask;

import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 * Created by jaydatta on 3/18/17.
 */

public class LocationUpdateService extends Service {

    private static final String TAG = "LocationUpdateService:";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 50000;
    private static final float LOCATION_DISTANCE = 0;


    public class HeartBeat{
        public String id;
        public Double latitude;
        public Double longitude;

        public HeartBeat(Context context,Double latitude,Double longitude){

            String PREFS_NAME="ID";
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME,0);
            this.id = prefs.getString("userid","-1");
            this.latitude=latitude;
            this.longitude=longitude;
        }
    }

    private class LocationListener implements android.location.LocationListener
    {
        Location mLastLocation;

        public LocationListener(String provider)
        {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location)
        {
            Log.e(TAG, "onLocationChanged: " + location+"   time: "+new Date());
            mLastLocation.set(location);

            AsyncTask<String, String, String> postRequest = new HeartBeatTask(new HeartBeat(getApplicationContext(),location.getLatitude(),location.getLongitude())).execute("");
            try {
                System.out.println("Recieved:"+postRequest.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }


        }

        @Override
        public void onProviderDisabled(String provider)
        {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider)
        {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[] {
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };



    //"http://192.168.1.10:8080"
//    private static final String BASE_URL = "http://192.168.0.107:8080";
    private static final String BASE_URL = "http://192.168.1.10:8080";
    public static int BUZZ_FREQUENCY=5;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Query the database and show alarm if it applies

        // Here you can return one of some different constants.
        // This one in particular means that if for some reason
        // this service is killed, we don't want to start it
        // again automatically

        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);

//        stopSelf();
        return START_NOT_STICKY;
    }

    //service
    @Override
    public void onCreate(){

        Log.e(TAG, "onCreate");
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }

    }

    // service
    @Override
    public void onDestroy(){
        AlarmManager alarm = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarm.set(
            alarm.RTC_WAKEUP,
                System.currentTimeMillis()+(1000*60*BUZZ_FREQUENCY),
                PendingIntent.getService(this,0,new Intent(this,LocationUpdateService.class),0)
        );
    }


    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }


}
