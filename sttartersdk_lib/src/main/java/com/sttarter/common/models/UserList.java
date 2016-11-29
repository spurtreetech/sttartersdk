package com.sttarter.common.models;

import java.util.ArrayList;

/**
 * Created by rahul on 05/08/15.
 */
public class UserList {

    int status;
    ArrayList<User> users;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }



}
