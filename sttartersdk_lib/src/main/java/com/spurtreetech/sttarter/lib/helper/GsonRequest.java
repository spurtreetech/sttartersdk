package com.spurtreetech.sttarter.lib.helper;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.Map;


/**
 * Created by Rahul on 09-02-2015.
 */
public class GsonRequest<T> extends Request<T> {

    private final Gson gson = new Gson();
    private final Class<T> clazz;
    private final Map<String, String> headers;
    private final Listener<T> listener;
    private final Response.ErrorListener errorListener;
    private final Map<String, String> parameters;

    /**
     * Make a request and return a parsed object from JSON.
     * @param url URL of the request to make
     * @param clazz Relevant class object, for Gson's reflection
     * @param headers Map of request headers
     * @param method Method.GET, Method.POST, Method.UPDATE, Method.DELETE etc.
     * @param parameters
     */
    public GsonRequest(String url, Class<T> clazz, Map<String, String> headers,
                       Listener<T> listener, Response.ErrorListener errorListener, int method, Map<String, String> parameters) {
        super(method, url, errorListener);
        this.clazz = clazz;
        this.headers = headers;
        this.listener = listener;
        this.parameters = parameters;
        this.errorListener = errorListener;
    }

    /**
     * Make a request and return a parsed object from JSON.
     * @param url URL of the request to make
     * @param clazz Relevant class object, for Gson's reflection
     * @param headers Map of request headers
     * @param listener on success listener
     * @param errorListener on error listener
     * @param method Method.GET, Method.POST, Method.UPDATE, Method.DELETE etc.
     * @param parameters parameters to be passed in GET or POST
     * @param sequence sequence of request in the queue
     * @param errorListener
     */
    public GsonRequest(String url, Class<T> clazz, Map<String, String> headers,
                       Listener<T> listener, Response.ErrorListener errorListener, int method, Map<String, String> parameters, int sequence, Response.ErrorListener errorListener1) {
        super(method, url, errorListener);
        this.clazz = clazz;
        this.headers = headers;
        this.listener = listener;
        this.parameters = parameters;
        this.errorListener = errorListener;
        this.setSequence(sequence);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    public void deliverError(VolleyError error) {
        if(this.errorListener != null) {
            this.errorListener.onErrorResponse(error);
        }
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            Log.d("Request<T>", "response - " + json + ", statusCode - " + response.statusCode);
            //this.setStatusCode(response.statusCode);
            return Response.success(
                    gson.fromJson(json, clazz),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }

    public Response.ErrorListener responseErrorListener() {

        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressBarLayout.setVisibility(RelativeLayout.INVISIBLE);
                //Toast.makeText(EmergencyInfo.this, "An error occurred. Please try again.", Toast.LENGTH_SHORT);

                Log.e("Alerts Error", error.getMessage() + ", ststus code: " + error.networkResponse.statusCode);
            }
        };
    }

    public RetryPolicy getTimeoutPolicy(Integer socketTimeout) {
        // Login time increased to 30 seconds
        socketTimeout = ((socketTimeout==null)? 3000 : socketTimeout);
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        return policy;
    }
}
