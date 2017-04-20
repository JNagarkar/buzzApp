package com.group32.cse535.buzzapp;


import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.group32.cse535.buzzapp.service.Event;
import com.group32.cse535.buzzapp.service.EventList;

import org.w3c.dom.Text;

/**
 * Created by jaydatta on 4/16/17.
 */

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.MyViewHolder>{


    private EventList eventList;
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

        System.out.println(event.getImageURL()+"  image url");
        if(event.getImageURL()!=null){
            AsyncTask<String,Void,Bitmap> downloadTask = new DownloadImageTask(holder).execute(event.getImageURL());
        }
        else{
            AsyncTask<String,Void,Bitmap> downloadTask = new DownloadImageTask(holder).execute("http://www.adweek.com/wp-content/uploads/files/news_article/justin-beiber-hed2-2013.jpg");
        }
    }

    @Override
    public int getItemCount() {
        return eventList.getEventList().size();
    }
}
