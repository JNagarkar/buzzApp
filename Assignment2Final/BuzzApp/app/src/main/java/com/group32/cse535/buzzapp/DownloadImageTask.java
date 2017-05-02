package com.group32.cse535.buzzapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.group32.cse535.buzzapp.service.EventList;

import java.io.InputStream;

/**
 * Created by jaydatta on 4/16/17.
 */

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    EventsAdapter.MyViewHolder holder;

    private static final String TAG = "DownloadImageTask:";
    public DownloadImageTask(EventsAdapter.MyViewHolder holder) {
        this.holder = holder;
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];

        Log.v(TAG,urldisplay+" this is url to display");
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("DownloadImageTask","Error in downloading image "+ e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }
    protected void onPostExecute(Bitmap result) {
        holder.imageView.setImageBitmap(result);
    }
}
