package com.sttarter.common.responses;

import com.sttarter.common.models.User;

/**
 * Created by Aishvarya on 15-11-2016.
 */

public class SignUpResponse extends STTResponse {
    //Placeholder for additional parameters available upon sign up.
    User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
