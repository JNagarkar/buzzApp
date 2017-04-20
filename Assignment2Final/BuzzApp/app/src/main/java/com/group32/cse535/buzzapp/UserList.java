package com.group32.cse535.buzzapp;



import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaydatta on 4/17/17.
 */

public class UserList {

    public ArrayList<User> getUserList() {
        return userList;
    }

    public void setUserList(ArrayList<User> userList) {
        this.userList = userList;
    }

    public UserList(ArrayList<User> userList) {
        this.userList = userList;
    }

    public UserList(){

    }

    public ArrayList<User> userList;


}
