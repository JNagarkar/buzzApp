package com.group32.cse535.buzzapp.adapters;

import android.view.View;

/**
 * Created by jaydatta on 4/16/17.
 */

public interface ClickListener {
    void onClick(View child, int childPosition);
    void onLongClick(View child, int childPosition);


}
