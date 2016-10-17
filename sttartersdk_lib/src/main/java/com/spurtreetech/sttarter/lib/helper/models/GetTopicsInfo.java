package com.spurtreetech.sttarter.lib.helper.models;

import java.util.ArrayList;

/**
 * Created by RahulT on 18-06-2015.
 */
public class GetTopicsInfo {

    int status;
    //GetTopicsData data;
    ArrayList<Topic> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<Topic> getData() {
        return data;
    }

    public void setData(ArrayList<Topic> data) {
        this.data = data;
    }
}