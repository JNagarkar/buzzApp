package com.group32.cse535.buzzapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.group32.cse535.buzzapp.R;
import com.group32.cse535.buzzapp.models.User;
import com.group32.cse535.buzzapp.utils.UserList;

/**
 * Created by jaydatta on 4/17/17.
 */

public class DisplayUserAdapter extends RecyclerView.Adapter<DisplayUserAdapter.MyViewHolder> {

    public UserList userList;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView userName,userContact,userEmail,eventName;

        public MyViewHolder(View view) {
            super(view);
            userName = (TextView) view.findViewById(R.id.userName);
            userContact = (TextView) view.findViewById(R.id.userContact);
            userEmail= (TextView) view.findViewById(R.id.userEmail);
            eventName= (TextView) view.findViewById(R.id.eventName);
        }
    }

    public DisplayUserAdapter(UserList userList){
        this.userList = userList;
    }

    @Override
    public DisplayUserAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DisplayUserAdapter.MyViewHolder holder, int position) {
        User currentUser = userList.getUserList().get(position);
        holder.userName.setText(currentUser.getName());
        holder.userContact.setText(currentUser.getContactNumber());
        holder.userEmail.setText(currentUser.getEmail());
        holder.eventName.setText(currentUser.getToken());
    }

    @Override
    public int getItemCount() {
        return userList.getUserList().size();
    }
}
