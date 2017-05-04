package com.group32.cse535.buzzapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.group32.cse535.buzzapp.utils.BroadCastEvent;
import com.group32.cse535.buzzapp.receiver.BroadCastMessageResponse;
import com.group32.cse535.buzzapp.adapters.ClickListener;
import com.group32.cse535.buzzapp.adapters.DisplayUserAdapter;
import com.group32.cse535.buzzapp.adapters.DividerItemDecoration;
import com.group32.cse535.buzzapp.adapters.EventRecyclerTouchListener;
import com.group32.cse535.buzzapp.R;
import com.group32.cse535.buzzapp.models.User;
import com.group32.cse535.buzzapp.utils.UserFetcher;
import com.group32.cse535.buzzapp.service.task.UserFetcherTask;
import com.group32.cse535.buzzapp.utils.UserList;
import com.group32.cse535.buzzapp.service.task.BroadCastTask;
import com.group32.cse535.buzzapp.models.Event;

import java.util.concurrent.ExecutionException;

/**
 * Created by jaydatta on 4/17/17.
 */

public class DisplayRespondedUsersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DisplayUserAdapter userAdapter;

    private static final String TAG = "DisplayRespondedUserActivity:";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_display_users);



        String PREFS_NAME="ID";
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME,0);
        String senderID = prefs.getString("userid","-1");

        String NAME="RefreshToken";
        prefs = getSharedPreferences(NAME, Context.MODE_PRIVATE);
        String refreshToken = prefs.getString("RefreshToken","-1");

        Intent intent = getIntent();
        final String eventID = intent.getStringExtra("event");
        final String timeEventStarted= intent.getStringExtra("broadCastTime");
        this.setTitle(intent.getStringExtra("eventName"));

        UserFetcher userFetcher = new UserFetcher(senderID,refreshToken,eventID,Long.valueOf(timeEventStarted));
        //This should happen instantly
        AsyncTask<String, String, UserList> userFetchTask = new UserFetcherTask(userFetcher).execute();


        String secondBroadCastID = intent.getStringExtra("secondBroadCastID");
        String secondBroadCastEventName = intent.getStringExtra("secondBroadCastEventName");
        String secondBroadCastEventID = intent.getStringExtra("secondBroadCastEventID");
        String secondBroadCastEventURL = intent.getStringExtra("secondBroadCastEventURL");
        String secondBroadCastEventImageURL = intent.getStringExtra("secondBroadCastEventImageURL");
        String secondBroadCastEventStartDate = intent.getStringExtra("secondBroadCastEventStartDate");
        String secondBroadCastEventStartTime = intent.getStringExtra("secondBroadCastEventStartTime");
        String secondBroadCastEventVenue = intent.getStringExtra("secondBroadCastEventVenue");


        boolean secondOver = false;
        if(intent.hasExtra("secondOver")){
            secondOver=true;
        }


        UserList userList=null;
        try {
            userList = userFetchTask.get();

            if(userList.getUserList()!=null){
                for(User currentUser: userList.getUserList()){
                    Log.v(TAG,currentUser.getName()+"  responded yes");
                }

            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if(userList.getUserList().size()!=0){
            setContentView(R.layout.activty_display_users);
            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            userAdapter = new DisplayUserAdapter(userList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            recyclerView.setAdapter(userAdapter);

            final UserList finalUserList = userList;

            recyclerView.addOnItemTouchListener(new EventRecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
                @Override
                public void onClick(View child, int childPosition) {
                    User user = finalUserList.getUserList().get(childPosition);
                    Toast.makeText(getApplicationContext(),user.getName()+" selected",Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onLongClick(View child, int childPosition) {
                    User user = finalUserList.getUserList().get(childPosition);
                    Toast.makeText(getApplicationContext(),user.getName()+" send message",Toast.LENGTH_SHORT).show();


                    try{
                        SmsManager smsManager  = SmsManager.getDefault();
                        smsManager.sendTextMessage(user.getContactNumber().trim(),null,"Hey "+user.getName()+"  excited for the plan?",null,null);
                        Toast.makeText(getApplicationContext(), "SMS Sent!",
                                Toast.LENGTH_LONG).show();
                    }
                    catch(Exception e){
                        Toast.makeText(getApplicationContext(),
                                "SMS failed, please try again later!",
                                Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }

                public void onClickWhatsApp(View view) {

                    PackageManager pm=getPackageManager();
                    try {

                        Intent waIntent = new Intent(Intent.ACTION_SEND);
                        waIntent.setType("text/plain");
                        String text = "YOUR TEXT HERE";

                        PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                        //Check if package exists or not. If not then code
                        //in catch block will be called
                        waIntent.setPackage("com.whatsapp");

                        waIntent.putExtra(Intent.EXTRA_TEXT, text);
                        startActivity(Intent.createChooser(waIntent, "Share with"));

                    } catch (PackageManager.NameNotFoundException e) {
                       /* Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                                .show();*/
                    }
                }
            }));
        }
        else if(!secondOver){
            Toast.makeText(this,"No one responded,Lets wait for some more time",Toast.LENGTH_SHORT).show();
            //Broadcast again, but now with double radius

            final Event event =  new Event();
            event.setEventURL(secondBroadCastEventURL);
            event.setVenue(secondBroadCastEventVenue);
            event.setStartTime(secondBroadCastEventStartTime);
            event.setImageURL(secondBroadCastEventImageURL);
            event.setId(secondBroadCastEventID);
            event.setName(secondBroadCastEventName);
            event.setStartDate(secondBroadCastEventStartDate);


            BroadCastEvent broadCastEvent = new BroadCastEvent(secondBroadCastID,event,null);
            broadCastEvent.setPersonalMessage("secondTime");
            AsyncTask<String, String, BroadCastMessageResponse> postRequest = new BroadCastTask(broadCastEvent).execute("");

            try {
                Log.v(TAG,"From broadcast request:"+postRequest.get());
                while(postRequest.get()==null){

                }
                final BroadCastMessageResponse response = postRequest.get();
                Log.v(TAG,"waiting for:"+response.getExpectedTime()+" minutes, double time");
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Log.v(TAG,"End of time, now polling");
                        Intent displayRespondedUserIntent = new Intent(DisplayRespondedUsersActivity.this, DisplayRespondedUsersActivity.class);
                        displayRespondedUserIntent.putExtra("event",event.getId()+"");
                        displayRespondedUserIntent.putExtra("eventName",event.getName()+"");
                        displayRespondedUserIntent.putExtra("broadCastTime",response.getBroadCastEventCurrentTime()+"");

                        Log.v(TAG,response.getBroadCastEventCurrentTime()+" this is broadcast time");
                        displayRespondedUserIntent.putExtra("secondOver","yes");
                        startActivity(displayRespondedUserIntent);
                    }
                },Integer.valueOf(response.getExpectedTime())*1000*10);

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        else{
            Toast.makeText(this,"You are not in luck :(",Toast.LENGTH_SHORT).show();
        }
    }
}
