package com.spurtreetech.sttarter.lib.helper;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.spurtreetech.sttarter.lib.R;
import com.spurtreetech.sttarter.lib.helper.models.PayloadData;
import com.spurtreetech.sttarter.lib.helper.models.TopicMeta;
import com.spurtreetech.sttarter.lib.helper.utils.NotificationHelperListener;
import com.spurtreetech.sttarter.lib.provider.STTProviderHelper;
import com.spurtreetech.sttarter.lib.provider.messages.MessagesCursor;
import com.spurtreetech.sttarter.lib.provider.topics.TopicsCursor;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Handles call backs from the STT Client
 *
 */
public class STTCallbackHandler implements MqttCallback {

    /** {@link Context} for the application used to format and import external strings**/
    private Context context;
    /** Client handle to reference the connection that this handler is attached to**/
    private String clientHandle;

    private String notificationActivity = "com.spurtreetech.sttarter.sttarter.NotificationActivity";

    /**
     * Creates an <code>STTCallbackHandler</code> object
     * @param context The application's context
     * @param clientHandle The handle to a {@link Connection} object
     */
    BroadcastHelper  broadCastHelper;
    NotificationHelperListener notificationHelperListener;

    public STTCallbackHandler(Context context, String clientHandle)
    {
        this.context = context;
        this.clientHandle = clientHandle;
        broadCastHelper = new BroadcastHelper(context);
    }

    public STTCallbackHandler(Context context, String clientHandle, NotificationHelperListener notificationHelperListener)
    {
        this.context = context;
        this.clientHandle = clientHandle;
        this.notificationHelperListener = notificationHelperListener;
        broadCastHelper = new BroadcastHelper(context);
    }

    /**
     * @see MqttCallback#connectionLost(Throwable)
     */
    @Override
    public void connectionLost(Throwable cause) {
//	  cause.printStackTrace();
        if (cause != null) {
            Connection c = Connections.getInstance(context).getConnection(clientHandle);
            c.addAction("Connection Lost");
            c.changeConnectionStatus(Connection.ConnectionStatus.DISCONNECTED);

            //format string to use a notification text
            Object[] args = new Object[2];
            args[0] = c.getId();
            args[1] = c.getHostName();

            String message = context.getString(R.string.connection_lost, args);

            //build intent
            Intent intent = new Intent();
            intent.setClassName(context, notificationActivity);
            intent.putExtra("handle", clientHandle);

            //notify the user
            Notify.notifcation(context, message, intent, R.string.notifyTitle_connectionLost);
            // TODO try to restart connection if internet connection is available
            try {
                STTarter.getInstance().initiateConnnection(notificationHelperListener);
            } catch (STTarter.ContextNotInitializedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @see MqttCallback#messageArrived(String, MqttMessage)
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {

        //Get connection object associated with this object
        Connection c = Connections.getInstance(context).getConnection(clientHandle);

        //create arguments to format message arrived notifcation string
        String[] args = new String[2];
        args[0] = new String(message.getPayload());
        args[1] = topic+";qos:"+message.getQos()+";retained:"+message.isRetained();

        //get the string from strings.xml and format
        //String messageString = context.getString(R.string.messageRecieved, (Object[]) args);
        String messageString = context.getString(R.string.messageRecieved);

        //create intent to start activity
        Intent intent = new Intent();
        intent.setClassName(context, notificationActivity);
        intent.putExtra("handle", clientHandle);

        //format string args
        Object[] notifyArgs = new String[3];
        notifyArgs[0] = c.getId();
        notifyArgs[1] = new String(message.getPayload());
        notifyArgs[2] = topic;

        //notify the user
        //Notify.notifcation(context, context.getString(R.string.notification, notifyArgs), intent, R.string.notifyTitle);

        //update client history
        c.addAction(messageString);
        Log.d(getClass().getSimpleName(), "Message Received: " + new String(message.getPayload()) + " , from topic: " + topic);
        //broadCastHelper.sendMessage(message.getPayload().toString());
        // insert message into the local db
        STTProviderHelper ph = new STTProviderHelper();
        // TODO find if a message is a system message or a normal message, if system message then show else
        Gson gson = new Gson();
        PayloadData pd = gson.fromJson(new String(message.getPayload()), PayloadData.class);
        if(pd.getType().equals("system")) {
            // do not enter it in database
            Log.d(getClass().getSimpleName(), "System Message received");
            // Notify the chat activity of the message, catch all different types of message
            String systemMessageType = "user_typing";
            switch (pd.getPayload().getTitle()) {
                case "user-typing":
                    break;
                case "online":
                    systemMessageType = "online";  //pd.getPayload().getTitle();
                    break;
                case "offline":
                    systemMessageType = "offline";
                    break;
                case "joinchat":
                    systemMessageType = "joinchat";
                    break;
                case "leftchat":
                    systemMessageType = "leftchat";
                    break;
                case "newtopic":
                    systemMessageType = "newtopic";
                    break;
                case "topicdeleted":
                    systemMessageType = "topicdeleted";
                    break;
                case "newchat":
                    systemMessageType = "newchat";
                    break;
                case "newgroupchat":
                    systemMessageType = "newgroupchat";
                    break;
                default:
                    break;
            }
            broadCastHelper.sendSystemMessage(pd.getPayload().getMessage(), SysMessage.valueOf(systemMessageType), pd.getPayload().getTopic());

        } else {Log.d(getClass().getSimpleName(), "Text Message received");
            // find the message, check if the  message is by sender, update if true or else insert it into the database is not already present by checking the hashcode
            TopicMeta tm = new TopicMeta();
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

                if(STTarter.isApplicationSentToBackground()) {

                    // insertMessage should have is_read field as false if application is in background
                    ph.insertMessage(pd, false, false);
                    ph.updateTopicActiveTime(pd);

                    String notificationString = PreferenceHelper.getSharedPreference().getString(STTKeys.NOTIFICATION_TOPICS,"");
                    TopicsCursor tc = ph.getTopicData(pd.getPayload().getTopic());

                    if(tc.getCount()!=0) {

                        tc.moveToFirst();
                        tm = gson.fromJson(tc.getTopicMeta(), TopicMeta.class);

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
            }

        }
        // TODO find if message exists, if not then insert it or else discard it. Also if exist check if it was by sender and mark it as sent.

        //if()
    }

    /**
     * @see MqttCallback#deliveryComplete(IMqttDeliveryToken)
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // Do nothing
    }

}
