package com.group32.cse535.buzzapp;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Parcelable;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.group32.cse535.buzzapp.service.Event;

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

        if(remoteMessage.getNotification()!=null){

            sendNotification(remoteMessage.getNotification().getBody(),remoteMessage.getNotification().getTitle());

        }
        else{
            Event event=null;
            User sender=null;
            String senderID=null;
            Long broadCastTime=null;
            String personalMessage = null;

            boolean errorCondition=false;


            String notificationBody=null;
            String notificationTitle=null;

            Map<String, String> hmap = remoteMessage.getData();
            if(hmap.containsKey("data")){
                if(hmap.get("data").equals("dummyData")){
                    errorCondition=true;
                }
            }
            notificationBody =hmap.get("notificationBody");
            notificationTitle = hmap.get("notificationTitle");
            personalMessage = hmap.get("personalMessage");

            Log.v(TAG,notificationBody+":"+notificationTitle);

            if (remoteMessage.getData().size() > 0 && errorCondition==false) {
                Log.d(TAG, "Message data payload: " + remoteMessage.getData());
                // Handle message within 10 seconds
                event = extractEvent(remoteMessage);
                sender = extractUser(remoteMessage);
                senderID = remoteMessage.getData().get("senderID");
                if(remoteMessage.getData().containsKey("broadCastTime")){
                    broadCastTime = Long.valueOf(remoteMessage.getData().get("broadCastTime"));
                    Log.v(TAG,broadCastTime+"  set to");

                }
                Log.v(TAG,event.getName()+":"+event.getId()+":"+event.getVenue()+":"+event.getEventURL());
            }
            else{
                Log.v(TAG,"No payLoad found");
            }

            // Check if message contains a notification payload.
            if (remoteMessage.getNotification() != null) {
                Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            }

            // TODO: This is to send notification to user end.
            sendNotificationAndData(personalMessage,notificationTitle,notificationBody,event,sender,senderID,broadCastTime);
        }
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

        Log.v(TAG,str);*/
        /*for(Map.Entry<String,String> entry: remoteDataMap.entrySet()){
            Log.v(TAG,entry.getKey()+":"+entry.getValue());
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

    public static final String INTENT_FILTER = "INTENT_FILTER";

    private void sendNotification(String messageBody, String messageTitle) {

        Intent intent = new Intent(Intent.makeMainActivity(new ComponentName(this, MainActivity.class)));
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(EventRecievedActivity.class);
        stackBuilder.addNextIntent(intent);

        int requestID = (int) System.currentTimeMillis();

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this.getApplicationContext(), requestID/* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.common_full_open_on_phone)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);


        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(1000);

        notificationBuilder.setContentIntent(pendingIntent);
        //    startActivity(intent);
        notificationManager.notify(requestID /* ID of notification */, notificationBuilder.build());

    }



    private void sendNotificationAndData(String personalMessage,String messageBody, String messageTitle, Event event, User sender, String senderID, Long broadCastTime) {

        Context eventContext = EventRecievedActivity.getEventRecievedContext();
        Intent intent = new Intent(Intent.makeMainActivity(new ComponentName(this, EventRecievedActivity.class)));

        Parcelable[] pc = new Parcelable[2];
        pc[0] = event;
        pc[1]=sender;

        intent.putExtra("parcel",pc);
        intent.putExtra("senderID",senderID);
        intent.putExtra("broadCastTime",broadCastTime);
        intent.putExtra("personalMessage",personalMessage);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(EventRecievedActivity.class);
        stackBuilder.addNextIntent(intent);

        int requestID = (int) System.currentTimeMillis();

        intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this.getApplicationContext(), requestID /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);


        Log.v(TAG,"this:"+this.getApplicationContext());
//        Log.v(TAG,eventContext.getApplicationContext()+":"+eventContext.getApplicationInfo());

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.common_full_open_on_phone)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(1000);

        notificationBuilder.setContentIntent(pendingIntent);
    //    startActivity(intent);
        notificationManager.notify(requestID /* ID of notification */, notificationBuilder.build());

    }

}