package com.spurtreetech.sttarter.lib.helper.models;

import com.spurtreetech.sttarter.lib.helper.Keys;

/**
 * Created by rahul on 13/08/15.
 */
public class LoginResponse {

    int status;
    String title, msg;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isValidUser() {
        return this.status == Keys.PERFECT_RESPONSE;
    }


}
