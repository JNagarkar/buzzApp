package com.group32.cse535.buzzapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.group32.cse535.buzzapp.service.Event;

import java.util.concurrent.ExecutionException;

public class EventRecievedActivity extends AppCompatActivity {
    TextView eventTextView;
    TextView senderTextView;
    Button responseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_recieved);
        eventTextView= (TextView) findViewById(R.id.textViewEvent);
        senderTextView = (TextView)findViewById(R.id.textViewUser);
        responseButton = (Button) findViewById(R.id.button2);
        Log.d("EventRecievedActivity ","oncreate");




        Intent intent = getIntent();
        final Event event= (Event) intent.getParcelableArrayExtra("parcel")[0];
        User user = (User) intent.getParcelableArrayExtra("parcel")[1];

        final String senderID = intent.getStringExtra("senderID");
        System.out.println("GOT "+senderID);
        Log.d("EVENTRECIEVED",event.getName()+":"+event.getVenue());

        eventTextView.setText(event.getName()+":"+event.getVenue()+event.getId());
        senderTextView.setText(user.getName()+":"+user.getContactNumber());


        responseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String PREFS_NAME="ID";
                SharedPreferences prefs = getSharedPreferences(PREFS_NAME,0);
                String myID = prefs.getString("userid","-1");

                AsyncTask<String, String, String> sendYesReponse = new YesResponseTask(new YesResponseEvent(senderID,event,myID)).execute("");
                try {
                    System.out.println("From broadcast request:"+sendYesReponse.get());



                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
