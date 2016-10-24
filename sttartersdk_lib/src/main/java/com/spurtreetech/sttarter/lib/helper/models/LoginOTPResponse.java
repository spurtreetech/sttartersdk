package com.spurtreetech.sttarter.lib.helper.models;

import com.spurtreetech.sttarter.lib.helper.STTKeys;

/**
 * Created by rahul on 13/08/15.
 */
public class LoginOTPResponse {


    int status;
    private String stt_token;
    private String user_token;
    private String ie_token;
    private String app_key;
    private String app_secret;
    private String username;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStt_token() {
        return stt_token;
    }

    public void setStt_token(String stt_token) {
        this.stt_token = stt_token;
    }

    public String getIe_token() {
        return ie_token;
    }

    public void setIe_token(String ie_token) {
        this.ie_token = ie_token;
    }

    public String getApp_key() {
        return app_key;
    }

    public void setApp_key(String app_key) {
        this.app_key = app_key;
    }

    public String getApp_secret() {
        return app_secret;
    }

    public void setApp_secret(String app_secret) {
        this.app_secret = app_secret;
    }

    public boolean hasUserLoggedIn() {
        return this.status == STTKeys.PERFECT_RESPONSE;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getUser_token() {
        return user_token;
    }

    public void setUser_token(String user_token) {
        this.user_token = user_token;
    }

}
