package com.group32.cse535.buzzapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.group32.cse535.buzzapp.service.BroadCastTask;
import com.group32.cse535.buzzapp.service.Event;
import com.group32.cse535.buzzapp.service.EventFetcherTask;
import com.group32.cse535.buzzapp.service.EventList;

import java.util.concurrent.ExecutionException;

// This class fetches nearby events for sender

public class EventsSelectorActivity extends AppCompatActivity {

    private EventList eventList = null;
    private RecyclerView recyclerView;
    private EventsAdapter eventsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        Intent intent = getIntent();
        String currentLatitude = intent.getStringExtra("latitude");
        String currentLongitude = intent.getStringExtra("longitude");
        String radius = intent.getStringExtra("radius");
        String myID = intent.getStringExtra("myID");

        EventFetcher eventFetcher = new EventFetcher(currentLatitude+"",currentLongitude+"",radius,myID);
        AsyncTask<String, String, EventList> eventFetchTask = new EventFetcherTask(eventFetcher).execute();

        EventList eventList =null;
        try {
            eventList = eventFetchTask.get();
            System.out.println("verifying if image url are really null");
            for(Event event: eventList.getEventList()){
                System.out.println(event.getImageURL()+"  verifying");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_events);
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
        recyclerView.addOnItemTouchListener(new EventRecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View child, int childPosition) {
                Event event = finalEventList.getEventList().get(childPosition);
                Toast.makeText(getApplicationContext(),event.getName()+" selected",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onLongClick(View child, int childPosition) {
                Event event = finalEventList.getEventList().get(childPosition);
                Toast.makeText(getApplicationContext(),event.getName()+" long",Toast.LENGTH_SHORT).show();

                String PREFS_NAME="ID";
                SharedPreferences prefs = getSharedPreferences(PREFS_NAME,0);
                String id = prefs.getString("userid","-1");

                AsyncTask<String, String, String> postRequest = new BroadCastTask(new BroadCastEvent(id,event)).execute("");
                try {
                    System.out.println("From broadcast request:"+postRequest.get());
                    while(postRequest.get()==null){

                    }
                    final String timer = postRequest.get();
                    System.out.println("waiting for:"+timer+" minutes");
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            Intent displayRespondedUserIntent = new Intent(EventsSelectorActivity.this, DisplayRespondedUsersActivity.class);
                            startActivity(displayRespondedUserIntent);
                        }
                    },Integer.valueOf(timer)*1000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }));
    }

    private void notifyChange(){
        eventsAdapter.notifyDataSetChanged();
    }
}
