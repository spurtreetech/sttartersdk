package com.sttarter.coupons.models;

import com.sttarter.common.responses.STTResponse;

/**
 * Created by RahulT on 18-06-2015.
 */
public class CouponResponse extends STTResponse {

    CouponResult result;

    public CouponResult getResult() {
        return result;
    }

    public void setResult(CouponResult result) {
        this.result = result;
    }
}
