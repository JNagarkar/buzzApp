package com.group32.cse535.buzzapp.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.provider.AlarmClock;
import android.support.annotation.Nullable;

import com.google.android.gms.location.LocationListener;

/**
 * Created by jaydatta on 3/18/17.
 */

public class LocationUpdateService extends Service{

    private static final String BASE_URL = "http://192.168.1.10:8080";
    public static int BUZZ_FREQUENCY=1;

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

        stopSelf();
        return START_NOT_STICKY;
    }


    @Override
    public void onCreate(){

        System.out.println("Starting service");
    }

    @Override
    public void onDestroy(){
        AlarmManager alarm = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarm.set(
            alarm.RTC_WAKEUP,
                System.currentTimeMillis()+(1000*60*BUZZ_FREQUENCY),
                PendingIntent.getService(this,0,new Intent(this,LocationUpdateService.class),0)
        );
    }
}
