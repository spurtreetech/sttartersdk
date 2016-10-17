package com.spurtreetech.sttarter.lib.helper;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.spurtreetech.sttarter.lib.helper.Connection.ConnectionStatus;
import com.spurtreetech.sttarter.lib.helper.models.LoginOTPResponse;
import com.spurtreetech.sttarter.lib.helper.models.LoginResponse;
import com.spurtreetech.sttarter.lib.helper.volley.LruBitmapCache;
import com.spurtreetech.sttarter.lib.provider.STTProviderHelper;

import org.eclipse.paho.android.service.MqttAndroidClient;
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

public class STTarter {

    private static STTarter instance = null;
    private Context context;
    private String appId;
    public String clientId;
    public String userId;
    private String appSecret;
    private RequestQueue mRequestQueue;
    private String clientHandle;
    STTGeneralRoutines sttgr;
    private boolean isApplicationInBackground = true;
    private Connection connection;
    SharedPreferences sp;
    SharedPreferences.Editor spEditor;
    private MqttAndroidClient client;
    private MqttConnectOptions  conOpt;


    public static final String TAG = STTarter.class.getSimpleName();

    private ImageLoader mImageLoader;
    LruBitmapCache mLruBitmapCache;

    /**
     * {@link ChangeListener} for use with all {@link Connection} objects created by this instance of <code>ClientConnections</code>
     */
    private ChangeListener changeListener = new ChangeListener();


    public static synchronized STTarter getInstance() {

        if(STTarter.instance == null) {
            STTarter.instance = new STTarter();
        }

        return STTarter.instance;
    }

    /*
    public CursorLoader getTopicsCursor(){
        Uri uri;
        String  selection = null;

        STTProviderHelper ph = new STTProviderHelper();
        uri = Uri.parse("content://" + STTContentProvider.AUTHORITY + "/" + DatabaseHelper.TABLE_TOPICS + "/getAllTopics");
        //selection = DatabaseHelper.COLUMN_MESSAGE_TOPIC + " LIKE '%" + ph.getBuzzFeedTopic() + "%'";
        Log.d("STTarter: TOPICS", "CursorLoader initialized");
        CursorLoader cursorLoader = new CursorLoader(context, uri, null, selection, null, null);

        return  cursorLoader;
    }

    public void setCursorNotifierForTopics(CursorRecyclerAdapter adpater, Cursor cursor){
        Uri uri = Uri.parse("content://" + STTContentProvider.AUTHORITY + "/" + DatabaseHelper.TABLE_TOPICS + "/getAllTopics");
        adpater.changeCursor(cursor);
        cursor.setNotificationUri(context.getContentResolver(),uri );
    }
    */

    /**
     * @param appId STTarter application ID
     * @param appSecret secret key of the application
     * @param applicationContext context of the Application
     * @param userToken Token of the user for Authenticating in STTarter
     */
    public void init(String appId, String appSecret, String userID, String userToken,String app_Token, Context applicationContext) {

        //STTarter.getInstance();
        this.context = applicationContext;
        this.appId = appId;
        this.appSecret = appSecret;

        sp = applicationContext.getSharedPreferences(Keys.STTARTER_PREFERENCES, Context.MODE_PRIVATE);
        spEditor = sp.edit();

        Log.d("STTarter", "init data: " + appId + ", " + appSecret + ", " + userID + ", " + userToken);
        // TODO save the appId in SharedPreference and use appId (and appKey) to build requests to the server
        spEditor.putString(Keys.USER_ID, userID);
        spEditor.putString(Keys.APP_KEY, appId);
        spEditor.putString(Keys.APP_SECRET, appSecret);
        spEditor.putString(Keys.USER_TOKEN, userToken);
        spEditor.putString(Keys.AUTH_TOKEN, app_Token);
        spEditor.commit();
        //AUTH_TOKEN

        Log.d("STTarter", Keys.USER_ID + " : " + sp.getString(Keys.USER_ID, ""));

        Log.d("STTarter", "Context - " + this.context);
        sttgr = new STTGeneralRoutines();
        //sttgr.retieveNewToken(this.appId, this.appSecret);
        //sttgr.retrieveNewToken();

        this.userId = userID;


        /** client handle is Uri(host+port) + clientId **/
        this.clientId = getClientId();

        try {
            initiateConnnection();
        } catch (ContextNotInitializedException e) {
            e.printStackTrace();
        }



    }


    public void loginUserRequestForOTP(Response.Listener<LoginResponse> registerSuccessListener, Response.ErrorListener loginResponseListener,String mobileStr, String orgStr, Context applicationContext) {

        this.context = applicationContext;

        Map<String, String> params = new HashMap<String, String>();

        params.put("mobile", mobileStr);
        params.put("org_id", orgStr);

        String url = Keys.GET_OTP;
        Log.d(TAG, "GET_OTP url - " + url);

        GsonRequest<LoginResponse> myReq = new GsonRequest<LoginResponse>(
                url,
                LoginResponse.class,
                RequestQueueHelper.getHeaders(),
                registerSuccessListener,
                loginResponseListener,
                Request.Method.POST, params);

        // Login time increased to 30 seconds
        int socketTimeout = 30000;//or (30000)30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        myReq.setRetryPolicy(policy);

        RequestQueueHelper.addToRequestQueue(myReq, "");
    }


    public void confirmOTPWithServer(String otpCode,Response.Listener<LoginOTPResponse> getOtpSuccessListener, Response.ErrorListener getOTPResponseListener,String mobileStr, String orgStr, Context applicationContext) {

        this.context = applicationContext;

        Map<String, String> params = new HashMap<String, String>();
        params.put("mobile", mobileStr);
        params.put("otp", otpCode);
        params.put("org_id", orgStr);

        String url = Keys.OTP_LOGIN;
        Log.d(TAG, "OTP_LOGIN url - " + url);

        GsonRequest<LoginOTPResponse> myReq = new GsonRequest<LoginOTPResponse>(
                url,
                LoginOTPResponse.class,
                RequestQueueHelper.getHeaders(),
                getOtpSuccessListener,
                getOTPResponseListener,
                Request.Method.POST,params);

        RequestQueueHelper.addToRequestQueue(myReq, "");
    }


    /**
     * Checks if the application is being sent in the background (i.e behind
     * another application's Activity).
     *
     * @return <code>true</code> if another application will be above this one.
     */
    public static boolean isApplicationSentToBackground() {
        ActivityManager am = (ActivityManager) STTarter.getInstance().context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(STTarter.getInstance().context.getPackageName())) {
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

        PreferenceHelper.getSharedPreferenceEditor().putString(Keys.USER_ID, userId).commit();
        Log.d("STTarter", "USER_ID - " + userId);

        try {
            initiateConnnection();
        } catch (ContextNotInitializedException e) {
            e.printStackTrace();
        }
    }

    public String getClientHandle() {
        return this.clientHandle;
    }

    public Map<String, String>  getHeaders() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("x-user-token", PreferenceHelper.getSharedPreference().getString(Keys.USER_TOKEN,""));
        headers.put("x-app-token", PreferenceHelper.getSharedPreference().getString(Keys.AUTH_TOKEN,""));
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
        headers.put("app_key", STTarter.getInstance().appId);
        headers.put("app_secret", STTarter.getInstance().appSecret);
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
            Log.d("subscribeAll", "subscribed - " + PreferenceHelper.getSharedPreference().getString(Keys.SUBSCRIBED_TOPICS, ""));
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
        String subscribedString = PreferenceHelper.getSharedPreference().getString(Keys.SUBSCRIBED_TOPICS,"");
        return subscribedString.split(",");
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }
        return mRequestQueue;
    }

    public void clearNotificationString() {
        PreferenceHelper.getSharedPreferenceEditor().putString(Keys.NOTIFICATION_TOPICS,"").commit();
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
                    cursorLoader = new CursorLoader(STTarter.getInstance().getContext(), uri, null, null,null, null);
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
     * Exception when STTarter is not initialized before calling
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
    public void initiateConnnection() throws ContextNotInitializedException{

        if(!PreferenceHelper.getSharedPreference().getString(Keys.USER_ID,"").equals("")) {

            Bundle bundle = new Bundle();
            //put data into a bundle to be passed back to ClientConnections
            bundle.putString(ActivityConstants.server, Keys.SERVER_URL);
            bundle.putString(ActivityConstants.port, Keys.PORT);
            bundle.putString(ActivityConstants.clientId, clientId);
            bundle.putInt(ActivityConstants.action, ActivityConstants.connect);
            bundle.putBoolean(ActivityConstants.cleanSession, Keys.CLEAN_SESSION);

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

            connectAction(bundle);
        } else {
            throw new ContextNotInitializedException("No user id specified");
        }

    }

    public String getUserId(){

        return this.userId;
    }

    public String getClientId() {
        String deviceId = "";
        try {
            deviceId = Settings.Secure.getString(STTarter.getInstance().getContext().getContentResolver(),Settings.Secure.ANDROID_ID);
        } catch (ContextNotInitializedException e) {
            e.printStackTrace();
        }

        /** client handle is Uri(host+port) + clientId **/
        this.clientId = getAppId() + deviceId;
        return this.clientId;
    }

    public  synchronized boolean isConnectedOrConnecting() {
        if(STTarter.getInstance() == null || STTarter.getInstance().connection == null)
            return false;
        else
            return STTarter.getInstance().connection.isConnectedOrConnecting();
    }

    /**
     * Process data from the connect action
     *
     * @param data the {@link Bundle} returned by the inti function
     */
    private void connectAction(Bundle data) {
        MqttConnectOptions  conOpt = new MqttConnectOptions();
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

        MqttAndroidClient client;
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
        client.setCallback(new STTCallbackHandler(this.context, clientHandle));


        //set traceCallback
        client.setTraceCallback(new STTTraceCallback());

        connection.addConnectionOptions(conOpt);
        Connections.getInstance(this.context).addConnection(connection);
        this.client = client;
        this.conOpt = conOpt;
        if (doConnect) {
                Thread t = new Thread(){
                    public void run(){
                        try {
                            STTarter.getInstance().client.connect(STTarter.getInstance().conOpt, null, callback);
                        } catch (MqttException e) {
                            e.printStackTrace();
                        }
                    }
                };
                t.start();
        }

        /*
        Intent testServiceIntent = new Intent(STTarter.getInstance().context, TestService.class);
        try {
            STTarter.getInstance().getContext().startService(testServiceIntent);
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
                STTarter.getInstance().subscribeAll(PreferenceHelper.getSharedPreference().getString(Keys.SUBSCRIBED_TOPICS,"").split(","));
            } else if (event.getPropertyName().equals("connectionStatus")){
                Log.d("ChangeListener", "Event status : " + event.getOldValue() + " to " + event.getNewValue());
                /*
                try {
                    if(!connection.getClient().isConnected() && STTarter.getInstance().isOnline(STTarter.getInstance().getContext()))
                        STTarter.getInstance().initiateConnnection();
                } catch (ContextNotInitializedException e) {
                    e.printStackTrace();
                }
                */
            }

            /*
            try {
                Log.d("ChangeListener", "Connection property changed");
                if(Connections.getInstance(STTarter.getInstance().getContext()).getConnection(clientHandle).isConnected()) {

                    Log.d("ChangeListener", "subscribed topics - " + PreferenceHelper.getSharedPreference().getString(Keys.SUBSCRIBED_TOPICS,""));
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

    public void logout() {
        STTarter.getInstance().client.stopService();
        STTProviderHelper ph = new STTProviderHelper();
        ph.emptyAllTable();
//STTarter.getInstance().getContext().getDatabasePath(STTSQLiteOpenHelper.DATABASE_FILE_NAME).delete();
        PreferenceHelper.getSharedPreferenceEditor().clear().commit();

    }

}
