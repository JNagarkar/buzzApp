package com.group32.cse535.buzzapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by jaydatta on 4/16/17.
 */

public class EventRecyclerTouchListener implements RecyclerView.OnTouchListener, RecyclerView.OnItemTouchListener {

    private GestureDetector gestureDetector;
    public ClickListener clickListener;

    public EventRecyclerTouchListener(){

    }

    private static final String TAG = "EventRecyclerTouchListener:";
    
    public EventRecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {

        this.clickListener = clickListener;
        gestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null && clickListener != null) {
                    clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                }
            }
        });
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {


        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child = rv.findChildViewUnder(e.getX(), e.getY());
        if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
            clickListener.onClick(child, rv.getChildPosition(child));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
