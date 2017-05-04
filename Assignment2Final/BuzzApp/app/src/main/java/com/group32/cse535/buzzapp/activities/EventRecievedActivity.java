package com.group32.cse535.buzzapp.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.group32.cse535.buzzapp.R;
import com.group32.cse535.buzzapp.models.User;
import com.group32.cse535.buzzapp.utils.YesResponseEvent;
import com.group32.cse535.buzzapp.service.task.YesResponseTask;
import com.group32.cse535.buzzapp.models.Event;

import java.util.concurrent.ExecutionException;

public class EventRecievedActivity extends AppCompatActivity {
    TextView eventTextView;
    TextView senderTextView;
    Button responseButton;
    TextView personalMessageView;
    private static final String TAG = "EventRecievedActivity:";
    
    public static Context eventRecievedContext;

    public static Context getEventRecievedContext() {
        return eventRecievedContext;
    }

    public static void setEventRecievedContext(Context eventRecievedContext) {
        EventRecievedActivity.eventRecievedContext = eventRecievedContext;
    }


    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
      //      updateUi();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_recieved);

      //  registerReceiver(myReceiver, new IntentFilter(MyFireBaseMessagingService.INTENT_FILTER));

        Log.v(TAG,"on eventRecievedActivity");

        eventTextView= (TextView) findViewById(R.id.textViewEvent);
        senderTextView = (TextView)findViewById(R.id.textViewUser);
        responseButton = (Button) findViewById(R.id.button2);
        personalMessageView = (TextView) findViewById(R.id.textView4);

        Log.d("EventRecievedActivity ","oncreate");

        Intent intent = getIntent();
        final Event event= (Event) intent.getParcelableArrayExtra("parcel")[0];
        User user = (User) intent.getParcelableArrayExtra("parcel")[1];

        final String senderID = intent.getStringExtra("senderID");
        Log.v(TAG,"GOT "+senderID);
        Log.d("EVENTRECIEVED",event.getName()+":"+event.getVenue());

        Long broadCastTime=intent.getLongExtra("broadCastTime",0);

        Log.v(TAG,"broadcast recieved in EventRecievedActivity:"+broadCastTime);

        String message = intent.getStringExtra("personalMessage");
        eventTextView.setText(event.getName()+":"+event.getVenue()+event.getId());
        senderTextView.setText(user.getName()+":"+user.getContactNumber());
        personalMessageView.setText(message);

            final Long finalBroadCastTime = broadCastTime;
            responseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String PREFS_NAME="ID";
                    SharedPreferences prefs = getSharedPreferences(PREFS_NAME,0);
                    String myID = prefs.getString("userid","-1");

                    AsyncTask<String, String, String> sendYesReponse = new YesResponseTask(new YesResponseEvent(senderID,event,myID,Long.valueOf(finalBroadCastTime))).execute("");
                    try {
                        Log.v(TAG,"From broadcast request:"+sendYesReponse.get());

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    finally {
                        finish();
                    }
                }
            });
        }

    @Override
    public void onDestroy(){
        super.onDestroy();
//        unregisterReceiver(myReceiver);
    }

}
