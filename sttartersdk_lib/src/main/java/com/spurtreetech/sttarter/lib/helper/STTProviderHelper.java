package com.spurtreetech.sttarter.lib.helper;

/*
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.spurtreetech.sttarter.lib.helper.hash.HashGenerationException;
import com.spurtreetech.sttarter.lib.helper.hash.HashGeneratorUtils;
import com.spurtreetech.sttarter.lib.helper.models.ChatTopic;
import com.spurtreetech.sttarter.lib.helper.models.Topic;
import com.spurtreetech.sttarter.lib.helper.models.TopicMeta;
import com.spurtreetech.sttarter.lib.helper.utils.DateTimeHelper;

import java.util.ArrayList;

public class STTProviderHelper {

    public synchronized void insertTopics(ArrayList<Topic> data, boolean subscribedTopics) {
        ContentValues values = new ContentValues();
        Uri uri = Uri.parse("content://" + STTContentProvider.AUTHORITY + "/" + DatabaseHelper.TABLE_TOPICS);

        for (Topic tempTopic: data) {
            values.put(DatabaseHelper.COLUMN_TOPIC_NAME,tempTopic.getTopic());
            values.put(DatabaseHelper.COLUMN_TOPIC_TYPE,tempTopic.getType());
            values.put(DatabaseHelper.COLUMN_TOPIC_META,tempTopic.getMeta().toString());
            values.put(DatabaseHelper.COLUMN_TOPIC_IS_PUBLIC,tempTopic.getIs_public());
            //values.put(DatabaseHelper.COLUMN_TOPIC_UNIX_TIMESTAMP, 0);
            if(subscribedTopics==true)
                values.put(DatabaseHelper.COLUMN_TOPIC_IS_SUBSCRIBED, 1 );

            try {
                if(ifTopicExists(tempTopic.getTopic())) {
                    String selection = DatabaseHelper.COLUMN_TOPIC_NAME + "= '" + tempTopic.getTopic() + "'";
                    String[] selectionArgs = {tempTopic.getTopic()};
                    STTarter.getInstance().getContext().getContentResolver().update(uri, values, selection, null);
                } else {
                    STTarter.getInstance().getContext().getContentResolver().insert(uri, values);
                }
            } catch (STTarter.ContextNotInitializedException e) {
                e.printStackTrace();
            }
        }
    }

    public void insertTopic(ChatTopic topic) {
        ContentValues values = new ContentValues();
        Uri uri = Uri.parse("content://" + STTContentProvider.AUTHORITY + "/" + DatabaseHelper.TABLE_TOPICS);

            values.put(DatabaseHelper.COLUMN_TOPIC_NAME, topic.getTopic());
            values.put(DatabaseHelper.COLUMN_TOPIC_TYPE, topic.getType());
            //values.put(DatabaseHelper.COLUMN_TOPIC_META, topic.getMeta());
            try {
                STTarter.getInstance().getContext().getContentResolver().insert(uri, values);
                //STTarter.getInstance().getContext().getContentResolver().notifyChange(uri, null);
            } catch (STTarter.ContextNotInitializedException e) {
                e.printStackTrace();
            }
    }

    public void updateTopicActiveTime(String topicName, String unixTimeStamp) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TOPIC_UNIX_TIMESTAMP, unixTimeStamp);
        Uri uri = Uri.parse("content://" + STTContentProvider.AUTHORITY + "/" + DatabaseHelper.TABLE_TOPICS + "/updateActiveTime");
        String selection = DatabaseHelper.COLUMN_TOPIC_NAME  + " = '" + topicName + "'";
        try {
            STTarter.getInstance().getContext().getContentResolver().update(uri, values, selection, null);
        } catch (STTarter.ContextNotInitializedException e) {
            e.printStackTrace();
        }
    }

    public void insertMessage(String message, String topic, boolean is_sender, boolean is_delivered, String timestamp, String from) {

        ContentValues values = new ContentValues();
        Uri uri = STTContentProvider.MESSAGES_URI;  // "content://" + STTContentProvider.AUTHORITY + "/" + DatabaseHelper.TABLE_MESSEGES
        values.put(DatabaseHelper.COLUMN_MESSAGE_TEXT, message);
        values.put(DatabaseHelper.COLUMN_MESSAGE_TOPIC, topic);
        values.put(DatabaseHelper.COLUMN_MESSAGE_TYPE, "message");
        values.put(DatabaseHelper.COLUMN_IS_SENDER, is_sender);
        values.put(DatabaseHelper.COLUMN_IS_DELIVERED, is_delivered);
        values.put(DatabaseHelper.COLUMN_MESSAGE_TIMESTAMP, DateTimeHelper.getTimeStampFromUnixTime(timestamp));
        values.put(DatabaseHelper.COLUMN_MESSAGE_FROM, from);
        values.put(DatabaseHelper.COLUMN_MESSAGE_UNIX_TIMESTAMP, timestamp);
        values.put(DatabaseHelper.COLUMN_MESSAGE_HASH, getMessageHash(message, topic, timestamp));

        try {
            STTarter.getInstance().getContext().getContentResolver().insert(uri, values);
            STTarter.getInstance().getContext().getContentResolver().notifyChange(Uri.parse("content://" + STTContentProvider.AUTHORITY + "/" + DatabaseHelper.TABLE_MESSAGES + "/singleTopicMessages"), null);
            Log.d(getClass().getSimpleName(), "Inserted Message: " + message);
        } catch (STTarter.ContextNotInitializedException e) {
            e.printStackTrace();
        }
    }

    public String getMessageHash(String message, String topic, String timestamp) {
        String hash = "";
        String temp = message + " - " + timestamp;
        try {
            hash = HashGeneratorUtils.generateMD5(temp);
            Log.d(this.getClass().getSimpleName(), "message: " + temp + ", generated hash: " + hash);
        } catch (HashGenerationException e) {
            e.printStackTrace();
        }
        return hash;
    }

    public TopicMeta getTopic(String topic) {
        ContentValues values = new ContentValues();
        Uri uri = Uri.parse("content://" + STTContentProvider.AUTHORITY + "/" + DatabaseHelper.TABLE_TOPICS + "/getTopicInfo");
        String selection = DatabaseHelper.COLUMN_TOPIC_NAME + " = '" + topic + "'";
        values.put(DatabaseHelper.COLUMN_TOPIC_NAME, topic);;
        try {
            Gson gson = new Gson();
            Cursor cursor = STTarter.getInstance().getContext().getContentResolver().query(uri, null, selection, null, null);
            if(cursor.getCount()>0) {
                cursor.moveToFirst();
                TopicMeta tm = gson.fromJson(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TOPIC_META)), TopicMeta.class);
                return tm;
            }
            //STTarter.getInstance().getContext().getContentResolver().notifyChange(uri, null);
        } catch (STTarter.ContextNotInitializedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void deleteAllTopics() {
        try {
            STTarter.getInstance().getContext().getContentResolver().delete(STTContentProvider.TOPICS_URI, null, null);
            Log.d(getClass().getSimpleName(), "Deleted topics from SQLite DB");
        } catch (STTarter.ContextNotInitializedException e) {
            e.printStackTrace();
        }
    }

    public Cursor getViewableTopics() {
        ContentValues values = new ContentValues();
        Uri uri = Uri.parse("content://" + STTContentProvider.AUTHORITY + "/" + DatabaseHelper.TABLE_TOPICS);
        try {
            return STTarter.getInstance().getContext().getContentResolver().query(uri, null, null, null, null);
        } catch (STTarter.ContextNotInitializedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Cursor findMessage(String message, String topic, String timestamp) {
        Uri uri = Uri.parse("content://" + STTContentProvider.AUTHORITY + "/" + DatabaseHelper.TABLE_MESSAGES + "/ifMessageExists");    // + getMessageHash(message, topic, timestamp));
        String selection = DatabaseHelper.COLUMN_MESSAGE_HASH + " LIKE '%" + getMessageHash(message, topic, timestamp) + "%'";
        //String selection = DatabaseHelper.COLUMN_MESSAGE_HASH + " = '" + getMessageHash(message, topic, timestamp) + "'";
        try {
            return STTarter.getInstance().getContext().getContentResolver().query(uri, null, selection, null, null);
        } catch (STTarter.ContextNotInitializedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Cursor isMessageBySender(String message, String topic, String timestamp) {

        Uri uri = Uri.parse("content://" + STTContentProvider.AUTHORITY + "/" + DatabaseHelper.TABLE_MESSAGES + "/ifMessageBySender");  //+ getMessageHash(message, topic, timestamp));
        String selection = DatabaseHelper.COLUMN_MESSAGE_HASH + " LIKE '%" + getMessageHash(message, topic, timestamp) + "%' AND " + DatabaseHelper.COLUMN_IS_SENDER + "==1";
        try {
            return STTarter.getInstance().getContext().getContentResolver().query(uri, null, selection, null, null);
        } catch (STTarter.ContextNotInitializedException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Cursor getMessagesForTopic(String topicname) {
        ContentValues values = new ContentValues();
        Uri uri = Uri.parse("content://" + STTContentProvider.AUTHORITY + "/" + DatabaseHelper.TABLE_MESSAGES);
        String selection = DatabaseHelper.COLUMN_MESSAGE_TOPIC + " LIKE '%" + topicname + "%'";
        try {
            return STTarter.getInstance().getContext().getContentResolver().query(uri, null, selection, null, null);
        } catch (STTarter.ContextNotInitializedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void updateMessageSentStatus(String message, String topic, String timestamp) {
        ContentValues values = new ContentValues();
        Uri uri =  Uri.parse("content://" + STTContentProvider.AUTHORITY + "/" + DatabaseHelper.TABLE_MESSAGES + "/updateMessageStatus");   //STTContentProvider.MESSAGES_URI;  //
        values.put(DatabaseHelper.COLUMN_MESSAGE_TEXT, message);
        values.put(DatabaseHelper.COLUMN_MESSAGE_TOPIC, topic);
        values.put(DatabaseHelper.COLUMN_MESSAGE_TYPE, "message");
        values.put(DatabaseHelper.COLUMN_IS_SENDER, 1);
        values.put(DatabaseHelper.COLUMN_IS_DELIVERED, 1);
        values.put(DatabaseHelper.COLUMN_MESSAGE_TIMESTAMP, DateTimeHelper.getTimeStampFromUnixTime(timestamp));
        //values.put(DatabaseHelper.COLUMN_MESSAGE_TIMESTAMP, timestamp);
        //values.put(DatabaseHelper.COLUMN_MESSAGE_FROM, );
        values.put(DatabaseHelper.COLUMN_MESSAGE_UNIX_TIMESTAMP, timestamp);
        values.put(DatabaseHelper.COLUMN_MESSAGE_HASH, getMessageHash(message, topic, timestamp));
        //values.put(DatabaseHelper.COL)
        //values.put(DatabaseHelper.COLUMN_MESSAGE_HASH, getMessageHash(message, topic, timestamp));

        try {
            STTarter.getInstance().getContext().getContentResolver().insert(uri, values);
            //STTarter.getInstance().getContext().getContentResolver().notifyChange(uri, null);
            Log.d(getClass().getSimpleName(), "Updated Message: " + message);
        } catch (STTarter.ContextNotInitializedException e) {
            e.printStackTrace();
        }
    }

    public Cursor getUnreadMessages () {
        ContentValues values = new ContentValues();
        Uri uri = Uri.parse("content://" + STTContentProvider.AUTHORITY + "/" + DatabaseHelper.TABLE_MESSAGES);
        String selection = DatabaseHelper.COLUMN_MESSAGE_TEXT + " LIKE '%i%'";
        try {
            return STTarter.getInstance().getContext().getContentResolver().query(uri, null, selection, null, null);
        } catch (STTarter.ContextNotInitializedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public int isTopicSubscribed(String topicName) {
        ContentValues values = new ContentValues();
        Uri uri = Uri.parse("content://" + STTContentProvider.AUTHORITY + "/" + DatabaseHelper.TABLE_TOPICS + "/isTopicSubscribed");
        String selection = DatabaseHelper.COLUMN_TOPIC_NAME + " = '" + topicName + "'";
        try {
            Cursor cursor = STTarter.getInstance().getContext().getContentResolver().query(uri, null, selection, null, null);
            if(cursor.getCount()>0) {
                cursor.moveToFirst();
                Log.d("STTProviderHelper", " subscribed status - " + cursor.getString(0) + ", " + cursor.getString(1) + ", " + cursor.getString(2) + ", " + cursor.getString(3)  + ", " + cursor.getString(4) + cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_TOPIC_IS_SUBSCRIBED)));
                int isSubscribed = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_TOPIC_IS_SUBSCRIBED));
                cursor.close();
                return isSubscribed;
                //Log.d("STTProviderHelper", cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TOPIC_IS_SUBSCRIBED)));
            }
            //if(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TOPIC_IS_SUBSCRIBED)).equals("1"))
        } catch (STTarter.ContextNotInitializedException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Cursor getMyTopics() {
        ContentValues values = new ContentValues();
        Uri uri = Uri.parse("content://" + STTContentProvider.AUTHORITY + "/" + DatabaseHelper.TABLE_TOPICS + "/getMyTopics");
        String selection = DatabaseHelper.COLUMN_TOPIC_IS_SUBSCRIBED + " = 1 AND NOT " + DatabaseHelper.COLUMN_TOPIC_TYPE + " = 'org' AND NOT " + DatabaseHelper.COLUMN_TOPIC_TYPE + " = 'master'";
        try {
            return STTarter.getInstance().getContext().getContentResolver().query(uri, null, selection, null, null);
        } catch (STTarter.ContextNotInitializedException e) {
            e.printStackTrace();
        }
        // get only topics which have been subscribed by the user
        return null;
    }

    public Cursor getAllTopics() {
        ContentValues values = new ContentValues();
        Uri uri = Uri.parse("content://" + STTContentProvider.AUTHORITY + "/" + DatabaseHelper.TABLE_TOPICS + "/getAllTopics");
        String selectionTopic = "NOT " + DatabaseHelper.COLUMN_TOPIC_TYPE + " = 'org' AND NOT " + DatabaseHelper.COLUMN_TOPIC_TYPE + " = 'master' AND " + DatabaseHelper.COLUMN_TOPIC_IS_PUBLIC + " = 1";
        //String selectionMessage = DatabaseHelper.COLUMN_MESSAGE_TIMESTAMP
        try {
            //STTContentProvider.getDatabase().execSQL("SELECT * FROM " + DatabaseHelper.TABLE_TOPICS + " INNER JOIN " + DatabaseHelper.TABLE_MESSAGES + " ON " + DatabaseHelper.COLUMN_MESSAGE_TOPIC + " WHERE " + selectionTopic + " AND " );
            return STTarter.getInstance().getContext().getContentResolver().query(uri, null, selectionTopic, null, null);
        } catch (STTarter.ContextNotInitializedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Cursor getBuzzFeed() {
        ContentValues values = new ContentValues();
        Uri uri = Uri.parse("content://" + STTContentProvider.AUTHORITY + "/" + DatabaseHelper.TABLE_MESSAGES);
        String buzzFeed = getBuzzFeedTopic();
        if(buzzFeed != null) {
            return null;
        } else {
            String selection = DatabaseHelper.COLUMN_MESSAGE_TOPIC + " = '" + buzzFeed + "'";
            try {
                return STTarter.getInstance().getContext().getContentResolver().query(uri, null, selection, null, null);
            } catch (STTarter.ContextNotInitializedException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public String getBuzzFeedTopic() {
        Uri uri = Uri.parse("content://" + STTContentProvider.AUTHORITY + "/" + DatabaseHelper.TABLE_TOPICS + "/buzzFeedTopic");
        try {
            Cursor cursor = STTarter.getInstance().getContext().getContentResolver().query(uri, null, null, null, null);
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                return cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TOPIC_NAME));
            }
        } catch (STTarter.ContextNotInitializedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Cursor insertOrReplace(ArrayList<Topic> data, boolean subscribedTopics) {
        ContentValues values = new ContentValues();
        Uri uri = Uri.parse("content://" + STTContentProvider.AUTHORITY + "/" + DatabaseHelper.TABLE_TOPICS);

        for (Topic tempTopic: data) {
            values.put(DatabaseHelper.COLUMN_TOPIC_NAME,tempTopic.getTopic());
            values.put(DatabaseHelper.COLUMN_TOPIC_TYPE,tempTopic.getType());
            values.put(DatabaseHelper.COLUMN_TOPIC_META,tempTopic.getMeta().toString());
            values.put(DatabaseHelper.COLUMN_TOPIC_IS_PUBLIC,tempTopic.getIs_public());
            values.put(DatabaseHelper.COLUMN_TOPIC_IS_SUBSCRIBED, subscribedTopics);


            try {
                STTarter.getInstance().getContext().getContentResolver().insert(uri, values);
            } catch (STTarter.ContextNotInitializedException e) {
                e.printStackTrace();
            }
        }

        try {
            STTarter.getInstance().getContext().getContentResolver().notifyChange(uri, null);
        } catch (STTarter.ContextNotInitializedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean ifTopicExists(String topicName) {
        Uri uri = Uri.parse("content://" + STTContentProvider.AUTHORITY + "/" + DatabaseHelper.TABLE_TOPICS + "/ifTopicExists");    // + getMessageHash(message, topic, timestamp));
        //String selection = DatabaseHelper.COLUMN_MESSAGE_HASH + " LIKE '%" + getMessageHash(message, topic, timestamp) + "%'";
        String selection = DatabaseHelper.COLUMN_TOPIC_NAME + " = '" + topicName + "'";

        try {
            Cursor c = STTarter.getInstance().getContext().getContentResolver().query(uri, null, selection, null, null);
            if(c.getCount()>0) {
                return true;
            } else {
                return false;
            }
        } catch (STTarter.ContextNotInitializedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getLatestMessage(String topicName) {

        //Uri uri = Uri.parse("content://" + STTContentProvider.AUTHORITY + "/" + DatabaseHelper.TABLE_MESSAGES + "/getLatestMessage");
        String selection = DatabaseHelper.COLUMN_MESSAGE_TOPIC + " = '" + topicName + "'";
        String query = "SELECT MAX(" + DatabaseHelper.COLUMN_MESSAGE_TIMESTAMP + ") as " + DatabaseHelper.COLUMN_MESSAGE_TIMESTAMP + ", " + DatabaseHelper.COLUMN_MESSAGE_TEXT + " FROM " + DatabaseHelper.TABLE_MESSAGES + " WHERE " + selection;
        Cursor cursor = STTContentProvider.getDatabase().rawQuery(query, null);
        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            String latestMessage =  cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_MESSAGE_TEXT));
            cursor.close();
            return latestMessage;
        }

        return "";
    }

    public int  updateTopicSubscribe(String topicName, int subscribeStatus) {
        Uri uri = Uri.parse("content://" + STTContentProvider.AUTHORITY + "/" + DatabaseHelper.TABLE_TOPICS + "/updateSubscribeStatus");
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TOPIC_IS_SUBSCRIBED, subscribeStatus);
        String selection = DatabaseHelper.COLUMN_TOPIC_NAME  + " = '" + topicName  + "'";
        try {
            int updatedRows = STTarter.getInstance().getContext().getContentResolver().update(uri, values, selection, null);
            return updatedRows;
        } catch (STTarter.ContextNotInitializedException e) {
            e.printStackTrace();
        }
        return 0;
    }

}

*/