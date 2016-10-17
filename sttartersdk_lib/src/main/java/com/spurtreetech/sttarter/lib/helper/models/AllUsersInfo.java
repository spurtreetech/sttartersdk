package com.spurtreetech.sttarter.lib.helper.models;

import java.util.ArrayList;

/**
 * Created by rahul on 05/08/15.
 */
public class AllUsersInfo {

    int status;
    ArrayList<Member_data> users;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<Member_data> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<Member_data> users) {
        this.users = users;
    }



}
