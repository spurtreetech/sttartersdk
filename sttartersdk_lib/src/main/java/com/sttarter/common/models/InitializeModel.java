package com.sttarter.common.models;

/**
 * Created by Aishvarya on 17-11-2016.
 */

public class InitializeModel {

    //Mandatory
    String appKey;
    String appSecret;

    //Mandatory for all types of authentication
    String userIdentifier; // Could be phone, username, email ID or externalID

    //Mandatory if type of authentication is STTARTER enabled UN/PWD
    String password;



    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getUserIdentifier() {
        return userIdentifier;
    }

    public void setUserIdentifier(String userIdentifier) {
        this.userIdentifier = userIdentifier;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
