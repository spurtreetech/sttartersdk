package com.sttarter.referral.models;

import com.sttarter.common.responses.STTResponse;

/**
 * Created by Shahbaz on 25-11-2016.
 */

public class ReferralResponse extends STTResponse {

    String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
