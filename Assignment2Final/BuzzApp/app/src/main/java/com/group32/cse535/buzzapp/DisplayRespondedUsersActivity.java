package com.group32.cse535.buzzapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.group32.cse535.buzzapp.service.BroadCastTask;
import com.group32.cse535.buzzapp.service.Event;
import com.group32.cse535.buzzapp.service.EventFetcherTask;
import com.group32.cse535.buzzapp.service.EventList;

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

        UserFetcher userFetcher = new UserFetcher();
        AsyncTask<String, String, UserList> userFetchTask = new UserFetcherTask(userFetcher).execute();

        UserList userList=null;
        try {
            userList = userFetchTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

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
            }
        }));
    }


}
