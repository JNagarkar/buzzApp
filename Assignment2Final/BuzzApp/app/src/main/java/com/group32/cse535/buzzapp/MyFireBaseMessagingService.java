package com.group32.cse535.buzzapp;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.group32.cse535.buzzapp.service.Event;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by jaydatta on 4/13/17.
 */

public class MyFireBaseMessagingService  extends FirebaseMessagingService {

    private static final String TAG = "FCM Service";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.

        Event event=null;
        User sender=null;
        String senderID=null;
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
           // Handle message within 10 seconds
                event = extractEvent(remoteMessage);
                sender = extractUser(remoteMessage);
                senderID = remoteMessage.getData().get("senderID");
            System.out.println(event.getName()+":"+event.getId()+":"+event.getVenue()+":"+event.getEventURL());
        }
        else{
            System.out.println("No payLoad found");
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // TODO: This is to send notification to user end.
        sendNotification(remoteMessage.getNotification().getBody(),remoteMessage.getNotification().getTitle(),event,sender,senderID);
        checkNotification(event,sender);
    }

    private User extractUser(RemoteMessage remoteMessage){
        Log.d(TAG, "Short lived task is done.");
        Map<String, String> remoteDataMap = remoteMessage.getData();

        User sender = new User();
        if(remoteDataMap.containsKey("sender")){
            sender.setName(remoteDataMap.get("sender"));
        }
        if(remoteDataMap.containsKey("senderPhone")){
            sender.setContactNumber(remoteDataMap.get("senderPhone"));
        }
        if(remoteDataMap.containsKey("senderMailID")){
            sender.setEmail(remoteDataMap.get("senderMailID"));
        }
        return sender;
    }

    private Event extractEvent(RemoteMessage remoteMessage) {

        Log.d(TAG, "Short lived task is done.");
        Map<String, String> remoteDataMap = remoteMessage.getData();
        //TODO: use BeanWrapper instead of Gavthi method
        Event event = new Event();
/*        String str = remoteDataMap.get("EventObject");
        JsonParser jsonParser = new JsonParser();
        JsonElement json = jsonParser.parse(str);

        System.out.println(str);*/
        /*for(Map.Entry<String,String> entry: remoteDataMap.entrySet()){
            System.out.println(entry.getKey()+":"+entry.getValue());
        }
*/
        String[] values = {"id","name","imageURL","eventURL","startDate","startTime","venue"};
        if(remoteDataMap.containsKey("id")){
            event.setId(remoteDataMap.get("id"));
        }
        if(remoteDataMap.containsKey("name")){
            event.setName(remoteDataMap.get("name"));
        }
        if(remoteDataMap.containsKey("imageURL")){
            event.setImageURL(remoteDataMap.get("imageURL"));
        }
        if(remoteDataMap.containsKey("eventURL")){
            event.setEventURL(remoteDataMap.get("eventURL"));
        }
        if(remoteDataMap.containsKey("startDate")){
            event.setStartDate(remoteDataMap.get("startDate"));
        }
        if(remoteDataMap.containsKey("startTime")){
            event.setStartTime(remoteDataMap.get("startTime"));
        }
        if(remoteDataMap.containsKey("venue")){
            event.setVenue(remoteDataMap.get("venue"));
        }

        return event;
    }

    private void sendNotification(String messageBody,String messageTitle,Event event,User sender,String senderID) {
        Intent intent = new Intent(this.getApplicationContext(), EventRecievedActivity.class);

        Parcelable[] pc = new Parcelable[2];
        pc[0] = event;
        pc[1]=sender;

        intent.putExtra("parcel",pc);
        intent.putExtra("senderID",senderID);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this.getApplicationContext(), 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.common_full_open_on_phone)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationBuilder.setContentIntent(pendingIntent);
    //    startActivity(intent);
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

    }

    public static void checkNotification(Event event,User sender){


    }
}