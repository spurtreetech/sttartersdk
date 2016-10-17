package com.spurtreetech.sttarter.lib.helper.models;

import java.util.ArrayList;

/**
 * Created by RahulT on 18-06-2015.
 */
public class GetTopicsData {

    int count;
    ArrayList<Topic> rows;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<Topic> getRows() {
        return rows;
    }

    public void setRows(ArrayList<Topic> rows) {
        this.rows = rows;
    }
}
