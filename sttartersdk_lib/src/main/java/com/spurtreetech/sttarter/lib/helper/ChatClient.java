package com.spurtreetech.sttarter.lib.helper;

import android.util.Log;

import com.google.gson.Gson;
import com.spurtreetech.sttarter.lib.helper.ActionListener.Action;
import com.spurtreetech.sttarter.lib.helper.models.Payload;
import com.spurtreetech.sttarter.lib.helper.models.PayloadData;
import com.spurtreetech.sttarter.lib.helper.utils.DateTimeHelper;
import com.spurtreetech.sttarter.lib.provider.STTProviderHelper;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

/**
 * Created by RahulT on 30-06-2015.
 */
public class ChatClient {

    // Possible system message types
    /*
    public static final String USER_TYPING = "user-typing";
    public static final String ONLINE = "online";
    public static final String OFFLINE = "offline";
    public static final String JOIN_CHAT = "joinchat";
    public static final String LEFT_CHAT = "leftchat";
    public static final String NEW_TOPIC = "newtopic";
    public static final String TOPIC_DELETED = "topicdeleted";
    public static final String NEW_CHAT = "newchat";
    public static final String NEW_GROUP_CHAT = "newgroupchat";
    */

    private static void send(String message, String topic, String type, int qos) {

        boolean retained = false;

        String[] args = new String[3];
        args[0] = message;
        args[1] = topic+";qos:"+qos+";retained:"+retained;
        args[2] = type;

        try {
            Connections.getInstance(STTarter.getInstance().getContext())
                    .getConnection(STTarter.getInstance().getClientHandle())
                    .getClient()
                    .publish(
                            topic,
                            message.getBytes(),
                            qos,
                            retained,
                            null,
                            new ActionListener(STTarter.getInstance().getContext(), Action.PUBLISH, STTarter.getInstance().getClientHandle(), args)
                    );
            // TODO insert into DB with status as not delivered : remove insert from action listener and use it to update status instead
            /** only message s must be stored in the database
             * other kind of messages must be used only for ui events (e.g.: typing, user status messgae etc)
             */
            // TODO insert into message content provider for the respective topic
            /*
            if(args[2].equals("message")) {
                STTProviderHelper ph = new STTProviderHelper();
                ph.insertMessage(args[0], topic, true, false, DateTimeHelper.getCurrentTimeStamp());
            }
            */
        } catch (MqttSecurityException e) {
            Log.e("ChatClient", "Failed to publish a messged from the client with the handle " + STTarter.getInstance().getClientHandle(), e);
        } catch (MqttException e) {
            Log.e("ChatClient", "Failed to publish a messged from the client with the handle " + STTarter.getInstance().getClientHandle(), e);
        } catch (STTarter.ContextNotInitializedException e) {
            e.printStackTrace();
        }
    }

    public static void sendMessage(String message, String topic) {

        PayloadData pd = new PayloadData();
        pd.setType("message");
        pd.setTimestamp(""+DateTimeHelper.getUnixTimeStamp());
        pd.setFrom(PreferenceHelper.getSharedPreference().getString(Keys.USER_ID, ""));

        Payload tempPayload = new Payload();
        tempPayload.setTitle("message");
        tempPayload.setMessage(message);
        tempPayload.setTopic(topic);
        pd.setPayload(tempPayload);

        int qos = ActivityConstants.defaultQos;

        Gson gson = new Gson();
        //gson.toJson(pd);

        // insert all messages by sender in the db
        STTProviderHelper ph = new STTProviderHelper();
        ph.insertMessage(pd, true, false);
        ph.updateTopicActiveTime(pd);
        send(gson.toJson(pd), topic, "message", qos);

        Log.d("ChatClient", "converted json - " + gson.toJson(pd));
    }

    public static void sendSystemMessage(String topic, SysMessage messageType) {

        String user = PreferenceHelper.getSharedPreference().getString(Keys.USER_ID, "");
        PayloadData pd = new PayloadData();
        pd.setType("system");
        pd.setTimestamp(""+DateTimeHelper.getUnixTimeStamp());
        pd.setFrom(user);

        Payload tempPayload = new Payload();
        //tempPayload.setMessage(message);
        //tempPayload.setTopic(topic);
        //tempPayload.setTitle();
        int qos = ActivityConstants.defaultQos;
        switch (messageType) {
            case user_typing:
                tempPayload.setTitle(SysMessage.user_typing.toString());
                tempPayload.setMessage(user + " is typing");
                tempPayload.setTopic(topic);
                qos = 0;
                break;
            case joinchat:
                tempPayload.setTitle(SysMessage.joinchat.toString());
                tempPayload.setMessage((user + " has joined chat"));
                tempPayload.setTopic(topic);
                qos = 0;
                break;
            case leftchat:
                tempPayload.setTitle(SysMessage.leftchat.toString());
                tempPayload.setMessage((user + " has left chat"));
                tempPayload.setTopic(topic);
                qos = 0;
                break;
            case online:
                tempPayload.setTitle(SysMessage.online.toString());
                tempPayload.setMessage((user + " is online"));
                tempPayload.setTopic(topic);
                qos = 0;
                break;
            case offline:
                tempPayload.setTitle(SysMessage.offline.toString());
                tempPayload.setMessage((user + " is offline"));
                tempPayload.setTopic(topic);
                qos = 0;
                break;
            case newtopic:
                tempPayload.setTitle(SysMessage.newtopic.toString());
                tempPayload.setTopic(topic);
                tempPayload.setMessage((user + " has added you in " + topic));
                qos = 0; //
                break;
            case topicdeleted:
                tempPayload.setTitle(SysMessage.topicdeleted.toString());
                tempPayload.setTopic(topic);
                tempPayload.setMessage(("topic " + topic + " has been deleted"));
                qos = 0; //
                break;
            case newchat:
                tempPayload.setTitle(SysMessage.newchat.toString());
                tempPayload.setTopic(topic);
                tempPayload.setMessage((user + "wants to chat with you"));
                qos = 0; //
                break;
            case newgroupchat:
                tempPayload.setTitle(SysMessage.newgroupchat.toString());
                tempPayload.setTopic(topic);
                tempPayload.setGroup_name("group_name");
                tempPayload.setMessage((user + " added you in group " + "group_name"));
                qos = 0; //
                break;
            default: break;
        }

        pd.setPayload(tempPayload);

        Gson gson = new Gson();
        //gson.toJson(pd);
        send(gson.toJson(pd),topic, "system", qos);

        Log.d("ChatClient", "converted json - " + gson.toJson(pd));

    }
}

