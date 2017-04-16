package com.group32.cse535.buzzapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by jaydatta on 4/13/17.
 */

public class MyFireBaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FireBaseInstanceIDService";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refrestoken: " + refreshedToken);

        String NAME="RefreshToken";
        SharedPreferences prefs = getSharedPreferences(NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("RefreshToken", refreshedToken);
        editor.commit();

        System.out.println("Stored refresh token into shared preferences");

        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */

    // send to our server
    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.



    }

}
