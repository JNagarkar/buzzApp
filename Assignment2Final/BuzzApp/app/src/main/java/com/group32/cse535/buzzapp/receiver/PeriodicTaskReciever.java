package com.group32.cse535.buzzapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.group32.cse535.buzzapp.activities.MainActivity;

/**
 * Created by jaydatta on 3/18/17.
 */

public class PeriodicTaskReciever extends BroadcastReceiver {

    private static final String TAG = "PeriodicTaskReceiver";
    private static final String INTENT_ACTION = "com.group32.cse535.buzzapp.PERIODIC_TASK_HEART_BEAT";

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction()==null || intent.getAction()==""){
            MainActivity mainActivity = (MainActivity)context.getApplicationContext();

            doPeriodicTask(context,mainActivity);

        }
    }

    private void doPeriodicTask(Context context, MainActivity mainActivity) {

        // Periodic task

    }




}
