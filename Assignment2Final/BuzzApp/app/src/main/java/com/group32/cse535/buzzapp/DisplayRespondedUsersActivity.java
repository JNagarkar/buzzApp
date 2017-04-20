package com.group32.cse535.buzzapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

/**
 * Created by jaydatta on 4/17/17.
 */

public class DisplayRespondedUsersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DisplayUserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_display_users);

        //TODO: write userFetcher and userFetcherTask

        String PREFS_NAME="ID";
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME,0);
        String senderID = prefs.getString("userid","-1");

        String NAME="RefreshToken";
        prefs = getSharedPreferences(NAME, Context.MODE_PRIVATE);
        String refreshToken = prefs.getString("RefreshToken","-1");

        Intent intent = getIntent();
        final String eventID = intent.getStringExtra("event");
        final String timeEventStarted= intent.getStringExtra("broadCastTime");

        UserFetcher userFetcher = new UserFetcher(senderID,refreshToken,eventID,Long.valueOf(timeEventStarted));
        //This should happen instantly
        AsyncTask<String, String, UserList> userFetchTask = new UserFetcherTask(userFetcher).execute();

        UserList userList=null;
        try {
            userList = userFetchTask.get();

            if(userList.getUserList()!=null){
                for(User currentUser: userList.getUserList()){
                    System.out.println(currentUser.getName()+"  responded yes");
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

                    //TODO: Check if you can send message from here

                    try{
                        SmsManager smsManager  = SmsManager.getDefault();
                        smsManager.sendTextMessage(user.getContactNumber().trim(),null,"Hey "+user.getName()+"  excited for the plan?",null,null);
                        Toast.makeText(getApplicationContext(), "SMS Sent!",
                                Toast.LENGTH_LONG).show();
                    }
                    catch(Exception e){
                        Toast.makeText(getApplicationContext(),
                                "SMS faild, please try again later!",
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
        else{
            Toast.makeText(this,"No one responded",Toast.LENGTH_SHORT).show();
        }

    }
}
