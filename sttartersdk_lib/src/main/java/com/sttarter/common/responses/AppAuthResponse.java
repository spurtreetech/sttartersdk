package com.sttarter.common.responses;

/**
 * Created by Aishvarya on 15-11-2016.
 */

public class AppAuthResponse extends STTResponse {

    //Token for authenticated app.
    String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
