package com.spurtreetech.sttarter.lib.helper;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

public class RequestQueueHelper {

    private static final String TAG = "RequestQueueHelper";
    private static HashMap<String, Boolean> errorResolverFlags = new HashMap<>();

    static {
        errorResolverFlags.put("AuthFailureError", false);
        errorResolverFlags.put("ServerError", false);
        errorResolverFlags.put("NetworkError", false);
        errorResolverFlags.put("ParseError", false);
    }

    public static <T> Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<String, String>();
        return headers;
    }

    public static <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public static <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public static void cancelPendingRequests(Object tag) {
        if (getRequestQueue() != null) {
            getRequestQueue().cancelAll(tag);
        }
    }

    private static RequestQueue getRequestQueue() {
        return STTarter.getInstance().getRequestQueue();
    }

    public static Response.ErrorListener responseErrorListener() {

        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressBarLayout.setVisibility(RelativeLayout.INVISIBLE);
                //Toast.makeText(EmergencyInfo.this, "An error occurred. Please try again.", Toast.LENGTH_SHORT);
                //Log.e("Alerts Error", error.getMessage());
                String json = "";
                /*
                try {
                    json = new String(
                            error.networkResponse.data,
                            HttpHeaderParser.parseCharset(error.networkResponse.headers));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                */

                Log.e("RequestQueueHelper", "error response: " + json);
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                } else if (error instanceof AuthFailureError) {
                    // TODO call retrieveAccessToken and
                    Log.e(this.getClass().getSimpleName(), "AuthFailureError");
                    if(!errorResolverFlags.get("AuthFailureError")) {
                        STTGeneralRoutines gr = new STTGeneralRoutines();
                        gr.retrieveNewToken();
                        errorResolverFlags.put("AuthFailureError", true);
                    } else {
                        errorResolverFlags.put("AuthFailureError", false);
                    }
                } else if (error instanceof ServerError) {
                    //TODO
                    Log.e(this.getClass().getSimpleName(), "ServerError");
                } else if (error instanceof NetworkError) {
                    //TODO
                    Log.e(this.getClass().getSimpleName(), "NetworkError");
                } else if (error instanceof ParseError) {
                    //TODO
                    Log.e(this.getClass().getSimpleName(), "ParseError");
                }
            }
        };
    }
}