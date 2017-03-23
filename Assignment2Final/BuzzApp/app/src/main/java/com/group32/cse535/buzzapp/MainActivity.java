package com.group32.cse535.buzzapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.location.LocationListener;

import android.location.Location;
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
import com.group32.cse535.buzzapp.service.LocationUpdateService;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final String BASE_URL = "http://192.168.1.10:8080";


    //Define a request code to send to Google Play services
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private static final int REQUEST_CODE_READ_SMS = 1;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;

    private EditText name;
    private EditText number;
    private EditText email;
    private int defaultRadius=5;

    public String displayName=null;
    public String emailID=null;
    public String givenName=null;

    public String mPhoneNumber=null;

    private Button Create;
    private Button LoginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context mainContext = MainActivity.this;
        mPhoneNumber=null;

        boolean phoneNoSet = getPhoneNumber(mainContext);
        boolean locationSet = getLocation(mainContext);

        boolean locationSeviceSet = getLocationIntent(mainContext);
        // Starting periodic location updating service


        Intent i = getIntent();
    if(i!=null && getIntent().hasExtra("login")){
        GoogleSignInAccount acct =  (GoogleSignInAccount) i.getParcelableArrayExtra("login")[0];
        if(acct!=null){

            displayName = acct.getDisplayName();
            emailID=acct.getEmail();
            givenName = acct.getGivenName();
            AsyncTask<String, String, String> postRequest = new CallAPI().execute("");
            try {
                System.out.println("Recieved:"+postRequest.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }else{
            System.out.println("oooooooooooo");
        }
    }

        setContentView(R.layout.activity_main);
        Create = (Button) findViewById(R.id.Create);
        LoginButton = (Button) findViewById(R.id.LoginButton);

        number = (EditText) findViewById(R.id.phoneNumber);
        name = (EditText) findViewById(R.id.name);

        System.out.println(mPhoneNumber);
        Toast.makeText(MainActivity.this, mPhoneNumber, Toast.LENGTH_LONG).show();

        number.setText(mPhoneNumber);


        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(signInIntent);
            }
        });



        // Create a user
        Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }

    private boolean getLocationIntent(Context mainContext) {

            try{
                Intent locationIntent = new Intent(MainActivity.this, LocationUpdateService.class);
                startService(locationIntent);
            }
            catch(Exception e){
                return false;
            }
        return true;
    }

    private boolean getPhoneNumber(Context mainContext) {
        int permissionCheck = ContextCompat.checkSelfPermission(mainContext,
                Manifest.permission.READ_PHONE_STATE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    REQUEST_CODE_READ_SMS); // define this constant yourself
        }
        if(permissionCheck == PackageManager.PERMISSION_GRANTED){
            // you have the permission
            TelephonyManager tMgr = (TelephonyManager)mainContext.getSystemService(Context.TELEPHONY_SERVICE);
            mPhoneNumber = tMgr.getLine1Number();
            return true;
        }

        return false;
    }


    private boolean getLocation(Context mainContext) {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
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

        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Now lets connect to the API
        mGoogleApiClient.connect();
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.v(this.getClass().getSimpleName(), "onPause()");

        //Disconnect from API onPause()
        if (mGoogleApiClient.isConnected()) {

//            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
  //          mGoogleApiClient.disconnect();
        }


    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    public User getInputValues(String displayName, String email, String givenName) {
        String userName = displayName;

        System.out.println(mPhoneNumber);
        String phoneNumber = mPhoneNumber;
        String emailId = email;
  //      Pattern pattern = Pattern.compile("\\([4-6]{1}[0-9]{2}\\) [0-9]{3}\\-[0-9]{4}$");
//        Matcher matcher = pattern.matcher(phoneNumber);


        if ( userName == null || userName.matches("") || userName.matches("\\d+.*") || phoneNumber==null) {
            Toast.makeText(this, "Enter proper text", Toast.LENGTH_SHORT).show();
            Log.e("USER INPUT", "NOT CORRECT");
            return null;
        }


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
        Calendar cal = Calendar.getInstance();

        System.out.println("--------"+sdf.format(cal.getTime()));
        return new User(userName, emailId, phoneNumber,currentLatitude,currentLongitude,4,5,null);
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

    }

/*    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }*/

    @Override
    public void onConnected(Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);

        } else {
            //If everything went fine lets get latitude and longitude
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();


            Date date = new Date(location.getTime());
            System.out.println("------------------------------->"+date);


            Toast.makeText(this, currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
            /*
             * Google Play services can resolve some errors it detects.
             * If the error has a resolution, try sending an Intent to
             * start a Google Play services activity that can resolve
             * error.
             */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
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

    private class CallAPI extends AsyncTask<String, String, String> {

        private User user = getInputValues(displayName, emailID, givenName);


        public CallAPI() {
            //set context variables if required
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... params) {

            String urlStr = BASE_URL+"/crud/users/create"; // URL to call
            System.out.println(urlStr);

            String result=null;
            try
            {
                RestTemplate restTemplate = new RestTemplate();
                org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();

                if(user==null){
                    return new String("Enter proper text");
                }

                headers.set("Content-type","application-json");
                HttpEntity<String> entity = new HttpEntity<String>("parameters",headers);
                restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
                result = restTemplate.postForObject(urlStr,user,String.class);

            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            //Update the UI
        }

        public String getPostDataString(JSONObject params) throws Exception {

            StringBuilder result = new StringBuilder();
            boolean first = true;

            Iterator<String> itr = params.keys();

            while(itr.hasNext()){

                String key= itr.next();
                Object value = params.get(key);

                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(value.toString(), "UTF-8"));

            }
            return result.toString();
        }


    }

}
