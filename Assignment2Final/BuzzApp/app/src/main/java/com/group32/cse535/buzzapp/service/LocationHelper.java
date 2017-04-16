package com.group32.cse535.buzzapp.service;

/**
 * Created by jaydatta on 4/12/17.
 */

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.location.LocationListener;



import android.location.Location;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.Date;

public class LocationHelper implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {


    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private static final int REQUEST_CODE_READ_SMS = 1;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;




    Context contextFromMainActivity=null;
    public LocationHelper(Context contextFromMainActivity) {
        this.contextFromMainActivity=contextFromMainActivity;
    }



    public ArrayList<Double> getMyLocation(){
        System.out.println(contextFromMainActivity+"   check empty");

//      boolean isPresent = getLocationIntent(contextFromMainActivity);
        boolean isThere = getLocation(contextFromMainActivity);
        getL();
        System.out.println(isThere);
        ArrayList<Double> returnList = new ArrayList<>();
        returnList.add(currentLatitude);
        returnList.add(currentLongitude);

        return returnList;
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            /*
             * Google Play services can resolve some errors it detects.
             * If the error has a resolution, try sending an Intent to
             * start a Google Play services activity that can resolve
             * error.
             */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult((Activity) contextFromMainActivity,CONNECTION_FAILURE_RESOLUTION_REQUEST);
                    /*
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
                /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());

        }
    }

    private boolean getLocationIntent(Context mainContext) {

        try{
            Intent locationIntent = new Intent(mainContext, LocationUpdateService.class);
            mainContext.startService(locationIntent);

            System.out.println("started service");

        }
        catch(Exception e){

            System.out.println(e+"caught");

            return false;
        }
        return true;
    }

    private boolean getLocation(Context mainContext) {
        mGoogleApiClient = new GoogleApiClient.Builder(mainContext)
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds


        System.out.println("\n" +
                "\n in getLocation \n\n\n");

        return false;
    }


    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(contextFromMainActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(contextFromMainActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.


            System.out.println("simply returning");


            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


        System.out.println(location+"      check value");

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);

        }
        else {

            System.out.println("returning--->");


            //If everything went fine lets get latitude and longitude
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();


            Date date = new Date(location.getTime());

            System.out.println(currentLatitude+":"+currentLongitude+"    values");
        }
    }

    public void getL(){
        if (ActivityCompat.checkSelfPermission(contextFromMainActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(contextFromMainActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.


            System.out.println("simply returning");


            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


        System.out.println(location+"      check value");

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);

        }
        else {

            System.out.println("returning--->");


            //If everything went fine lets get latitude and longitude
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();


            Date date = new Date(location.getTime());

            System.out.println(currentLatitude+":"+currentLongitude+"    values");
        }
    }



    @Override
    public void onConnectionSuspended(int i) {

    }
}
