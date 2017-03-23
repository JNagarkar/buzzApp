package com.group32.cse535.buzzapp.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by jaydatta on 3/18/17.
 */

public class AutoStart extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context,LocationUpdateService.class));
    }
}
