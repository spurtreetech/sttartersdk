package com.spurtreetech.sttarter.lib.helper;

import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.spurtreetech.sttarter.lib.helper.models.AllTopicsInfo;
import com.spurtreetech.sttarter.lib.helper.models.AllUsersInfo;
import com.spurtreetech.sttarter.lib.helper.models.AuthInfo;
import com.spurtreetech.sttarter.lib.helper.models.MyResponse;
import com.spurtreetech.sttarter.lib.helper.models.MyTopicsInfo;
import com.spurtreetech.sttarter.lib.helper.models.SubscribeInfo;
import com.spurtreetech.sttarter.lib.helper.models.Topic;
import com.spurtreetech.sttarter.lib.provider.STTProviderHelper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by RahulT on 18-06-2015.
 */
public class STTGeneralRoutines {

    private static final String TAG = "STTGeneralRoutines";
    //SharedPreferences sp = PreferenceHelper.getSharedPreference();
    //SharedPreferences.Editor spEditor = PreferenceHelper.getSharedPreferenceEditor();

    public static final String GET_ALL_TOPICS = "getAllTopics";
    public static final String RETRIEVE_NEW_TOKEN = "retieveNewToken";


    public void subscribeInitalize() {

        // TODO unsubscribe all topics
        // get all subscribed topics
        //String[] topics = PreferenceHelper.getSharedPreference().getString(Keys.SUBSCRIBED_TOPICS,"").split(",");
        //STTarter.getInstance().unsubscribe(topics);

        // TODO get all topics from server and subscribe to all of them
        getMyTopics();
        //getAllTopics();


    }

    public void getMyTopics() {

        Map<String, String> params = new HashMap<String, String>();

        Iterator it = params.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            Log.d(this.getClass().getCanonicalName(), "params - " + pairs.getKey() + ", " + pairs.getValue());
        }

        String url = Keys.MY_TOPICS + "/" ;//+ PreferenceHelper.getSharedPreference().getString(Keys.USER_ID, "");
        Log.d(TAG, "MyTopics url - " + url);

        GsonRequest<MyTopicsInfo> myReq = new GsonRequest<MyTopicsInfo>(
                url,
                MyTopicsInfo.class,
                STTarter.getInstance().getHeaders(),
                getMyTopicsSuccessListener(),
                RequestQueueHelper.responseErrorListener(),
                Request.Method.GET, params);

        RequestQueueHelper.addToRequestQueue(myReq, "");

    }

    private Response.Listener<MyTopicsInfo> getMyTopicsSuccessListener() {

        return new Response.Listener<MyTopicsInfo>() {
            @Override
            public void onResponse(MyTopicsInfo response) {

                String subscribedTopics = "";
                boolean clientConnected = false;
                try {
                    clientConnected = Connections.getInstance(STTarter.getInstance().getContext()).getConnection(STTarter.getInstance().getClientHandle()).isConnected();
                } catch (STTarter.ContextNotInitializedException e) {
                    e.printStackTrace();
                }

                for(Topic tempTopic : response.getTopics()) {
                    // TODO subscribe all topics
                    Log.d("getMyTopics()", tempTopic.getTopic());
                    //STTarter.getInstance().subscribe(tempTopic.getTopic());
                    Log.d(getClass().getCanonicalName(), "subscribed to - " + tempTopic.getTopic());
                    if(clientConnected) {
                        STTarter.getInstance().subscribe(tempTopic.getTopic());
                    }

                    if(!tempTopic.getType().equals("master")) {
                        if(subscribedTopics.equals("")) {subscribedTopics = tempTopic.getTopic();}
                        else {subscribedTopics += ","+tempTopic.getTopic();}
                    }
                }

                final MyTopicsInfo myTopicsResponse = response;

                try {
                    //ph.deleteAllTopics();
                    Thread insertThread = new Thread() {
                        public void run() {
                            STTProviderHelper ph = new STTProviderHelper();
                            ph.insertTopics(myTopicsResponse.getTopics(), true);
                        }
                    };
                    insertThread.start();
                } catch (SQLiteConstraintException e) {
                    e.printStackTrace();
                }

                // store the subscribed topics as a string in shared preferences
                //if(PreferenceHelper.getSharedPreference().getString(Keys.SUBSCRIBED_TOPICS,"").equals("")) {
                PreferenceHelper.getSharedPreferenceEditor().putString(Keys.SUBSCRIBED_TOPICS, subscribedTopics);
                PreferenceHelper.getSharedPreferenceEditor().commit();
                //}

                getAllTopics();

            }
        };
    }

    public void getAllTopics() {

        Map<String, String> params = new HashMap<String, String>();

        Iterator it = params.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            Log.d(this.getClass().getCanonicalName(), "params - " + pairs.getKey() + ", " + pairs.getValue());
        }

        String url = Keys.ALL_TOPICS;   // + "/" + PreferenceHelper.getSharedPreference().getString(Keys.USER_ID,"");
        Log.d(TAG, "AllTopics url - " + url);

        GsonRequest<AllTopicsInfo> myReq = new GsonRequest<AllTopicsInfo>(
                url,
                AllTopicsInfo.class,
                STTarter.getInstance().getHeaders(),
                getAllTopicsSuccessListener(),
                RequestQueueHelper.responseErrorListener(),
                Request.Method.GET, params);

        RequestQueueHelper.addToRequestQueue(myReq, "");

    }

    private Response.Listener<AllTopicsInfo> getAllTopicsSuccessListener() {

        return new Response.Listener<AllTopicsInfo>() {
            @Override
            public void onResponse(AllTopicsInfo response) {

                String subscribedTopics = "";
                boolean clientConnected = false;
                try {
                    clientConnected = Connections.getInstance(STTarter.getInstance().getContext()).getConnection(STTarter.getInstance().getClientHandle()).isConnected();
                } catch (STTarter.ContextNotInitializedException e) {
                    e.printStackTrace();
                }

                for(Topic tempTopic : response.getTopics()) {
                    // TODO subscribe all topics
                    Log.d("getAllTopics()", tempTopic.getTopic());
                    //STTarter.getInstance().subscribe(tempTopic.getTopic());
                    Log.d(getClass().getCanonicalName(), "subscribed to - " + tempTopic.getTopic());
                    if(clientConnected) {
                        // do not subscribe topics here
                        //STTarter.getInstance().subscribe(tempTopic.getTopic());
                    }

                    if(!tempTopic.getType().equals("master")) {
                        if(subscribedTopics.equals("")) {subscribedTopics = tempTopic.getTopic();}
                        else {subscribedTopics += ","+tempTopic.getTopic();}
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
                //if(PreferenceHelper.getSharedPreference().getString(Keys.SUBSCRIBED_TOPICS,"").equals("")) {
                PreferenceHelper.getSharedPreferenceEditor().putString(Keys.ALL_TOPICS_LIST, subscribedTopics);
                PreferenceHelper.getSharedPreferenceEditor().commit();
                //}
                getAllUsers();
                //getMyTopics();
            }
        };
    }


    private void getAllUsers() {

        Map<String, String> params = new HashMap<String, String>();

        Iterator it = params.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            Log.d(this.getClass().getCanonicalName(), "params - " + pairs.getKey() + ", " + pairs.getValue());
        }

        String url = Keys.ALL_USERS;   // + "/" + PreferenceHelper.getSharedPreference().getString(Keys.USER_ID,"");
        Log.d(TAG, "AllUserss url - " + url);

        GsonRequest<AllUsersInfo> myReq = new GsonRequest<AllUsersInfo>(
                url,
                AllUsersInfo.class,
                STTarter.getInstance().getHeaders(),
                getAllUsersSuccessListener(),
                RequestQueueHelper.responseErrorListener(),
                Request.Method.GET, params);

        RequestQueueHelper.addToRequestQueue(myReq, "");

    }

    private Response.Listener<AllUsersInfo> getAllUsersSuccessListener() {

        return new Response.Listener<AllUsersInfo>() {
            @Override
            public void onResponse(AllUsersInfo response) {

                Gson gson = new Gson();
                String sSS = gson.toJson(response);

                Log.d("UserInformations",sSS);

                //STTProviderHelper ph = new STTProviderHelper();
                try {
                    //ph.deleteAllTopics();
                    final AllUsersInfo allUsersInfo = response;

                    Thread insertThread = new Thread() {
                        public void run() {
                            STTProviderHelper ph = new STTProviderHelper();
                            ph.insertUsers(allUsersInfo.getUsers());
                        }
                    };
                    insertThread.start();

                } catch (SQLiteConstraintException e) {
                    e.printStackTrace();
                }

                // store the subscribed topics as a string in shared preferences
                //if(PreferenceHelper.getSharedPreference().getString(Keys.SUBSCRIBED_TOPICS,"").equals("")) {
                /*PreferenceHelper.getSharedPreferenceEditor().putString(Keys.ALL_TOPICS_LIST, subscribedTopics);
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

        String url = Keys.REGISTER;   // + "/" + PreferenceHelper.getSharedPreference().getString(Keys.USER_ID,"");
        Log.d(TAG, "AllUserss url - " + url);

        GsonRequest<MyResponse> myReq = new GsonRequest<MyResponse>(
                url,
                MyResponse.class,
                STTarter.getInstance().getHeaders(),
                getRegisterSuccessListener(),
                RequestQueueHelper.responseErrorListener(),
                Request.Method.POST, params);

        RequestQueueHelper.addToRequestQueue(myReq, "");

    }


    private Response.Listener<MyResponse> getRegisterSuccessListener() {

        return new Response.Listener<MyResponse>() {
            @Override
            public void onResponse(MyResponse response) {

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

        String url = Keys.UNREGISTER;   // + "/" + PreferenceHelper.getSharedPreference().getString(Keys.USER_ID,"");
        Log.d(TAG, "AllUserss url - " + url);

        GsonRequest<MyResponse> myReq = new GsonRequest<MyResponse>(
                url,
                MyResponse.class,
                STTarter.getInstance().getHeaders(),
                getUnRegisterSuccessListener(),
                RequestQueueHelper.responseErrorListener(),
                Request.Method.POST, params);

        RequestQueueHelper.addToRequestQueue(myReq, "");

    }


    private Response.Listener<MyResponse> getUnRegisterSuccessListener() {

        return new Response.Listener<MyResponse>() {
            @Override
            public void onResponse(MyResponse response) {

            }
        };
    }



    public void retrieveNewToken() {
        Map<String, String> headers = new HashMap<String, String>();

        Map<String, String> params = new HashMap<String, String>();
        params = STTarter.getInstance().getAppParams();

        /*
        Iterator it = headers.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            Log.d(this.getClass().getCanonicalName(), "headers - " + pairs.getKey() + ", " + pairs.getValue());
        }
        */

        String url = Keys.AUTH;

        GsonRequest<AuthInfo> myReq = new GsonRequest<AuthInfo>(
                url,
                AuthInfo.class,
                headers,
                getAuthTokenSuccessListener(),
                RequestQueueHelper.responseErrorListener(),
                Request.Method.POST, params);

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
                    PreferenceHelper.getSharedPreferenceEditor().putString(Keys.AUTH_TOKEN, response.getToken());
                    PreferenceHelper.getSharedPreferenceEditor().commit();
                    Log.d("STTGeneralRoutines", response.getToken());
                } else {
                    // TODO try another time or go back to home screen
                    Log.d(getClass().getCanonicalName(), "response - login credentials might be wrong: 401 unauthorized");
                }
                /*
                if(response.getStatus() == 200) {
                    // store the subscribed topics as a string in shared preferences
                    spEditor.putString(Keys.AUTH_TOKEN, response.getToken());
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

        String url = Keys.INITCHAT;

        GsonRequest<ChatTopicInfo> myReq = new GsonRequest<ChatTopicInfo>(
                url,
                ChatTopicInfo.class,
                STTarter.getInstance().getHeaders(),
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
                    chatIntent = new Intent(STTarter.getInstance().getContext(), ChatActivity.class);
                    chatIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    chatIntent.putExtra(Keys.CHAT_TOPIC, response.getData().getTopic());
                    STTarter.getInstance().getContext().startActivity(chatIntent);
                } catch (STTarter.ContextNotInitializedException e) {
                    e.printStackTrace();
                }
            }
        };
    }*/

    public void subscribeTopic(String topicName, String userName) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("topic", topicName);
        params.put("user", userName);

        Iterator it = params.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            Log.d(this.getClass().getCanonicalName(), "params - " + pairs.getKey() + ", " + pairs.getValue());
        }

        String url = Keys.SUB;

        GsonRequest<SubscribeInfo> myReq = new GsonRequest<SubscribeInfo>(
                url,
                SubscribeInfo.class,
                STTarter.getInstance().getHeaders(),
                subscribeSuccessListener(),
                RequestQueueHelper.responseErrorListener(),
                Request.Method.POST, params);

        // set this to be executed before any other rquesst in queue
        //myReq.setSequence(0);
        RequestQueueHelper.addToRequestQueue(myReq);
    }

    private Response.Listener<SubscribeInfo> subscribeSuccessListener() {

        return new Response.Listener<SubscribeInfo>() {
            @Override
            public void onResponse(SubscribeInfo response) {
                Log.d("STTGeneralRoutines", "SUBSCRIBED - " + response.getStatus());
            }
        };
    }


    public void unsubscribeTopic(String topicName, String userName) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("topic", topicName);
        params.put("user", userName);

        Iterator it = params.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            Log.d(this.getClass().getCanonicalName(), "Unsub params - " + pairs.getKey() + ", " + pairs.getValue());
        }

        String url = Keys.UNSUB;

        Log.d("Unsub Url",url);

        GsonRequest<SubscribeInfo> myReq = new GsonRequest<SubscribeInfo>(
                url,
                SubscribeInfo.class,
                STTarter.getInstance().getHeaders(),
                unsubscribeSuccessListener(topicName),
                RequestQueueHelper.responseErrorListener(),
                Request.Method.POST, params);

        // set this to be executed before any other rquesst in queue
        //myReq.setSequence(0);
        RequestQueueHelper.addToRequestQueue(myReq);
    }

    private Response.Listener<SubscribeInfo> unsubscribeSuccessListener(final String topicName) {

        return new Response.Listener<SubscribeInfo>() {
            @Override
            public void onResponse(SubscribeInfo response) {
                Log.d("STTGeneralRoutines", "UNSUBSCRIBED - " + response.getStatus());
                STTProviderHelper ph = new STTProviderHelper();
                ph.updateTopicSubscribe(topicName, 0);
                STTarter.getInstance().unsubscribe(topicName);
            }
        };
    }

}
