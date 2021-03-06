package com.sttarter.communicator;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteConstraintException;
import android.net.http.AndroidHttpClient;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.sttarter.common.models.PayloadData;
import com.sttarter.common.utils.CustomHurlStack;
import com.sttarter.common.utils.OkHttpHurlStack;
import com.sttarter.communicator.models.GroupMeta;
import com.sttarter.helper.interfaces.STTSuccessListener;
import com.sttarter.init.Connections;
import com.sttarter.common.utils.GsonRequest;
import com.sttarter.init.PreferenceHelper;
import com.sttarter.init.RequestQueueHelper;
import com.sttarter.init.STTKeys;
import com.sttarter.init.STTarterManager;
import com.sttarter.communicator.models.AllTopicsInfo;
import com.sttarter.common.models.UserList;
import com.sttarter.common.models.AuthInfo;
import com.sttarter.common.responses.STTResponse;
import com.sttarter.communicator.models.MyTopicsInfo;
import com.sttarter.communicator.models.SubscribeInfo;
import com.sttarter.communicator.models.Group;
import com.sttarter.provider.STTProviderHelper;
import com.sttarter.provider.messages.MessagesCursor;
import com.sttarter.provider.topics.TopicsCursor;
import com.sttarter.unknown.BuzzArrayListModel;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by Shahbaz on 14-11-2016.
 */

public class CommunicationManager {

    private static CommunicationManager instance = null;
    private static final String TAG = "STTGeneralRoutines";
    //SharedPreferences sp = PreferenceHelper.getSharedPreference();
    //SharedPreferences.Editor spEditor = PreferenceHelper.getSharedPreferenceEditor();

    public static final String GET_ALL_TOPICS = "getAllTopics";
    public static final String RETRIEVE_NEW_TOKEN = "retieveNewToken";

    public static synchronized CommunicationManager getInstance() {

        if(CommunicationManager.instance == null) {
            CommunicationManager.instance = new CommunicationManager();
        }

        return CommunicationManager.instance;
    }

    public void subscribeInitalize() {

        // TODO unsubscribe all topics
        // get all subscribed topics
        //String[] topics = PreferenceHelper.getSharedPreference().getString(STTKeys.SUBSCRIBED_TOPICS,"").split(",");
        //STTarterManager.getInstance().unsubscribe(topics);

        ActivityManager.MemoryInfo memoryInfo = getAvailableMemory();
        if (memoryInfo!=null) {
            if (!memoryInfo.lowMemory || !STTarterManager.getInstance().isApplicationInBackground()) {

                // TODO get all topics from server and subscribe to all of them
                Date dateTimeNow = Calendar.getInstance().getTime();
                long unixTime = dateTimeNow.getTime() / 1000L;
                long MAX_DURATION = MILLISECONDS.convert(15, SECONDS);
                Date time = new Date(PreferenceHelper.getSharedPreference().getLong(STTKeys.CHECK_DIFF, 0) * 1000);

                long duration = dateTimeNow.getTime() - time.getTime();

                if (duration >= MAX_DURATION) {
                    if (PreferenceHelper.getSharedPreference() != null) {
                        // TODO get all topics from server and subscribe to all of them
                        PreferenceHelper.getSharedPreferenceEditor().putLong(STTKeys.CHECK_DIFF, unixTime).commit();
                        getAllTopics();
                    }
                }
            }
        }
    }

    public void refreshTopics(){
        getMyTopics(null,null);
    }

    protected void getMyTopics(STTSuccessListener sttSuccessListener, STTResponse response) {

        Map<String, String> params = new HashMap<String, String>();

        Iterator it = params.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            Log.d(this.getClass().getCanonicalName(), "params - " + pairs.getKey() + ", " + pairs.getValue());
        }

        String url = STTKeys.MY_TOPICS + "/" ;//+ PreferenceHelper.getSharedPreference().getString(STTKeys.USER_ID, "");

        Log.d(TAG, "MyTopics url - " + url);

        GsonRequest<MyTopicsInfo> myReq = new GsonRequest<MyTopicsInfo>(
                url,
                MyTopicsInfo.class,
                getHeaders(),
                getMyTopicsSuccessListener(sttSuccessListener,response),
                RequestQueueHelper.responseErrorListener(),
                Request.Method.GET, params);

        int socketTimeout = 30000;//or (30000)30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(2*socketTimeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        myReq.setRetryPolicy(policy);

        RequestQueueHelper.addToRequestQueue(myReq, "");

    }

    private Response.Listener<MyTopicsInfo> getMyTopicsSuccessListener(final STTSuccessListener sttSuccessListener, final STTResponse sttResponse) {

        return new Response.Listener<MyTopicsInfo>() {
            @Override
            public void onResponse(MyTopicsInfo response) {
                try{
                    String subscribedTopics = "";
                    boolean clientConnected = false;
                    try {
                        clientConnected = Connections.getInstance(STTarterManager.getInstance().getContext()).getConnection(STTarterManager.getInstance().getClientHandle()).isConnected();
                    } catch (STTarterManager.ContextNotInitializedException e) {
                        e.printStackTrace();
                    }

                    for(Group tempGroup : response.getTopics()) {
                        // TODO subscribe all topics
                        Log.d("getMyTopics()", tempGroup.getTopic());
                        //STTarterManager.getInstance().subscribe(tempGroup.getTopic());
                        Log.d(getClass().getCanonicalName(), "subscribed to - " + tempGroup.getTopic());
                        if(clientConnected) {
                            STTarterManager.getInstance().subscribe(tempGroup.getTopic());
                        }

                        if(!tempGroup.getType().equals("master")) {
                            if(subscribedTopics.equals("")) {subscribedTopics = tempGroup.getTopic();}
                            else {subscribedTopics += ","+ tempGroup.getTopic();}
                        }

                        if (tempGroup.getType().contains("org")){
                            SharedPreferences sp = null;
                            try {
                                sp = STTarterManager.getInstance().getContext().getSharedPreferences(STTKeys.STTARTER_PREFERENCES, Context.MODE_PRIVATE);
                                SharedPreferences.Editor spEditor = sp.edit();

                                spEditor.putString(STTKeys.BUZZ_TOPIC, tempGroup.getTopic());
                                Log.d("Buzz_Organization", tempGroup.getTopic());

                                spEditor.commit();

                            } catch (STTarterManager.ContextNotInitializedException e) {
                                e.printStackTrace();
                            }

                        }

                    }

                    final MyTopicsInfo myTopicsResponse = response;

                    try {
                        //ph.deleteAllTopics();
                        Thread insertThread = new Thread() {
                            public void run() {
                                STTProviderHelper ph = new STTProviderHelper();
                                //ph.deleteAllTopics();
                                ph.insertTopics(myTopicsResponse.getTopics(), true);
                            }
                        };
                        insertThread.start();
                    } catch (SQLiteConstraintException e) {
                        e.printStackTrace();
                    }

                    // store the subscribed topics as a string in shared preferences
                    //if(PreferenceHelper.getSharedPreference().getString(STTKeys.SUBSCRIBED_TOPICS,"").equals("")) {
                    PreferenceHelper.getSharedPreferenceEditor().putString(STTKeys.SUBSCRIBED_TOPICS, subscribedTopics);
                    PreferenceHelper.getSharedPreferenceEditor().commit();
                    //}
                    getAllUsers();

                    if (sttSuccessListener!=null){
                        sttSuccessListener.Response(sttResponse);
                    }

                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
    }

    protected void getAllTopics() {

        Map<String, String> params = new HashMap<String, String>();

        Iterator it = params.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            Log.d(this.getClass().getCanonicalName(), "params - " + pairs.getKey() + ", " + pairs.getValue());
        }

        String url = STTKeys.ALL_TOPICS;   // + "/" + PreferenceHelper.getSharedPreference().getString(STTKeys.USER_ID,"");

        Log.d(TAG, "AllTopics url - " + url);

        GsonRequest<AllTopicsInfo> myReq = new GsonRequest<AllTopicsInfo>(
                url,
                AllTopicsInfo.class,
                getHeaders(),
                getAllTopicsSuccessListener(),
                RequestQueueHelper.responseErrorListener(),
                Request.Method.GET, params);

        int socketTimeout = 30000;//or (30000)30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(2*socketTimeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        myReq.setRetryPolicy(policy);

        RequestQueueHelper.addToRequestQueue(myReq, "");

    }

    private Response.Listener<AllTopicsInfo> getAllTopicsSuccessListener() {

        return new Response.Listener<AllTopicsInfo>() {
            @Override
            public void onResponse(AllTopicsInfo response) {
                try {
                    String subscribedTopics = "";
                    boolean clientConnected = false;
                    try {
                        clientConnected = Connections.getInstance(STTarterManager.getInstance().getContext()).getConnection(STTarterManager.getInstance().getClientHandle()).isConnected();
                    } catch (STTarterManager.ContextNotInitializedException e) {
                        e.printStackTrace();
                    }

                    for (Group tempGroup : response.getTopics()) {
                        // TODO subscribe all topics
                        Log.d("getAllTopics()", tempGroup.getTopic());
                        //STTarterManager.getInstance().subscribe(tempGroup.getTopic());
                        Log.d(getClass().getCanonicalName(), "subscribed to - " + tempGroup.getTopic());
                        if (clientConnected) {
                            // do not subscribe topics here
                            //STTarterManager.getInstance().subscribe(tempGroup.getTopic());
                        }

                        if (!tempGroup.getType().equals("master")) {
                            if (subscribedTopics.equals("")) {
                                subscribedTopics = tempGroup.getTopic();
                            } else {
                                subscribedTopics += "," + tempGroup.getTopic();
                            }
                        }
                    }

                    //STTProviderHelper ph = new STTProviderHelper();
                    try {
                        //ph.deleteAllTopics();
                        final AllTopicsInfo allTopicsInfo = response;
                        Thread insertThread = new Thread() {
                            public void run() {
                                STTProviderHelper ph = new STTProviderHelper();
                                ph.insertTopics(allTopicsInfo.getTopics(), false);
                            }
                        };
                        insertThread.start();

                    } catch (SQLiteConstraintException e) {
                        e.printStackTrace();
                    }

                    // store the subscribed topics as a string in shared preferences
                    //if(PreferenceHelper.getSharedPreference().getString(STTKeys.SUBSCRIBED_TOPICS,"").equals("")) {
                    PreferenceHelper.getSharedPreferenceEditor().putString(STTKeys.ALL_TOPICS_LIST, subscribedTopics);
                    PreferenceHelper.getSharedPreferenceEditor().commit();
                    //}

                    getMyTopics(null,null);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
    }


    protected void getAllUsers() {

        Map<String, String> params = new HashMap<String, String>();

        Iterator it = params.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            Log.d(this.getClass().getCanonicalName(), "params - " + pairs.getKey() + ", " + pairs.getValue());
        }

        String url = STTKeys.ALL_USERS;   // + "/" + PreferenceHelper.getSharedPreference().getString(STTKeys.USER_ID,"");

        Log.d(TAG, "AllUserss url - " + url);

        GsonRequest<UserList> myReq = new GsonRequest<UserList>(
                url,
                UserList.class,
                getHeaders(),
                getAllUsersSuccessListener(),
                RequestQueueHelper.responseErrorListener(),
                Request.Method.GET, params);

        int socketTimeout = 30000;//or (30000)30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(2*socketTimeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        myReq.setRetryPolicy(policy);

        RequestQueueHelper.addToRequestQueue(myReq, "");

    }

    private Response.Listener<UserList> getAllUsersSuccessListener() {

        return new Response.Listener<UserList>() {
            @Override
            public void onResponse(UserList response) {
                try{
                    Gson gson = new Gson();
                    String sSS = gson.toJson(response);

                    Log.d("UserInformations",sSS);

                    //STTProviderHelper ph = new STTProviderHelper();
                    try {
                        //ph.deleteAllTopics();
                        final UserList userList = response;

                        Thread insertThread = new Thread() {
                            public void run() {
                                STTProviderHelper ph = new STTProviderHelper();
                                ph.insertUsers(userList.getUsers());
                            }
                        };
                        insertThread.start();

                    } catch (SQLiteConstraintException e) {
                        e.printStackTrace();
                    }

                    // store the subscribed topics as a string in shared preferences
                    //if(PreferenceHelper.getSharedPreference().getString(STTKeys.SUBSCRIBED_TOPICS,"").equals("")) {
                /*PreferenceHelper.getSharedPreferenceEditor().putString(STTKeys.ALL_TOPICS_LIST, subscribedTopics);
                PreferenceHelper.getSharedPreferenceEditor().commit();*/
                    //}

                    //getMyTopics();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
    }


    public void getMessages(String group, Response.Listener<BuzzArrayListModel> getBuzzResponseListener, Response.ErrorListener getBuzzErrorResponseListener, Context applicationContext) {

        Map<String, String> params = new HashMap<String, String>();
        /*params.put("email", email.getEmail());
        params.put("subject", email.getSubject());
        params.put("message", email.getMessage());*/

        String url = STTKeys.BUZZ_MESSAGES+"/"+group+"/20/0";
        Log.d(TAG, "Buzz url - " + url);

        GsonRequest<BuzzArrayListModel> myReq = new GsonRequest<BuzzArrayListModel>(
                url,
                BuzzArrayListModel.class,
                getHeaders(),
                getMessagesResponseListener(),
                getBuzzErrorResponseListener,
                Request.Method.GET,params);

        int socketTimeout = 30000;//or (30000)30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(2*socketTimeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        myReq.setRetryPolicy(policy);

        RequestQueueHelper.addToRequestQueue(myReq, "");
    }

    private Response.Listener<BuzzArrayListModel> getMessagesResponseListener() {

        return new Response.Listener<BuzzArrayListModel>() {
            @Override
            public void onResponse(BuzzArrayListModel response) {

                /*Gson gson = new Gson();

                STTProviderHelper ph = new STTProviderHelper();
                PayloadData pd = gson.fromJson(new String(message.getPayload()), PayloadData.class);
                MessagesCursor messageCursor = ph.findMessage(pd.getPayload().getMessage()==null?"":pd.getPayload().getMessage(), pd.getPayload().getTopic(), Long.parseLong(pd.getTimestamp()));
                //Log.d(getClass().getSimpleName(), "cursor count : " + isMessagePresentCursor.getCount());
                if(messageCursor != null && messageCursor.getCount() > 0) {
                    messageCursor.moveToFirst();
                    //isMessagePresentCursor.close();
                    //isMessagePresentCursor.moveToFirst();
                    Log.i(this.getClass().getSimpleName(), "Message found: " + messageCursor.getMessageText());
                    //Cursor isMsgBySndrCursor = ph.isMessageBySender(pd.getPayload().getMessage(), pd.getPayload().getTopic(), pd.getTimestamp());
                    //Log.d(getClass().getSimpleName(), "Cursor count (isMsgBySender) - " + messageCursor.getCount());
                    if(messageCursor.getIsSender()) {
                        //isMsgBySndrCursor.close();
                        //isMsgBySndrCursor.moveToFirst();
                        // message is present and was by the sender so update its status as received
                        Log.i(this.getClass().getSimpleName(), "Message by sender");
                        // update the message as received status
                        if(!messageCursor.getIsDelivered())
                            ph.updateMessageSentStatus(pd);
                    } else {
                        Log.i(this.getClass().getSimpleName(), "Message not by sender");
                        // TODO update the
                        //ph.insertMessage(pd.getPayload().getMessage(), pd.getPayload().getTopic(), false, true, pd.getTimestamp());
                    }
                } else {

                    Log.i(this.getClass().getSimpleName(), "Message not found and Message not by sender");

                    if(STTarterManager.getInstance().isApplicationInBackground()) {

                        // insertMessage should have is_read field as false if application is in background
                        ph.insertMessage(pd, false, false);
                        ph.updateTopicActiveTime(pd);

                        String notificationString = PreferenceHelper.getSharedPreference().getString(STTKeys.NOTIFICATION_TOPICS,"");
                        TopicsCursor tc = ph.getTopicData(pd.getPayload().getTopic());

                        if(tc.getCount()!=0) {

                            tc.moveToFirst();
                            tm = gson.fromJson(tc.getTopicMeta(), GroupMeta.class);

                            if(notificationString.equals("")) {
                                notificationString = (tm==null || tm.getName()==null || tm.getName().equals("")) ? "Buzz" : tm.getName();
                            } else {
                                notificationString += ", " + ((tm==null || tm.getName()==null || tm.getName().equals("")) ? "Buzz" : tm.getName());
                            }

                            PreferenceHelper.getSharedPreferenceEditor().putString(STTKeys.NOTIFICATION_TOPICS, notificationString).commit();
                            this.notificationHelperListener.displayNotification(notificationString);
                            //NotificationHelper.displayNotification(notificationString);
                        }
                    } else {

                        // insertMessage should have is_read field as false if application is in background
                        ph.insertMessage(pd, false, true);
                        ph.updateTopicActiveTime(pd);
                    }
                }*/
            }
        };

    }


    public void createGroup(String groupName,String users,STTSuccessListener sttSuccessListener, Response.ErrorListener getErrorListener) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("group_id",groupName);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", groupName);
            jsonObject.put("type", "group");
            jsonObject.put("allow_reply", true);
            params.put("meta",jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        params.put("is_public","true");

        Iterator it = params.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            Log.d(this.getClass().getCanonicalName(), "params - " + pairs.getKey() + ", " + pairs.getValue());
        }

        String url = STTKeys.GROUP + "/" ;//+ PreferenceHelper.getSharedPreference().getString(STTKeys.USER_ID, "");

        GsonRequest<STTResponse> myReq = new GsonRequest<STTResponse>(
                url,
                STTResponse.class,
                getHeaders(),
                createGroupUsersSuccessListener(sttSuccessListener,groupName,users,getErrorListener),
                getErrorListener,
                Request.Method.POST, params);

        int socketTimeout = 30000;//or (30000)30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(2*socketTimeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        myReq.setRetryPolicy(policy);

        RequestQueueHelper.addToRequestQueue(myReq, "");

    }

    private Response.Listener<STTResponse> createGroupUsersSuccessListener(final STTSuccessListener sttSuccessListener, final String groupName, final String users, final Response.ErrorListener getErrorListener) {

        return new Response.Listener<STTResponse>() {
            @Override
            public void onResponse(final STTResponse response) {

                try {

                    String[] usersList = users.split(",");

                    if (usersList.length>0 && !TextUtils.isEmpty(usersList[0])) {
                        subscribeTopicWithoutCallback(false,groupName, STTarterManager.getInstance().getUsername(), sttSuccessListener, getErrorListener);
                        for (int i = 0; i < usersList.length; i++) {
                            subscribeTopicWithoutCallback(false,groupName, usersList[i], sttSuccessListener, getErrorListener);
                        }
                    }
                    else {
                        subscribeTopicWithoutCallback(false,groupName, STTarterManager.getInstance().getUsername(), sttSuccessListener, getErrorListener);
                    }

                } catch (SQLiteConstraintException e) {
                    e.printStackTrace();
                }
                finally {
                    getMyTopics(sttSuccessListener,response);
                    //sttSuccessListener.Response(response);
                }

                // store the subscribed topics as a string in shared preferences
                //if(PreferenceHelper.getSharedPreference().getString(STTKeys.SUBSCRIBED_TOPICS,"").equals("")) {
                /*PreferenceHelper.getSharedPreferenceEditor().putString(STTKeys.ALL_TOPICS_LIST, subscribedTopics);
                PreferenceHelper.getSharedPreferenceEditor().commit();*/
                //}

                //getMyTopics();
            }
        };
    }

    public void createHelpDesk(String helpDeskName,String meta,STTSuccessListener sttSuccessListener, Response.ErrorListener getErrorListener) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("query_id",helpDeskName);
        params.put("meta",meta);
        params.put("is_public","true");

        Iterator it = params.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            Log.d(this.getClass().getCanonicalName(), "params - " + pairs.getKey() + ", " + pairs.getValue());
        }

        String url = STTKeys.HELP_DESK + "/" ;//+ PreferenceHelper.getSharedPreference().getString(STTKeys.USER_ID, "");

        GsonRequest<STTResponse> myReq = new GsonRequest<STTResponse>(
                url,
                STTResponse.class,
                getHeaders(),
                createHelpDeskSuccessListener(sttSuccessListener),
                getErrorListener,
                Request.Method.POST, params);

        int socketTimeout = 30000;//or (30000)30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(2*socketTimeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        myReq.setRetryPolicy(policy);

        RequestQueueHelper.addToRequestQueue(myReq, "");

    }

    private Response.Listener<STTResponse> createHelpDeskSuccessListener(final STTSuccessListener sttSuccessListener) {

        return new Response.Listener<STTResponse>() {
            @Override
            public void onResponse(final STTResponse response) {

                try {
                    getMyTopics(sttSuccessListener,response);
                    //sttSuccessListener.Response(response);
                } catch (SQLiteConstraintException e) {
                    e.printStackTrace();
                }

                // store the subscribed topics as a string in shared preferences
                //if(PreferenceHelper.getSharedPreference().getString(STTKeys.SUBSCRIBED_TOPICS,"").equals("")) {
                /*PreferenceHelper.getSharedPreferenceEditor().putString(STTKeys.ALL_TOPICS_LIST, subscribedTopics);
                PreferenceHelper.getSharedPreferenceEditor().commit();*/
                //}

                //getMyTopics();
            }
        };
    }


    public void registerApp(String app_key,String username,String client, String manifacturer,String imei,String os,String gcmRegId) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("app_key",app_key);
        params.put("username",username);
        params.put("client",client);
        params.put("manifacturer",manifacturer);
        params.put("imei",imei);
        params.put("os",os);
        params.put("gcm_token",gcmRegId);

        Iterator it = params.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            Log.d(this.getClass().getCanonicalName(), "params - " + pairs.getKey() + ", " + pairs.getValue());
        }

        String url = STTKeys.REGISTER;   // + "/" + PreferenceHelper.getSharedPreference().getString(STTKeys.USER_ID,"");

        try {
            SharedPreferences sp = STTarterManager.getInstance().getContext().getSharedPreferences(STTKeys.STTARTER_PREFERENCES, Context.MODE_PRIVATE);
            if (!TextUtils.isEmpty(sp.getString(STTKeys.USERDEFINED_BASE_HOST,""))){
                url = url.replace("sttarter.com",sp.getString(STTKeys.USERDEFINED_BASE_HOST,""));
            }
        } catch (STTarterManager.ContextNotInitializedException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "AllUserss url - " + url);

        GsonRequest<STTResponse> myReq = new GsonRequest<STTResponse>(
                url,
                STTResponse.class,
                getHeaders(),
                getRegisterSuccessListener(),
                RequestQueueHelper.responseErrorListener(),
                Request.Method.POST, params);

        RequestQueueHelper.addToRequestQueue(myReq, "");

    }


    private Response.Listener<STTResponse> getRegisterSuccessListener() {

        return new Response.Listener<STTResponse>() {
            @Override
            public void onResponse(STTResponse response) {

            }
        };
    }


    public void unregisterApp(String app_key,String username,String client) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("app_key",app_key);
        params.put("username",username);
        params.put("client",client);

        Iterator it = params.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            Log.d(this.getClass().getCanonicalName(), "params - " + pairs.getKey() + ", " + pairs.getValue());
        }

        String url = STTKeys.UNREGISTER;   // + "/" + PreferenceHelper.getSharedPreference().getString(STTKeys.USER_ID,"");

        try {
            SharedPreferences sp = STTarterManager.getInstance().getContext().getSharedPreferences(STTKeys.STTARTER_PREFERENCES, Context.MODE_PRIVATE);
            if (!TextUtils.isEmpty(sp.getString(STTKeys.USERDEFINED_BASE_HOST,""))){
                url = url.replace("sttarter.com",sp.getString(STTKeys.USERDEFINED_BASE_HOST,""));
            }
        } catch (STTarterManager.ContextNotInitializedException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "AllUserss url - " + url);

        GsonRequest<STTResponse> myReq = new GsonRequest<STTResponse>(
                url,
                STTResponse.class,
                getHeaders(),
                getUnRegisterSuccessListener(),
                RequestQueueHelper.responseErrorListener(),
                Request.Method.POST, params);

        RequestQueueHelper.addToRequestQueue(myReq, "");

    }


    private Response.Listener<STTResponse> getUnRegisterSuccessListener() {

        return new Response.Listener<STTResponse>() {
            @Override
            public void onResponse(STTResponse response) {

            }
        };
    }



    public void retrieveNewToken() {
        Map<String, String> headers = new HashMap<String, String>();

        Map<String, String> params = new HashMap<String, String>();
        params = STTarterManager.getInstance().getAppParams();

        /*
        Iterator it = headers.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            Log.d(this.getClass().getCanonicalName(), "headers - " + pairs.getKey() + ", " + pairs.getValue());
        }
        */

        String url = STTKeys.AUTH;

        try {
            SharedPreferences sp = STTarterManager.getInstance().getContext().getSharedPreferences(STTKeys.STTARTER_PREFERENCES, Context.MODE_PRIVATE);
            if (!TextUtils.isEmpty(sp.getString(STTKeys.USERDEFINED_BASE_HOST,""))){
                url = url.replace("sttarter.com",sp.getString(STTKeys.USERDEFINED_BASE_HOST,""));
            }
        } catch (STTarterManager.ContextNotInitializedException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "retrieve new Token url - " + url);

        GsonRequest<AuthInfo> myReq = new GsonRequest<AuthInfo>(
                url,
                AuthInfo.class,
                headers,
                getAuthTokenSuccessListener(),
                RequestQueueHelper.responseErrorListener(),
                Request.Method.POST, params);

        int socketTimeout = 30000;//or (30000)30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(2*socketTimeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        myReq.setRetryPolicy(policy);

        // set this to be executed before any other rquesst in queue
        myReq.setSequence(0);
        RequestQueueHelper.addToRequestQueue(myReq);
    }

    private Response.Listener<AuthInfo> getAuthTokenSuccessListener() {

        return new Response.Listener<AuthInfo>() {
            @Override
            public void onResponse(AuthInfo response) {
                try{
                    if(!response.getToken().equals(null)) {
                        // store the subscribed topics as a string in shared preferences
                        PreferenceHelper.getSharedPreferenceEditor().putString(STTKeys.AUTH_TOKEN, response.getToken());
                        PreferenceHelper.getSharedPreferenceEditor().commit();
                        Log.d("STTGeneralRoutines", response.getToken());
                    } else {
                        // TODO try another time or go back to home screen
                        Log.d(getClass().getCanonicalName(), "response - login credentials might be wrong: 401 unauthorized");
                    }
                /*
                if(response.getStatus() == 200) {
                    // store the subscribed topics as a string in shared preferences
                    spEditor.putString(STTKeys.AUTH_TOKEN, response.getToken());
                    spEditor.commit();
                    Log.d("STTGeneralRoutines", response.getToken());
                } else if (response.getStatus() == 401) {
                    // TODO try another time or go back to home screen
                    Log.d(getClass().getCanonicalName(), "response - login credentials are wrong: 401 unauthorized");
                } else {
                    // TODO try another time or go back to home screen
                    Log.d(getClass().getCanonicalName(), "response - some error occured");
                }
                */
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
    }

    private void getNewAuthToken(String callBackFunctionName) {
        switch (callBackFunctionName) {
            case "subscribeInitalize" : this.subscribeInitalize();
                break;
            default: break;
        }
    }

    /*public void initChat(String requestingUser, String requestedUser) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("user1", requestingUser);
        params.put("user2", requestedUser);

        Iterator it = params.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            Log.d(this.getClass().getCanonicalName(), "params - " + pairs.getKey() + ", " + pairs.getValue());
        }

        String url = STTKeys.INITCHAT;

        GsonRequest<ChatTopicInfo> myReq = new GsonRequest<ChatTopicInfo>(
                url,
                ChatTopicInfo.class,
                STTarterManager.getInstance().getHeaders(),
                intiChatSuccessListener(),
                RequestQueueHelper.responseErrorListener(),
                Request.Method.POST, params);

        // set this to be executed before any other rquesst in queue
        myReq.setSequence(0);
        RequestQueueHelper.addToRequestQueue(myReq);
    }

    private Response.Listener<ChatTopicInfo> intiChatSuccessListener() {

        return new Response.Listener<ChatTopicInfo>() {
            @Override
            public void onResponse(ChatTopicInfo response) {
                // Add topic to the database and start the ChatActivity with the returned topic
                STTProviderHelper ph = new STTProviderHelper();
                //ph.insertTopic(response.getData());

                Log.d("STTGeneralRoutines", "chatId - " + response.getData().getTopic());
                Intent chatIntent = null;
                try {
                    chatIntent = new Intent(STTarterManager.getInstance().getContext(), ChatActivity.class);
                    chatIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    chatIntent.putExtra(STTKeys.CHAT_TOPIC, response.getData().getTopic());
                    STTarterManager.getInstance().getContext().startActivity(chatIntent);
                } catch (STTarterManager.ContextNotInitializedException e) {
                    e.printStackTrace();
                }
            }
        };
    }*/

    private void subscribeTopicWithoutCallback(boolean callback,String topicName, String userID, STTSuccessListener sttSuccessListener, Response.ErrorListener errorListener){
        Map<String, String> params = new HashMap<String, String>();
        params.put("group_id", topicName);
        params.put("user", userID);

        Iterator it = params.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            Log.d(this.getClass().getCanonicalName(), "params - " + pairs.getKey() + ", " + pairs.getValue());
        }

        String url = STTKeys.SUB;

        Log.d(TAG, "subscribe url - " + url);

        GsonRequest<STTResponse> myReq = new GsonRequest<STTResponse>(
                url,
                STTResponse.class,
                getHeaders(),
                subscribeSuccessListener(sttSuccessListener,callback),
                errorListener,
                Request.Method.POST, params);

        // set this to be executed before any other rquesst in queue
        //myReq.setSequence(0);
        RequestQueueHelper.addToRequestQueue(myReq);
    }

    public void subscribeTopic(String topicName, String userID, STTSuccessListener sttSuccessListener, Response.ErrorListener errorListener) {
        subscribeTopicWithoutCallback(true,topicName,userID,sttSuccessListener,errorListener);
    }

    private Response.Listener<STTResponse> subscribeSuccessListener(final STTSuccessListener sttSuccessListener, final boolean callBack) {

        return new Response.Listener<STTResponse>() {
            @Override
            public void onResponse(STTResponse response) {
                try{
                    Log.d("STTGeneralRoutines", "SUBSCRIBED - " + response.getStatus());
                    if (callBack)
                        sttSuccessListener.Response(response);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
    }


    public void leaveGroup(Context context,String topicName,STTSuccessListener sttSuccessListener,Response.ErrorListener errorListener) {
        Map<String, String> params = new HashMap<String, String>();
        String[] topic = topicName.split("-group-");
        params.put("group_id", topic[topic.length-1]);
        params.put("user", PreferenceHelper.getSharedPreference().getString(STTKeys.USER_ID, ""));

        Iterator it = params.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            Log.d(this.getClass().getCanonicalName(), "Unsub params - " + pairs.getKey() + ", " + pairs.getValue());
        }

        String url = STTKeys.UNSUB;

        try {
            SharedPreferences sp = STTarterManager.getInstance().getContext().getSharedPreferences(STTKeys.STTARTER_PREFERENCES, Context.MODE_PRIVATE);
            if (!TextUtils.isEmpty(sp.getString(STTKeys.USERDEFINED_BASE_HOST,""))){
                url = url.replace("sttarter.com",sp.getString(STTKeys.USERDEFINED_BASE_HOST,""));
            }
        } catch (STTarterManager.ContextNotInitializedException e) {
            e.printStackTrace();
        }

        Log.d("Unsub Url",url);

        GsonRequest<STTResponse> myReq = new GsonRequest<STTResponse>(
                url,
                STTResponse.class,
                getHeaders(),
                unsubscribeSuccessListener(sttSuccessListener,topicName),
                errorListener,
                Request.Method.DELETE, params);

        // set this to be executed before any other rquesst in queue
        //myReq.setSequence(0);
        //RequestQueueHelper.addToRequestQueue(myReq);

        HttpStack httpStack;
        if (Build.VERSION.SDK_INT > 19){
            httpStack = new CustomHurlStack();
        } else if (Build.VERSION.SDK_INT >= 9 && Build.VERSION.SDK_INT <= 19)
        {
            httpStack = new OkHttpHurlStack();
        } else {
            httpStack = new HttpClientStack(AndroidHttpClient.newInstance("Android"));
        }
        RequestQueue requestQueue = Volley.newRequestQueue(context, httpStack);
        requestQueue.add(myReq);
    }

    private Response.Listener<STTResponse> unsubscribeSuccessListener(final STTSuccessListener sttSuccessListener, final String topicName) {

        return new Response.Listener<STTResponse>() {
            @Override
            public void onResponse(STTResponse response) {
                try{
                    Log.d("STTGeneralRoutines", "UNSUBSCRIBED - " + response.getStatus());
                    STTProviderHelper ph = new STTProviderHelper();
                    ph.updateTopicSubscribe(topicName, 0);
                    STTarterManager.getInstance().unsubscribe(topicName);
                    sttSuccessListener.Response(response);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
    }

    public void updateRead(String topic){
        STTProviderHelper sttProviderHelper = new STTProviderHelper();
        sttProviderHelper.updateMessageRead(topic);
    }

    protected Map<String, String>  getHeaders() {
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

    // Get a MemoryInfo object for the device's current memory status.
    public ActivityManager.MemoryInfo getAvailableMemory() {
        ActivityManager activityManager = null;
        try {
            activityManager = (ActivityManager) STTarterManager.getInstance().getContext().getSystemService(Context.ACTIVITY_SERVICE);
        } catch (STTarterManager.ContextNotInitializedException e) {
            e.printStackTrace();
        }
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo;
    }

}

