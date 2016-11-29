package com.sttarter.referral;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.sttarter.common.responses.STTResponse;
import com.sttarter.helper.interfaces.STTSuccessListener;
import com.sttarter.init.PreferenceHelper;
import com.sttarter.init.RequestQueueHelper;
import com.sttarter.init.STTKeys;
import com.sttarter.referral.interfaces.STTReferralInterface;
import com.sttarter.referral.models.ReferralResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shahbaz on 24-11-2016.
 */

public class ReferralManager {

    private static ReferralManager instance = null;

    public static synchronized ReferralManager getInstance() {

        if (ReferralManager.instance == null) {
            ReferralManager.instance = new ReferralManager();
        }

        return ReferralManager.instance;
    }

    public void signup(String externalUserId, String name, String email, String phone, String referer_code, STTSuccessListener signupSuccessResponseListener, Response.ErrorListener errorListener) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("externalUserId", externalUserId);
            jsonObject.put("name", name);
            jsonObject.put("email", email);
            jsonObject.put("phone", phone);
            jsonObject.put("referer_code", referer_code);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = STTKeys.REFERRAL_SIGNUP;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, jsonObject, getSignUpSuccessListener(signupSuccessResponseListener), errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return ReferralManager.getHeaders();
            }
        };

        jsonObjReq.setShouldCache(false);

        // Adding request to request queue
        RequestQueueHelper.addToRequestQueue(jsonObjReq);
    }

    private Response.Listener<JSONObject> getSignUpSuccessListener(final STTSuccessListener sTTSuccessListener) {

        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject responseFromSignUp) {

                sTTSuccessListener.Response(new STTResponse());

            }
        };
    }


    public void getReferral(STTReferralInterface sttReferralInterface, Response.ErrorListener errorListener) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("externalUserId", PreferenceHelper.getSharedPreference().getString(STTKeys.USER_ID,""));
            jsonObject.put("name", PreferenceHelper.getSharedPreference().getString(STTKeys.USER_USERNAME,""));
            jsonObject.put("email", PreferenceHelper.getSharedPreference().getString(STTKeys.USER_EMAIL,""));
            jsonObject.put("phone", PreferenceHelper.getSharedPreference().getString(STTKeys.USER_PHONE,""));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = STTKeys.GET_REFERRAL_CODE;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, jsonObject, getReferranceSuccessListener(sttReferralInterface), errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return ReferralManager.getHeaders();
            }
        };

        jsonObjReq.setShouldCache(false);

        // Adding request to request queue
        RequestQueueHelper.addToRequestQueue(jsonObjReq);
    }

    private Response.Listener<JSONObject> getReferranceSuccessListener(final STTReferralInterface sttReferralInterface) {

        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject responseFromReferral) {

                Gson gson = new Gson();

                ReferralResponse referralResponse = gson.fromJson(responseFromReferral.toString(), ReferralResponse.class);

                sttReferralInterface.Response(referralResponse);

            }
        };
    }

    public void addTransaction(String transactionID,String transactionAmount,STTSuccessListener sttSuccessListener, Response.ErrorListener errorListener) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("externalUserId", PreferenceHelper.getSharedPreference().getString(STTKeys.USER_ID,""));
            jsonObject.put("transaction_id", transactionID);
            jsonObject.put("transaction_amount", transactionAmount);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = STTKeys.ADD_REFERRAL_TRANSACTION;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, jsonObject, addReferranceTransactionSuccessListener(sttSuccessListener), errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return ReferralManager.getHeaders();
            }
        };

        jsonObjReq.setShouldCache(false);

        // Adding request to request queue
        RequestQueueHelper.addToRequestQueue(jsonObjReq);
    }

    private Response.Listener<JSONObject> addReferranceTransactionSuccessListener(final STTSuccessListener sttSuccessListener) {

        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject responseFromAddReferrance) {
                Gson gson = new Gson();
                STTResponse sttResponse = gson.fromJson(responseFromAddReferrance.toString(),STTResponse.class);
                sttSuccessListener.Response(sttResponse);

                Log.d("MyResponse",responseFromAddReferrance.toString());

            }
        };
    }


    protected static Map<String, String>  getHeaders() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("x-app-token", PreferenceHelper.getSharedPreference().getString(STTKeys.AUTH_TOKEN,""));
        headers.put("Content-Type", "application/json");
        return headers;
    }

}
