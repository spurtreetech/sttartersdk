package com.sttarter.common.models;

/**
 * Created by Aishvarya on 17-11-2016.
 */

public class AuthModel {

    String appKey;
    String appSecret;

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
}
