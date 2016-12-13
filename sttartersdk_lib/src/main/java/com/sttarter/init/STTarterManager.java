package com.sttarter.init;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.sttarter.common.models.User;
import com.sttarter.common.responses.AppAuthResponse;
import com.sttarter.common.responses.STTResponse;
import com.sttarter.common.responses.SignUpResponse;
import com.sttarter.common.utils.GsonRequest;
import com.sttarter.common.utils.STTarterConstants;
import com.sttarter.communicator.CommunicationManager;
import com.sttarter.helper.interfaces.STTSuccessListener;
import com.sttarter.helper.uitools.LruBitmapCache;
import com.sttarter.helper.utils.NotificationHelperListener;
import com.sttarter.init.Connection.ConnectionStatus;
import com.sttarter.provider.STTProviderHelper;
import com.sttarter.referral.ReferralManager;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.android.service.MqttService;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class STTarterManager {

    private Context context;

    private String appId;
    private String appSecret;
    private STTarterConstants.AuthType authType;
    public String userId;

    private static STTarterManager instance = null;
    public String clientId;
    private RequestQueue mRequestQueue;
    private String clientHandle;
    CommunicationManager sttgr;
    private boolean isApplicationInBackground = true;
    private Connection connection;
    SharedPreferences sp;
    SharedPreferences.Editor spEditor;
    private MqttAndroidClient client;
    private MqttConnectOptions conOpt;


    public static final String TAG = STTarterManager.class.getSimpleName();

    private ImageLoader mImageLoader;
    LruBitmapCache mLruBitmapCache;

    /**
     * {@link ChangeListener} for use with all {@link Connection} objects created by this instance of <code>ClientConnections</code>
     */
    private ChangeListener changeListener = new ChangeListener();

    NotificationHelperListener notificationHelperListener;


    public static synchronized STTarterManager getInstance() {

        if(STTarterManager.instance == null) {
            STTarterManager.instance = new STTarterManager();
        }

        return STTarterManager.instance;
    }


    public void AuthenticateApp(Context context, String appkey, String appSecret, STTSuccessListener STTSuccessListener, Response.ErrorListener getAuthResponseListener) {
        String url = STTKeys.AUTH;
        Log.d(TAG, "AUTH url - " + url);

        this.context = context;

        Map<String, String> params = new HashMap<String, String>();
        params.put("app_key", appkey);
        params.put("app_secret", appSecret);

        sp = context.getSharedPreferences(STTKeys.STTARTER_PREFERENCES, Context.MODE_PRIVATE);
        spEditor = sp.edit();

        // TODO save the APP_KEY in SharedPreference and APP_SECRET to build requests to the server

        spEditor.putString(STTKeys.APP_KEY, appkey);
        spEditor.putString(STTKeys.APP_SECRET, appSecret);
        spEditor.commit();

        GsonRequest<AppAuthResponse> myReq = new GsonRequest<AppAuthResponse>(
                url,
                AppAuthResponse.class,
                RequestQueueHelper.getHeaders(),
                getAuthSuccessListener(context, STTSuccessListener),
                getAuthResponseListener,
                Request.Method.POST, params);

        int socketTimeout = 30000;//or (30000)30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(2*socketTimeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        myReq.setRetryPolicy(policy);

        RequestQueueHelper.addToRequestQueue(myReq, "");
    }


    public void init(Context ctx, String appKey, String appSecret, STTarterConstants.AuthType authType){

        this.context = ctx;

        // If first time, then we are just initiatlizing
        // Else we should ensure all services are running.
        boolean isCurrentlyLoggedIn = true;
        if (!isUserAuthenticated(ctx)){
            this.appId = appKey;
            this.appSecret = appSecret;
            this.authType = authType;

            sp = ctx.getSharedPreferences(STTKeys.STTARTER_PREFERENCES, Context.MODE_PRIVATE);
            spEditor = sp.edit();

            // TODO save the appId in SharedPreference and use appId (and appKey) to build requests to the server
            spEditor.putString(STTKeys.APP_KEY, appId);
            spEditor.putString(STTKeys.APP_SECRET, appSecret);
            spEditor.putString(STTKeys.APP_AUTHTYPE, STTarterConstants.getAuthTypeStorageString(authType));
            spEditor.commit();
        }else{
            initializeSTTarterServices();
        }
    }

    public boolean isUserAuthenticated(Context context) {
        this.context = context;
        if (context!=null) {
            sp = context.getSharedPreferences(STTKeys.STTARTER_PREFERENCES, Context.MODE_PRIVATE);
            return !TextUtils.isEmpty(sp.getString(STTKeys.USER_TOKEN,""));
        }
        else {
            return false;
        }
    }

    private void initializeSTTarterServices(){
        context.startService(new Intent(context, MqttService.class));
    }

    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * @param applicationContext context of the Application
     */
    public void init(Context applicationContext, NotificationHelperListener notificationHelperListener) {

        //STTarterManager.getInstance();
        this.context = applicationContext;
        this.notificationHelperListener = notificationHelperListener;

        /** client handle is Uri(host+port) + clientId **/
        this.clientId = getClientId();

        try {
            initiateConnnection(notificationHelperListener);
        } catch (ContextNotInitializedException e) {
            e.printStackTrace();
        }

    }

    public void loginwithAccount(Context context, String username, String password, STTSuccessListener STTSuccessListener, Response.ErrorListener getLoginResponseListener) {
        String url = STTKeys.LOGIN;
        Log.d(TAG, "Login url - " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("password", password);

        GsonRequest<User> myReq = new GsonRequest<User>(
                url,
                User.class,
                getHeaders(),
                getLoginSuccessListener(context, STTSuccessListener),
                getLoginResponseListener,
                Request.Method.POST, params);

        int socketTimeout = 30000;//or (30000)30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(2*socketTimeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        myReq.setRetryPolicy(policy);

        RequestQueueHelper.addToRequestQueue(myReq, "");
    }



    private Response.Listener<User> getLoginSuccessListener(final Context applicationContext, final STTSuccessListener STTSuccessListener) {

        return new Response.Listener<User>() {
            @Override
            public void onResponse(final User loginResponse) {

                if (loginResponse.getUser_token() != null){

                    sp = applicationContext.getSharedPreferences(STTKeys.STTARTER_PREFERENCES, Context.MODE_PRIVATE);
                    spEditor = sp.edit();

                    // TODO save the appToken in SharedPreference and userToken to build requests to the server

                    spEditor.putString(STTKeys.USER_ID,loginResponse.getUsername());
                    spEditor.putString(STTKeys.USER_NAME,loginResponse.getName());
                    spEditor.putString(STTKeys.USER_EMAIL,loginResponse.getEmail());
                    spEditor.putString(STTKeys.USER_PHONE,loginResponse.getMobile());
                    spEditor.putString(STTKeys.USER_TOKEN,loginResponse.getUser_token());
                    spEditor.commit();

                    STTResponse sTTResponse = new STTResponse();
                    sTTResponse.setStatus(200);
                    STTSuccessListener.Response(sTTResponse);
                }

            }
        };
    }

    public void loginUserRequestForOTP(Context applicationContext, STTSuccessListener sttSuccessListener, Response.ErrorListener loginResponseListener, String mobileStr, String orgStr) {

        this.context = applicationContext;
        HashMap params = new HashMap();
        params.put("mobile", mobileStr);
        params.put("org_id", orgStr);
        String url = STTKeys.GET_OTP;
        GsonRequest myReq = new GsonRequest(url, User.class, RequestQueueHelper.getHeaders(), getLoginSuccessListener(context, sttSuccessListener), loginResponseListener, Request.Method.POST, params);
        short socketTimeout = 30000;
        DefaultRetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 0, 1.0F);
        myReq.setRetryPolicy(policy);
        RequestQueueHelper.addToRequestQueue(myReq, "");
    }

    public void confirmOTPWithServer(Context applicationContext, String otpCode, STTSuccessListener sttSuccessListener, Response.ErrorListener getOTPResponseListener, String mobileStr, String orgStr) {
        this.context = applicationContext;
        HashMap params = new HashMap();
        params.put("mobile", mobileStr);
        params.put("otp", otpCode);
        params.put("org_id", orgStr);
        String url = STTKeys.OTP_LOGIN;
        GsonRequest myReq = new GsonRequest(url, User.class, RequestQueueHelper.getHeaders(), getLoginSuccessListener(context, sttSuccessListener), getOTPResponseListener, Request.Method.POST, params);
        short socketTimeout = 30000;
        DefaultRetryPolicy policy = new DefaultRetryPolicy(2 * socketTimeout, 0, 1.0F);
        myReq.setRetryPolicy(policy);
        RequestQueueHelper.addToRequestQueue(myReq, "");
    }

    public void quickLogin(Context applicationContext, Response.Listener<STTResponse> getSuccessListener, Response.ErrorListener getErrorListener, String name, String mobileStr, String emailStr, String orgStr) {
        this.context = applicationContext;
        HashMap params = new HashMap();
        params.put("name", name);
        params.put("mobile", mobileStr);
        params.put("email", emailStr);
        params.put("org_id", orgStr);
        String url = STTKeys.QUICK_LOGIN;
        GsonRequest myReq = new GsonRequest(url, STTResponse.class, RequestQueueHelper.getHeaders(), getSuccessListener, getErrorListener, Request.Method.POST, params);
        short socketTimeout = 30000;
        DefaultRetryPolicy policy = new DefaultRetryPolicy(2 * socketTimeout, 0, 1.0F);
        myReq.setRetryPolicy(policy);
        RequestQueueHelper.addToRequestQueue(myReq, "");
    }

    private Response.Listener<AppAuthResponse> getAuthSuccessListener(final Context applicationContext, final STTSuccessListener STTSuccessListener) {

        return new Response.Listener<AppAuthResponse>() {
            @Override
            public void onResponse(final AppAuthResponse appAuthResponse) {

                if (appAuthResponse.getToken() != null){

                    sp = applicationContext.getSharedPreferences(STTKeys.STTARTER_PREFERENCES, Context.MODE_PRIVATE);
                    spEditor = sp.edit();

                    // TODO save the appToken in SharedPreference and userToken to build requests to the server

                    spEditor.putString(STTKeys.AUTH_TOKEN, appAuthResponse.getToken());
                    spEditor.commit();

                    STTResponse STTResponse = appAuthResponse;
                    STTSuccessListener.Response(STTResponse);
                }

            }
        };
    }



    public void signUp(Context context, User signUpModel, String referralCode, STTSuccessListener STTSuccessListener, Response.ErrorListener getSignUpErrorListener) {
        String url = STTKeys.SIGNUP;
        Log.d(TAG, "SignUp url - " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put("name", signUpModel.getName());
        params.put("email", signUpModel.getEmail());
        params.put("mobile", signUpModel.getMobile());
        params.put("username", signUpModel.getUsername());
        params.put("password", signUpModel.getPassword());
        if (signUpModel.getMeta()!=null)
            params.put("meta", signUpModel.getMeta());
        if (signUpModel.getAvatar()!=null)
            params.put("avatar", signUpModel.getAvatar());

        GsonRequest<SignUpResponse> myReq = new GsonRequest<SignUpResponse>(
                url,
                SignUpResponse.class,
                getHeaders(),
                getSignUpSuccessListener(context,referralCode, STTSuccessListener,getSignUpErrorListener),
                getSignUpErrorListener,
                Request.Method.POST, params);

        int socketTimeout = 30000; //or (30000)30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(2*socketTimeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        myReq.setRetryPolicy(policy);

        RequestQueueHelper.addToRequestQueue(myReq, "");
    }

    private Response.Listener<SignUpResponse> getSignUpSuccessListener(final Context applicationContext, final String referralCode, final STTSuccessListener sTTSuccessListener, final Response.ErrorListener getSignUpErrorListener) {

        return new Response.Listener<SignUpResponse>() {
            @Override
            public void onResponse(final SignUpResponse responseFromSignUp) {
                //If receiving the user token, save it into initializing user.
                Log.d("SignUpResponse",responseFromSignUp.toString());
                sp = applicationContext.getSharedPreferences(STTKeys.STTARTER_PREFERENCES,Context.MODE_PRIVATE);
                spEditor = sp.edit();
                spEditor.putString(STTKeys.USER_ID,responseFromSignUp.getUser().getUsername()+"");
                spEditor.putString(STTKeys.USER_NAME,responseFromSignUp.getUser().getName());
                spEditor.putString(STTKeys.USER_EMAIL,responseFromSignUp.getUser().getEmail());
                spEditor.putString(STTKeys.USER_PHONE,responseFromSignUp.getUser().getMobile());
                spEditor.putString(STTKeys.USER_TOKEN,responseFromSignUp.getUser().getUser_token());
                spEditor.commit();

                ReferralManager.getInstance().signup(responseFromSignUp.getUser().getUsername(),responseFromSignUp.getUser().getName(),responseFromSignUp.getUser().getEmail(),responseFromSignUp.getUser().getMobile(),referralCode,sTTSuccessListener,getSignUpErrorListener);

            }
        };
    }

    /**
     * Checks if the application is being sent in the background (i.e behind
     * another application's Activity).
     *
     * @return <code>true</code> if another application will be above this one.
     */
    public static boolean isApplicationSentToBackground() {
        ActivityManager am = (ActivityManager) STTarterManager.getInstance().context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(STTarterManager.getInstance().context.getPackageName())) {
                return true;
            }
        }

        return false;
    }

    public boolean isApplicationInBackground() {
        return isApplicationInBackground;
    }

    public void setApplicationInBackground(boolean isApplicationInBackground) {
        this.isApplicationInBackground = isApplicationInBackground;
    }

    public void user(String userId) {

        PreferenceHelper.getSharedPreferenceEditor().putString(STTKeys.USER_ID, userId).commit();
        Log.d("STTarterManager", "USER_ID - " + userId);

        try {
            initiateConnnection(notificationHelperListener);
        } catch (ContextNotInitializedException e) {
            e.printStackTrace();
        }
    }

    public String getClientHandle() {
        return this.clientHandle;
    }

    protected Map<String, String>  getHeaders() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("x-user-token", PreferenceHelper.getSharedPreference().getString(STTKeys.USER_TOKEN,""));
        headers.put("x-app-token", PreferenceHelper.getSharedPreference().getString(STTKeys.AUTH_TOKEN,""));
        //headers.put("X-App-Token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE0MzU0MDQ1MzQ4NzQsImFwcCI6eyJuYW1lIjoidGVzdCBhcHAiLCJwZXJtaXNzaW9ucyI6IjEsMiwzLDQiLCJhcHBfa2V5IjoiZTc0MTkwZGZkYjFmNDkwZDNkYjBkNzJiOTQ1YzY3YmEifX0.ivHRsje-Rk0k1_msPjhRfk6H57OwBDXj4uzxaSWxv4c");
        Iterator it = headers.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            Log.d(this.getClass().getCanonicalName(), "params - " + pairs.getKey() + ", " + pairs.getValue());
        }
        return headers;
    }

    public String  getAppId() {
        return this.appId;
    }

    public String  getAppSecret() {
        return this.appSecret;
    }

    public Map<String, String>  getAppParams() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("app_key", STTarterManager.getInstance().appId);
        headers.put("app_secret", STTarterManager.getInstance().appSecret);
        return headers;
    }

    public void subscribeAll(String[] topics) {
        int[] qos = new int[topics.length];
        for(int i=0; i<topics.length; i++) {
            qos[i] = ActivityConstants.defaultQos;
        }

        try {
            Connections.getInstance(context).getConnection(clientHandle).getClient()
                    .subscribe(topics, qos, null, new ActionListener(context, ActionListener.Action.SUBSCRIBE, clientHandle, topics));
            Log.d("subscribeAll", "subscribed - " + PreferenceHelper.getSharedPreference().getString(STTKeys.SUBSCRIBED_TOPICS, ""));
        }
        catch (MqttSecurityException e) {
            Log.e(this.getClass().getCanonicalName(), "Failed to subscribe to" + topics.length + " topics, the client with the handle " + clientHandle, e);
        }
        catch (MqttException e) {
            Log.e(this.getClass().getCanonicalName(), "Failed to subscribe to" + topics.length + " topics, the client with the handle " + clientHandle, e);
        }
    }

    public void subscribe(String topic) {
        int qos = ActivityConstants.defaultQos;
        Log.d("subscribe", "clientHandle: " + clientHandle);
        try {
            Connections.getInstance(context).getConnection(clientHandle).getClient()
                    .subscribe(topic, qos, null, new ActionListener(context, ActionListener.Action.SUBSCRIBE, clientHandle, topic));
            Log.d("subscribe", "subscribed - " + topic);
        }
        catch (MqttSecurityException e) {
            Log.e(this.getClass().getCanonicalName(), "Failed to subscribe to" + topic + " the client with the handle " + clientHandle, e);
        }
        catch (MqttException e) {
            Log.e(this.getClass().getCanonicalName(), "Failed to subscribe to" + topic + " the client with the handle " + clientHandle, e);
        }
    }

    public void unsubscribe(String topic) {
        try {
            Connections.getInstance(context).getConnection(clientHandle).getClient()
                    .unsubscribe(topic, null, new ActionListener(context, ActionListener.Action.UNSUBSCRIBE, clientHandle));
        }
        catch (MqttSecurityException e) {
            Log.e(this.getClass().getCanonicalName(), "Failed to subscribe to" + topic + " the client with the handle " + clientHandle, e);
        }
        catch (MqttException e) {
            Log.e(this.getClass().getCanonicalName(), "Failed to subscribe to" + topic + " the client with the handle " + clientHandle, e);
        }
    }

    public void unsubscribe(String[] topics) {
        try {
            Connections.getInstance(context).getConnection(clientHandle).getClient()
                    .unsubscribe(topics, null, new ActionListener(context, ActionListener.Action.UNSUBSCRIBE, clientHandle));
        }
        catch (MqttSecurityException e) {
            Log.e(this.getClass().getCanonicalName(), "Failed to unsubscribe to" + topics.length + " topics, the client with the handle " + clientHandle, e);
        }
        catch (MqttException e) {
            Log.e(this.getClass().getCanonicalName(), "Failed to unsubscribe to" + topics.length + " topics, the client with the handle " + clientHandle, e);
        }
    }

    /*public void initChat(String requestingUser, String requestedUser) {
        sttgr.initChat(requestingUser, requestedUser);
    }*/

    public String[] getSubscribedTopics() {
        String subscribedString = PreferenceHelper.getSharedPreference().getString(STTKeys.SUBSCRIBED_TOPICS,"");
        return subscribedString.split(",");
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }
        return mRequestQueue;
    }

    public void clearNotificationString() {
        PreferenceHelper.getSharedPreferenceEditor().putString(STTKeys.NOTIFICATION_TOPICS,"").commit();
    }

    /*
    public void updateAdapterOnChange(final CursorRecyclerAdapter<RecyclerView.ViewHolder> ca) {
        new LoaderManager.LoaderCallbacks<Cursor>() {

            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                Uri uri;
                switch (id) {
                    case 0:
                        uri = STTContentProvider.MESSAGES_URI;
                        break;
                    default:
                        uri = null;
                }
                CursorLoader cursorLoader = null;
                try {
                    cursorLoader = new CursorLoader(STTarterManager.getInstance().getContext(), uri, null, null,null, null);
                } catch (ContextNotInitializedException e) {
                    e.printStackTrace();
                }
                return cursorLoader;
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                switch (loader.getId()) {
                    case 0:
                        data.setNotificationUri(
                                this.getContentResolver(),
                                STTContentProvider.MESSAGES_URI);
                        ca.changeCursor(data);
                        ca.notifyDataSetChanged();
                        chatRecyclerView.scrollToPosition(ca.getItemCount()-1);
                    default:
                        break;
                }
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {

            }
        };
    }
    */

    /**
     * @return Context
     * @throws ContextNotInitializedException
     */
    public Context getContext() throws ContextNotInitializedException {
        if(this.context == null) {
            throw new ContextNotInitializedException();
        } else {
            //Log.d(this.getClass().getSimpleName(), "Context returned: " + this.context);
            return this.context;
        }
    }

    public boolean isOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            Log.d("ConDetect", "connectivity manager" + cm.toString());
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            Log.d("ConDetect", "NetworkInfo" + cm.toString());
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                Log.d("ConDetect", "boolean value : " + netInfo.isConnectedOrConnecting());
                return true;
            }
        } catch (Exception e) {
            for (StackTraceElement tempStack : e.getStackTrace()) {
                // Log.d("Exception thrown: Treeview Fetch", "" +
                // tempStack.getLineNumber());
                Log.d("Exception thrown: ",
                        "" + tempStack.getLineNumber() + " methodName: " + tempStack.getClassName() + "-"
                                + tempStack.getMethodName());
            }
        }
        return false;
    }


    /**
     * Exception when STTarterManager is not initialized before calling
     */
    public class ContextNotInitializedException extends Exception{
        // TODO log a message to say that context was null
        public ContextNotInitializedException(){}

        public ContextNotInitializedException(String detailMessage){
            super(detailMessage);
        }

        public ContextNotInitializedException(Throwable throwable) {
            super(throwable);
        }

        public ContextNotInitializedException(String detailMessage, Throwable throwable) {
            super(detailMessage, throwable);
        }
    }

    /**
     * initialize connection to the server
     */
    public void initiateConnnection(NotificationHelperListener notificationHelperListener) throws ContextNotInitializedException{

        if(!PreferenceHelper.getSharedPreference().getString(STTKeys.USER_ID,"").equals("")) {

            Bundle bundle = new Bundle();
            //put data into a bundle to be passed back to ClientConnections
            bundle.putString(ActivityConstants.server, STTKeys.SERVER_URL);
            bundle.putString(ActivityConstants.port, STTKeys.PORT);
            bundle.putString(ActivityConstants.clientId, clientId);
            bundle.putInt(ActivityConstants.action, ActivityConstants.connect);
            bundle.putBoolean(ActivityConstants.cleanSession, STTKeys.CLEAN_SESSION);

            bundle.putString(ActivityConstants.message,
                    ActivityConstants.empty);
            bundle.putString(ActivityConstants.topic, ActivityConstants.empty);
            bundle.putInt(ActivityConstants.qos, ActivityConstants.defaultQos);
            bundle.putBoolean(ActivityConstants.retained,
                    ActivityConstants.defaultRetained);

            bundle.putString(ActivityConstants.username,
                    ActivityConstants.empty);
            bundle.putString(ActivityConstants.password,
                    ActivityConstants.empty);

            bundle.putInt(ActivityConstants.timeout,
                    ActivityConstants.defaultTimeOut);
            bundle.putInt(ActivityConstants.keepalive,
                    ActivityConstants.defaultKeepAlive);
            bundle.putBoolean(ActivityConstants.ssl,
                    ActivityConstants.defaultSsl);

            connectAction(bundle,notificationHelperListener);
        } else {
            throw new ContextNotInitializedException("No user id specified");
        }

    }

    public String getUsername(){
        try {
            return PreferenceHelper.getSharedPreference().getString(STTKeys.USER_ID, "");
        }
        catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    public String getClientId() {
        String deviceId = "";
        try {
            deviceId = Settings.Secure.getString(STTarterManager.getInstance().getContext().getContentResolver(),Settings.Secure.ANDROID_ID);
        } catch (ContextNotInitializedException e) {
            e.printStackTrace();
        }

        /** client handle is Uri(host+port) + clientId **/
        this.clientId = getAppId() + deviceId;
        return this.clientId;
    }

    public  synchronized boolean isConnectedOrConnecting() {
        if(STTarterManager.getInstance() == null || STTarterManager.getInstance().connection == null)
            return false;
        else
            return STTarterManager.getInstance().connection.isConnectedOrConnecting();
    }

    /**
     * Process data from the connect action
     *
     * @param data the {@link Bundle} returned by the inti function
     */
    private void connectAction(Bundle data, NotificationHelperListener notificationHelperListener) {
        MqttConnectOptions conOpt = new MqttConnectOptions();
        //conOpt.setCleanSession(false);
        //conO
        Log.d(this.getClass().getCanonicalName(), "conOpt - " + conOpt.toString());
    /*
     * Mutal Auth connections could do something like this
     *
     *
     * SSLContext context = SSLContext.getDefault();
     * context.init({new CustomX509KeyManager()},null,null); //where CustomX509KeyManager proxies calls to keychain api
     * SSLSocketFactory factory = context.getSSLSocketFactory();
     *
     * MqttConnectOptions options = new MqttConnectOptions();
     * options.setSocketFactory(factory);
     *
     * client.connect(options);
     *
     */

        // The basic client information
        String server = (String) data.get(ActivityConstants.server);
        String clientId = (String) data.get(ActivityConstants.clientId);
        int port = Integer.parseInt((String) data.get(ActivityConstants.port));
        boolean cleanSession = (Boolean) data.get(ActivityConstants.cleanSession);

        boolean ssl = (Boolean) data.get(ActivityConstants.ssl);
        String ssl_key = (String) data.get(ActivityConstants.ssl_key);
        String uri = null;
        if (ssl) {
            Log.e("SSLConnection", "Doing an SSL Connect");
            uri = "ssl://";

        }
        else {
            uri = "tcp://";
        }

        uri = uri + server + ":" + port;

        //MqttAndroidClient client;
        client = Connections.getInstance(this.context).createClient(this.context, uri, clientId);

        Log.d(getClass().getSimpleName(), "Client: " + client);

        if (ssl){
            try {
                if(ssl_key != null && !ssl_key.equalsIgnoreCase(""))
                {
                    FileInputStream key = new FileInputStream(ssl_key);
                    conOpt.setSocketFactory(client.getSSLSocketFactory(key,
                            "STTtest"));
                }
            } catch (MqttSecurityException e) {
                Log.e(this.getClass().getCanonicalName(),
                        "MqttException Occured: ", e);
            } catch (FileNotFoundException e) {
                Log.e(this.getClass().getCanonicalName(),
                        "MqttException Occured: SSL Key file not found", e);
            }
        }

        // create a client handle
        clientHandle = uri + clientId;

        Log.d(getClass().getName(), "clientHandle: " + clientHandle);

        // last will message
        String message = (String) data.get(ActivityConstants.message);
        String topic = (String) data.get(ActivityConstants.topic);
        Integer qos = (Integer) data.get(ActivityConstants.qos);
        Boolean retained = (Boolean) data.get(ActivityConstants.retained);

        // connection options

        String username = (String) data.get(ActivityConstants.username);

        String password = (String) data.get(ActivityConstants.password);

        int timeout = (Integer) data.get(ActivityConstants.timeout);
        int keepalive = (Integer) data.get(ActivityConstants.keepalive);

        connection = new Connection(clientHandle, clientId, server, port, context, client, ssl);
        //arrayAdapter.add(connection);

        connection.registerChangeListener(changeListener);
        // connect client

        String[] actionArgs = new String[1];
        actionArgs[0] = clientId;
        connection.changeConnectionStatus(ConnectionStatus.CONNECTING);

        conOpt.setCleanSession(cleanSession);
        conOpt.setConnectionTimeout(timeout);
        Log.d(this.getClass().getCanonicalName(), "conOpt - " + conOpt.toString());
        conOpt.setKeepAliveInterval(keepalive);
        if (!username.equals(ActivityConstants.empty)) {
            conOpt.setUserName(username);
        }
        if (!password.equals(ActivityConstants.empty)) {
            conOpt.setPassword(password.toCharArray());
        }

        final ActionListener callback = new ActionListener(context,
                ActionListener.Action.CONNECT, clientHandle, actionArgs);

        boolean doConnect = true;

        if ((!message.equals(ActivityConstants.empty))
                || (!topic.equals(ActivityConstants.empty))) {
            // need to make a message since last will is set
            try {
                conOpt.setWill(topic, message.getBytes(), qos.intValue(),
                        retained.booleanValue());
            }
            catch (Exception e) {
                Log.e(this.getClass().getCanonicalName(), "Exception Occured", e);
                doConnect = false;
                callback.onFailure(null, e);
            }
        }

        // whenever a new message arrives or connection is lost
        client.setCallback(new STTCallbackHandler(this.context, clientHandle,notificationHelperListener));


        //set traceCallback
        client.setTraceCallback(new STTTraceCallback());

        connection.addConnectionOptions(conOpt);
        Connections.getInstance(this.context).addConnection(connection);
        //this.client = client;
        this.conOpt = conOpt;
        if (doConnect) {
                Thread t = new Thread(){
                    public void run(){
                        try {
                            STTarterManager.getInstance().client.connect(STTarterManager.getInstance().conOpt, null, callback);
                        } catch (MqttException e) {
                            e.printStackTrace();
                        }
                    }
                };
                t.start();
        }

        /*
        Intent testServiceIntent = new Intent(STTarterManager.getInstance().context, TestService.class);
        try {
            STTarterManager.getInstance().getContext().startService(testServiceIntent);
        } catch (ContextNotInitializedException e) {
            e.printStackTrace();
        }
        */

    }



    /**
     * This class ensures that the user interface is updated as the Connection objects change their states
     *
     *
     */
    private class ChangeListener implements PropertyChangeListener {

        /**
         * @see PropertyChangeListener#propertyChange(PropertyChangeEvent)
         */
        @Override
        public void propertyChange(PropertyChangeEvent event) {

            if (!event.getPropertyName().equals(ActivityConstants.ConnectionStatusProperty)) {
                return;
            }

            if(event.getNewValue()!=null && event.getNewValue().toString().equals("Client Connected")) {
                Log.d("ChangeListener", "ChangeListener subscribeAll()");
                STTarterManager.getInstance().subscribeAll(PreferenceHelper.getSharedPreference().getString(STTKeys.SUBSCRIBED_TOPICS,"").split(","));
            } else if (event.getPropertyName().equals("connectionStatus")){
                Log.d("ChangeListener", "Event status : " + event.getOldValue() + " to " + event.getNewValue());
                /*
                try {
                    if(!connection.getClient().isConnected() && STTarterManager.getInstance().isOnline(STTarterManager.getInstance().getContext()))
                        STTarterManager.getInstance().initiateConnnection();
                } catch (ContextNotInitializedException e) {
                    e.printStackTrace();
                }
                */
            }

            /*
            try {
                Log.d("ChangeListener", "Connection property changed");
                if(Connections.getInstance(STTarterManager.getInstance().getContext()).getConnection(clientHandle).isConnected()) {

                    Log.d("ChangeListener", "subscribed topics - " + PreferenceHelper.getSharedPreference().getString(STTKeys.SUBSCRIBED_TOPICS,""));
                }
            } catch (ContextNotInitializedException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            */
            /*
            clientConnections.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    clientConnections.arrayAdapter.notifyDataSetChanged();
                }

            });
            */
        }

    }


    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            getLruBitmapCache();
            mImageLoader = new ImageLoader(this.mRequestQueue, mLruBitmapCache);
        }

        return this.mImageLoader;
    }

    public LruBitmapCache getLruBitmapCache() {
        if (mLruBitmapCache == null)
            mLruBitmapCache = new LruBitmapCache();
        return this.mLruBitmapCache;
    }

    public void logout(Context context) {
        this.context = context;
        try {
            if (client!=null && client.isConnected())
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
        STTProviderHelper ph = new STTProviderHelper();
        ph.emptyAllTable();
        //STTarterManager.getInstance().getContext().getDatabasePath(STTSQLiteOpenHelper.DATABASE_FILE_NAME).delete();
        sp = context.getSharedPreferences(STTKeys.STTARTER_PREFERENCES, Context.MODE_PRIVATE);
        spEditor = sp.edit();
        spEditor.clear().commit();

    }

}
