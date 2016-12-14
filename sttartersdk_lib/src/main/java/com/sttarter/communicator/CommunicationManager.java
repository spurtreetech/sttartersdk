package com.sttarter.communicator;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteConstraintException;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.google.gson.Gson;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

        // TODO get all topics from server and subscribe to all of them
        getAllTopics();


    }

    protected void getMyTopics() {

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
                getMyTopicsSuccessListener(),
                RequestQueueHelper.responseErrorListener(),
                Request.Method.GET, params);

        int socketTimeout = 30000;//or (30000)30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(2*socketTimeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        myReq.setRetryPolicy(policy);

        RequestQueueHelper.addToRequestQueue(myReq, "");

    }

    private Response.Listener<MyTopicsInfo> getMyTopicsSuccessListener() {

        return new Response.Listener<MyTopicsInfo>() {
            @Override
            public void onResponse(MyTopicsInfo response) {

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

                String subscribedTopics = "";
                boolean clientConnected = false;
                try {
                    clientConnected = Connections.getInstance(STTarterManager.getInstance().getContext()).getConnection(STTarterManager.getInstance().getClientHandle()).isConnected();
                } catch (STTarterManager.ContextNotInitializedException e) {
                    e.printStackTrace();
                }

                for(Group tempGroup : response.getTopics()) {
                    // TODO subscribe all topics
                    Log.d("getAllTopics()", tempGroup.getTopic());
                    //STTarterManager.getInstance().subscribe(tempGroup.getTopic());
                    Log.d(getClass().getCanonicalName(), "subscribed to - " + tempGroup.getTopic());
                    if(clientConnected) {
                        // do not subscribe topics here
                        //STTarterManager.getInstance().subscribe(tempGroup.getTopic());
                    }

                    if(!tempGroup.getType().equals("master")) {
                        if(subscribedTopics.equals("")) {subscribedTopics = tempGroup.getTopic();}
                        else {subscribedTopics += ","+ tempGroup.getTopic();}
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

                getMyTopics();
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
                    getMyTopics();
                    sttSuccessListener.Response(response);
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
                Log.d("STTGeneralRoutines", "SUBSCRIBED - " + response.getStatus());
                if (callBack)
                sttSuccessListener.Response(response);
            }
        };
    }


    public void leaveGroup(String topicName,STTSuccessListener sttSuccessListener,Response.ErrorListener errorListener) {
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
        RequestQueueHelper.addToRequestQueue(myReq);
    }

    private Response.Listener<STTResponse> unsubscribeSuccessListener(final STTSuccessListener sttSuccessListener, final String topicName) {

        return new Response.Listener<STTResponse>() {
            @Override
            public void onResponse(STTResponse response) {
                Log.d("STTGeneralRoutines", "UNSUBSCRIBED - " + response.getStatus());
                STTProviderHelper ph = new STTProviderHelper();
                ph.updateTopicSubscribe(topicName, 0);
                STTarterManager.getInstance().unsubscribe(topicName);
                sttSuccessListener.Response(response);
            }
        };
    }

    public void updateRead(String topic){
        STTProviderHelper sttProviderHelper = new STTProviderHelper();
        sttProviderHelper.updateMessageRead(topic);
    }

    protected Map<String, String>  getHeaders() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("x-user-token", PreferenceHelper.getSharedPreference().getString(STTKeys.USER_TOKEN,""));
        headers.put("x-app-token", PreferenceHelper.getSharedPreference().getString(STTKeys.AUTH_TOKEN,""));
        return headers;
    }

}

