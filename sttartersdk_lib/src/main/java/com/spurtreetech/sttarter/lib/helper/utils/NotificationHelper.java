package com.spurtreetech.sttarter.lib.helper.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.spurtreetech.sttarter.lib.R;
import com.spurtreetech.sttarter.lib.helper.STTarter;
import com.spurtreetech.sttarter.lib.helper.models.TopicMeta;
import com.spurtreetech.sttarter.lib.provider.STTProviderHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rahul on 07/08/15.
 */
public class NotificationHelper {

    private static NotificationManager notificationManager;

    public static void displayNotification(String groupsString) {

        Gson gson = new Gson();

        PendingIntent pIntent = null;
        Intent intent = new Intent();
        intent.setClassName("com.indusedge.indusedgesttarter", "com.indusedge.indusedgesttarter.TabbedActivity");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);

        NotificationCompat.Builder builder = null;

        STTProviderHelper ph = new STTProviderHelper();
        Cursor tempCursor = ph.getUnreadMessageCount();

        tempCursor.moveToFirst();
        String bigText = "", mainText = "", topicMetaString = "";
        JSONObject intentJSON = new JSONObject();
        JSONArray topicsArray = new JSONArray();
        int messagesCount = 0, topicsCount = 0;
        topicsCount = tempCursor.getCount();
        while (!tempCursor.isAfterLast()) {
            TopicMeta tm;
            if(bigText.equals("")) {
                topicMetaString = tempCursor.getString(2);
                tm = gson.fromJson(topicMetaString, TopicMeta.class);
                //bigText = tempCursor.getString(tempCursor.getColumnIndex("topic")) + " : " + "";
                Log.d("NotificationHelper", "column index - " + tempCursor.getString(0) + ", " + tempCursor.getString(1) + ", " + tempCursor.getString(2) + ", TopicName " + ((tm == null || tm.getName()==null)?"":tm.getName()));
                messagesCount = messagesCount + tempCursor.getInt(1);
            } else {
                topicMetaString = tempCursor.getString(2);
                tm = gson.fromJson(topicMetaString, TopicMeta.class);
                Log.d("NotificationHelper", "column index - " + tempCursor.getString(0) + ", " + tempCursor.getString(1) + ", " + tempCursor.getString(2) + ", TopicName "  + ((tm == null || tm.getName()==null)?"":tm.getName()));
                messagesCount = messagesCount + tempCursor.getInt(1);
            }
            //JSONObject tempTopicJSON = new JSONObject();
            //tempTopicJSON.p
            topicsArray.put(tempCursor.getString(0));
            tempCursor.moveToNext();
        }

        try {
            intentJSON.put("count", topicsCount);
            intentJSON.put("topic_name", topicsArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        intent.putExtra("notification_data", intentJSON.toString());
        try {
            notificationManager = (NotificationManager) STTarter.getInstance().getContext().getSystemService(Context.NOTIFICATION_SERVICE);
            pIntent = PendingIntent.getActivity(STTarter.getInstance().getContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        } catch (STTarter.ContextNotInitializedException e) {
            e.printStackTrace();
        }

        mainText = "You have " + messagesCount + " unread " + ((messagesCount==1)? "message" : "messages") + " in " + topicsCount + ((topicsCount==1)? " topic" : " topics");
        Log.d("NotificationHelper", "messages count - " + messagesCount + ", topics count - " + topicsCount + ", intent string: " + intentJSON.toString());

        try {
            builder = new NotificationCompat.Builder(STTarter.getInstance().getContext());
        } catch (STTarter.ContextNotInitializedException e) {
            e.printStackTrace();
        }
        try {
            builder.setContentTitle(STTarter.getInstance().getContext().getResources().getString(R.string.app_name))
                    .setContentText(mainText)
                    .setContentIntent(pIntent)
                    //.addAction(android.R.drawable.stat_notify_chat, "Open", pIntent)
                    .setSmallIcon(R.mipmap.autosense)
                    .setCategory(Notification.CATEGORY_EVENT)
                    .setGroupSummary(true)
                    .setGroup("sttarter")
                    .setAutoCancel(true);
        } catch (STTarter.ContextNotInitializedException e) {
            e.printStackTrace();
        }
                /*
                .setStyle(new NotificationCompat.BigTextStyle()
                        .setBigContentTitle("IndusEdge")
                        .bigText("you have unread messages in " + groupsString))
                        //.addLine("you have unread messages in " + groupsString)
                        //.addLine("Line 2")
                        //.setSummaryText("Total unread messages")
                        //.setBigContentTitle("IndusEdge"))

                        //.setNumber(2)
                        .setContentIntent(pIntent)
                        .addAction(android.R.drawable.stat_notify_chat, "Open", pIntent)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setCategory(Notification.CATEGORY_EVENT)
                        .setGroupSummary(true)
                        .setGroup("sttarter")
                .setAutoCancel(true);
                */
        Notification notification = builder.build();

        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(123456, notification);
    }

    public static void displayUnreadMessageNotification() {

        try {
            notificationManager = (NotificationManager) STTarter.getInstance().getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        } catch (STTarter.ContextNotInitializedException e) {
            e.printStackTrace();
        }
        NotificationCompat.Builder builder = null;
        try {
            builder = new NotificationCompat.Builder(STTarter.getInstance().getContext());
        } catch (STTarter.ContextNotInitializedException e) {
            e.printStackTrace();
        }
        try {
            builder.setContentTitle(STTarter.getInstance().getContext().getResources().getString(R.string.app_name))
                    .setContentText("you have unread messages")
                    .setStyle(new NotificationCompat.InboxStyle()
                            .addLine("you have unread messages ")
                            //.addLine("Line 2")
                            .setSummaryText("Total unread messages")
                            .setBigContentTitle(STTarter.getInstance().getContext().getResources().getString(R.string.app_name)))
                    .setNumber(2)
                    .setSmallIcon(R.mipmap.autosense)
                    .setCategory(Notification.CATEGORY_EVENT)
                    .setGroupSummary(true)
                    .setGroup("sttarter");
        } catch (STTarter.ContextNotInitializedException e) {
            e.printStackTrace();
        }
        Notification notification = builder.build();
        notificationManager.notify(123456, notification);
    }

}
