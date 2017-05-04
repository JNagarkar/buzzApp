package com.group32.cse535.buzzapp.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.group32.cse535.buzzapp.utils.BroadCastEvent;
import com.group32.cse535.buzzapp.receiver.BroadCastMessageResponse;
import com.group32.cse535.buzzapp.adapters.ClickListener;
import com.group32.cse535.buzzapp.adapters.DividerItemDecoration;
import com.group32.cse535.buzzapp.utils.EventFetcher;
import com.group32.cse535.buzzapp.adapters.EventRecyclerTouchListener;
import com.group32.cse535.buzzapp.adapters.EventsAdapter;
import com.group32.cse535.buzzapp.R;
import com.group32.cse535.buzzapp.service.task.BroadCastTask;
import com.group32.cse535.buzzapp.models.Event;
import com.group32.cse535.buzzapp.service.task.EventFetcherTask;
import com.group32.cse535.buzzapp.utils.EventList;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

// This class fetches nearby events for sender

public class EventsSelectorActivity extends Activity {

    private EventList eventList = null;
    private RecyclerView recyclerView;
    private EventsAdapter eventsAdapter;

    private static final String TAG = "EventsSelectorActivity";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        Intent intent = getIntent();
        String currentLatitude = intent.getStringExtra("latitude");
        String currentLongitude = intent.getStringExtra("longitude");
        String radius = intent.getStringExtra("radius");
        String myID = intent.getStringExtra("myID");


/*
        String PREFS_NAME="latitude";
        SharedPreferences prefs = this.getSharedPreferences(PREFS_NAME,0);
        currentLatitude = prefs.getString("latitude",currentLatitude);


        PREFS_NAME="longitude";
        prefs = this.getSharedPreferences(PREFS_NAME,0);
        currentLongitude = prefs.getString("longitude",currentLongitude);

        System.out.println(currentLatitude+":"+currentLongitude);
*/

        EventFetcher eventFetcher = new EventFetcher(currentLatitude + "", currentLongitude + "", radius, myID);
        AsyncTask<String, String, EventList> eventFetchTask = new EventFetcherTask(eventFetcher).execute();

        EventList eventList = null;

        //TODO: Redundant
        try {
            eventList = eventFetchTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        setContentView(R.layout.activity_events);
        if (eventList.getEventList() == null || eventList.getEventList().isEmpty()) {

            List<Event> listEvent = new ArrayList<>();
            Event event = new Event();
            event.setName("NotFound");
            event.setVenue("Try later");
            listEvent.add(event);
            if (eventList.getEventList() == null) {
                eventList.setEventList(listEvent);
            } else if (eventList.getEventList().isEmpty()) {
                eventList.getEventList().add(event);
            }
        } else {

            // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            // setSupportActionBar(toolbar);

            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            eventsAdapter = new EventsAdapter(eventList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            recyclerView.setAdapter(eventsAdapter);
            final EventList finalEventList = eventList;

            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);


            recyclerView.addOnItemTouchListener(new EventRecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
                @Override
                public void onClick(View child, int childPosition) {
                    Event event = finalEventList.getEventList().get(childPosition);
                    Toast.makeText(getApplicationContext(), event.getName() + " selected", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onLongClick(View child, int childPosition) {
                    final Event event = finalEventList.getEventList().get(childPosition);
                    Toast.makeText(getApplicationContext(), event.getName() + " long", Toast.LENGTH_SHORT).show();

                    String PREFS_NAME = "ID";
                    SharedPreferences prefs = getSharedPreferences(PREFS_NAME, 0);
                    final String id1 = prefs.getString("userid", "-1");

                    LayoutInflater li = LayoutInflater.from(getApplicationContext());
                    View promptsView = li.inflate(R.layout.prompts, null);

                    alertDialogBuilder.setView(promptsView);

                    final EditText result = (EditText) findViewById(R.id.editTextResult);
                    result.setVisibility(View.INVISIBLE);
                    final EditText userInput = (EditText) promptsView
                            .findViewById(R.id.editTextDialogUserInput);


                    // set dialog message
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // get user input and set it to result
                                            // edit text
                                            result.setText(userInput.getText());
                                            doAfterAlert(id1, event, null, result.getText().toString());
                                        }
                                    })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    if (alertDialog == null) {
                        Log.v(TAG, "nulll");

                    } else {
                        alertDialog.show();
                    }

                }

                public void doAfterAlert(final String id, final Event event1, Date date, String text) {
                    BroadCastEvent broadCastEvent = new BroadCastEvent(id, event1, null);
                    broadCastEvent.setPersonalMessage(/*"Hey, I am a fan of "+broadCastEvent.getEvent().getName()*/text);
                    AsyncTask<String, String, BroadCastMessageResponse> postRequest = new BroadCastTask(broadCastEvent).execute("");
                    try {
                        Log.v(TAG, "From broadcast request:" + postRequest.get());
                        while (postRequest.get() == null) {

                        }
                        final BroadCastMessageResponse response = postRequest.get();
                        Log.v(TAG, "waiting for:" + response.getExpectedTime() + " minutes");

                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                Log.v(TAG, "End of time, now polling");
                                Intent displayRespondedUserIntent = new Intent(EventsSelectorActivity.this, DisplayRespondedUsersActivity.class);
                                displayRespondedUserIntent.putExtra("event", event1.getId() + "");
                                displayRespondedUserIntent.putExtra("eventName", event1.getName() + "");

                                Log.v(TAG, response.getBroadCastEventCurrentTime() + " this is broadcast time");

                                displayRespondedUserIntent.putExtra("broadCastTime", response.getBroadCastEventCurrentTime() + "");
                                displayRespondedUserIntent.putExtra("secondBroadCastID", id);
                                displayRespondedUserIntent.putExtra("secondBroadCastEventName", event1.getName());
                                displayRespondedUserIntent.putExtra("secondBroadCastEventID", event1.getId());
                                displayRespondedUserIntent.putExtra("secondBroadCastEventURL", event1.getEventURL());
                                displayRespondedUserIntent.putExtra("secondBroadCastEventImageURL", event1.getImageURL());
                                displayRespondedUserIntent.putExtra("secondBroadCastEventStartDate", event1.getStartDate());
                                displayRespondedUserIntent.putExtra("secondBroadCastEventStartTime", event1.getStartTime());
                                displayRespondedUserIntent.putExtra("secondBroadCastEventVenue", event1.getVenue());

                                startActivity(displayRespondedUserIntent);
                            }
                        }, Integer.valueOf(response.getExpectedTime()) * 1000 * 10);
                        //TODO: You can change the time for which you want the device to wait. Here it is waiting less than a minute for brevity.
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }));

        }
    }

    private void notifyChange(){
        eventsAdapter.notifyDataSetChanged();
    }
}
