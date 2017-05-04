package com.group32.cse535.buzzapp.adapters;


import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.group32.cse535.buzzapp.service.task.DownloadImageTask;
import com.group32.cse535.buzzapp.R;
import com.group32.cse535.buzzapp.models.Event;
import com.group32.cse535.buzzapp.utils.EventList;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by jaydatta on 4/16/17.
 */

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.MyViewHolder>{


    private EventList eventList;

    private ArrayList<String> imageList = new ArrayList<>();

    private static String TAG="EventsAdaper:";

    View myView;
    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView name,venue,startDate,startTime;
        public ImageView imageView;
        public MyViewHolder(View view){
            super(view);
            name = (TextView)view.findViewById(R.id.eventName);
            venue = (TextView) view.findViewById(R.id.eventVenue);
            startDate = (TextView)view.findViewById(R.id.eventStartDate);
            startTime = (TextView)view.findViewById(R.id.eventStartTime);
            imageView = (ImageView) view.findViewById(R.id.eventImage);
//            view.setOnClickListener(this);
        }

    }

    public EventsAdapter(EventList eventList){
        this.eventList = eventList;
        setImageList();
    }

    private void setImageList() {

        imageList.add("https://s3.amazonaws.com/content.ravinia.org/images/2014_LandingPage_Buttons/Tickets/tickets_header.jpg");
        imageList.add("http://www.deanshu.com/wp-content/uploads/2014/08/Parachute-House-of-Blues-Anaheim.jpg");
        imageList.add("https://www.callingallgigs.com/wp-content/uploads/2011/11/concert-crowd.jpg");
        imageList.add("http://www.ks95.com/wp-content/uploads/2014/04/94130125.jpg");
        imageList.add("https://www.ste-michelle.com/assets/visit_us/faq_btm.jpg");
        imageList.add("http://www.stranahantheater.org/wp-content/uploads/2016/09/concert.jpg");
        imageList.add("http://az616578.vo.msecnd.net/files/2016/06/30/636029240982230102667140873_635945327802088955996706849_o-ROCK-CONCERT-facebook.jpg");
        imageList.add("http://i.ebayimg.com/images/g/5OcAAOSwTuJYo4Jm/s-l1600.jpg");
        imageList.add("http://az616578.vo.msecnd.net/files/2016/07/30/636054893919341118-840154384_concert-crowd.jpg");
        imageList.add("http://www.ticketswest.com/media/1028/concert_Featured%20Event%20Tile.jpg");
        imageList.add("http://az616578.vo.msecnd.net/files/2016/05/09/635983505329496433385654456_concert-audience.jpg");

    }


    @Override
    public EventsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EventsAdapter.MyViewHolder holder, int position) {
        Event event = eventList.getEventList().get(position);
        holder.name.setText(event.getName());
        holder.venue.setText(event.getVenue());
        holder.startDate.setText(event.getStartDate());
        holder.startTime.setText(event.getStartTime());

        Random rand = new Random();


        Log.v(TAG,event.getImageURL()+"  image url");
        if(!event.getName().equals("NotFound")){
            if(event.getImageURL()!=null){
                AsyncTask<String,Void,Bitmap> downloadTask = new DownloadImageTask(holder).execute(event.getImageURL());
            }
            else{
                int  n = (imageList.size()-1) % (position+1);
                AsyncTask<String,Void,Bitmap> downloadTask = new DownloadImageTask(holder).execute(imageList.get(n));
            }
        }
    }

    @Override
    public int getItemCount() {
        return eventList.getEventList().size();
    }
}
