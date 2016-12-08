package com.sttarter.common.models;

import com.sttarter.common.responses.STTResponse;

import java.util.ArrayList;

/**
 * Created by rahul on 05/08/15.
 */
public class UserList extends STTResponse {

    ArrayList<User> users;

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }



}
