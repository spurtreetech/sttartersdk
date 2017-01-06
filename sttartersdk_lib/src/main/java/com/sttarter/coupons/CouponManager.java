package com.sttarter.coupons;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.sttarter.coupons.interfaces.STTCouponInterface;
import com.sttarter.coupons.models.CouponResponse;
import com.sttarter.init.PreferenceHelper;
import com.sttarter.init.RequestQueueHelper;
import com.sttarter.init.STTKeys;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shahbaz on 23-12-2016.
 */

public class CouponManager {

    private static CouponManager instance = null;

    public static synchronized CouponManager getInstance() {

        if(CouponManager.instance == null) {
            CouponManager.instance = new CouponManager();
        }

        return CouponManager.instance;
    }

    public void redeemCoupon(String order_value, String coupon_code, STTCouponInterface sttCouponInterface, Response.ErrorListener errorListener) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("user_name", PreferenceHelper.getSharedPreference().getString(STTKeys.USER_ID,""));
            jsonObject.put("order_value", order_value);
            jsonObject.put("coupon_code", coupon_code);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = STTKeys.COUPON_REDEEM;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, jsonObject, getReferranceSuccessListener(sttCouponInterface), errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return CouponManager.getHeaders();
            }
        };

        jsonObjReq.setShouldCache(false);

        // Adding request to request queue
        RequestQueueHelper.addToRequestQueue(jsonObjReq);
    }

    private Response.Listener<JSONObject> getReferranceSuccessListener(final STTCouponInterface sttCouponInterface) {

        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject responseFromCoupon) {
                try {
                    Gson gson = new Gson();
                    CouponResponse referralResponse = gson.fromJson(responseFromCoupon.toString(), CouponResponse.class);
                    sttCouponInterface.Response(referralResponse);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
    }

    protected static Map<String, String>  getHeaders() {
        Map<String, String> headers = new HashMap<String, String>();
        try {
            headers.put("x-user-token", PreferenceHelper.getSharedPreference().getString(STTKeys.USER_TOKEN, ""));
            headers.put("x-app-token", PreferenceHelper.getSharedPreference().getString(STTKeys.AUTH_TOKEN, ""));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return headers;
    }

}
