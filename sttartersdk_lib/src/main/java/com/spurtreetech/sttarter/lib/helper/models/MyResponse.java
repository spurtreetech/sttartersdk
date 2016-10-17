package com.spurtreetech.sttarter.lib.helper.models;

/**
 * Created by RahulT on 23-06-2015.
 */
public class MyResponse {

    int status;
    String title;
    String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
