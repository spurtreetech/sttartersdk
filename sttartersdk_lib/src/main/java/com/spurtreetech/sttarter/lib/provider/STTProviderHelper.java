package com.spurtreetech.sttarter.lib.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.google.gson.Gson;
import com.spurtreetech.sttarter.lib.helper.STTarter;
import com.spurtreetech.sttarter.lib.helper.hash.HashGenerationException;
import com.spurtreetech.sttarter.lib.helper.hash.HashGeneratorUtils;
import com.spurtreetech.sttarter.lib.helper.models.PayloadData;
import com.spurtreetech.sttarter.lib.helper.models.Topic;
import com.spurtreetech.sttarter.lib.helper.models.Member_data;
import com.spurtreetech.sttarter.lib.provider.messages.MessagesContentValues;
import com.spurtreetech.sttarter.lib.provider.messages.MessagesCursor;
import com.spurtreetech.sttarter.lib.provider.messages.MessagesSelection;
import com.spurtreetech.sttarter.lib.provider.topics.TopicsColumns;
import com.spurtreetech.sttarter.lib.provider.topics.TopicsContentValues;
import com.spurtreetech.sttarter.lib.provider.topics.TopicsCursor;
import com.spurtreetech.sttarter.lib.provider.topics.TopicsSelection;
import com.spurtreetech.sttarter.lib.provider.topicusers.TopicUsersSelection;
import com.spurtreetech.sttarter.lib.provider.users.UsersColumns;
import com.spurtreetech.sttarter.lib.provider.users.UsersContentValues;
import com.spurtreetech.sttarter.lib.provider.users.UsersSelection;

import java.util.ArrayList;

/**
 * Created by rahul on 25/08/15.
 */
public class STTProviderHelper {



    public synchronized void insertTopics(ArrayList<Topic> data, boolean subscribedTopics) {

        //TopicsContentValues[] tcv = new TopicsContentValues[data.size()];

        ArrayList<TopicsContentValues> tcv = new ArrayList<>();
        int count = 0;
        for (Topic tempTopic: data) {

            try {

                TopicsSelection where = new TopicsSelection();
                where.topicName(tempTopic.getTopic());

                // Check if current topic is already inserted
                Cursor c = STTarter.getInstance().getContext().getContentResolver().query(TopicsColumns.CONTENT_URI, new String[]{TopicsColumns._ID}, where.sel(), where.args(), null);
                TopicsContentValues temp = new TopicsContentValues();

                // If already present then update else insert
                if(c!=null && c.getCount()>0) {

                    // update subscribed status of topics from "mytopics" API
                    if(subscribedTopics==true) {
                        temp.putTopicIsSubscribed(subscribedTopics);
                        temp.putTopicIsPublic((tempTopic.getIs_public() == 1) ? true : false);

                        Gson gson = new Gson();
                        String sttJson = gson.toJson(tempTopic.getMeta());
                        String groupMembersJson = gson.toJson(tempTopic.getGroup_members());

                        temp.putTopicMeta(sttJson);
                        temp.putTopicType(tempTopic.getType());
                        temp.putTopicGroupMembers(groupMembersJson);
                        temp.update(STTarter.getInstance().getContext().getContentResolver(), where);
                    }

                } else {

                    temp.putTopicName(tempTopic.getTopic());
                    temp.putTopicType(tempTopic.getType());
                    temp.putTopicIsPublic((tempTopic.getIs_public() == 1) ? true : false);
                    temp.putTopicUpdatedUnixTimestamp(1);
                    Gson gson = new Gson();
                    String sttJson = gson.toJson(tempTopic.getMeta());
                    String groupMembersJson = gson.toJson(tempTopic.getGroup_members());

                    temp.putTopicMeta(sttJson);
                    temp.putTopicGroupMembers(groupMembersJson);
                    if(subscribedTopics==true)
                        temp.putTopicIsSubscribed(subscribedTopics);

                    tcv.add(temp);
                    count++;

                }


            } catch (STTarter.ContextNotInitializedException e) {
                e.printStackTrace();
            }

            // Bulk insert all rows that do not exist
            if(count>0) {
                ContentValues[] cv = new ContentValues[count];
                count = 0;
                for (TopicsContentValues tempTcv : tcv) {
                    cv[count] = tempTcv.values();
                }
                try {
                    STTarter.getInstance().getContext().getContentResolver().bulkInsert(TopicsColumns.CONTENT_URI, cv);
                } catch (STTarter.ContextNotInitializedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public synchronized void insertMessage(PayloadData pd, boolean is_sender, boolean is_delivered) {

        TopicsSelection whereTopics = new TopicsSelection();
        whereTopics.topicName(pd.getPayload().getTopic());
        TopicsCursor tc = null;
        try {
            tc = whereTopics.query(STTarter.getInstance().getContext());
            tc.moveToFirst();
        } catch (STTarter.ContextNotInitializedException e) {
            e.printStackTrace();
        }

        Log.d("ContentProvider><><>>", pd.getPayload().toString());

        MessagesContentValues mcv = new MessagesContentValues();
        mcv.putMessageText(pd.getPayload().getMessage());
        mcv.putMessageTopic(pd.getPayload().getTopic());
        mcv.putMessageType("message");
        mcv.putIsSender(is_sender);
        mcv.putIsDelivered(is_delivered);
        mcv.putIsRead(is_sender ? true : false);
        mcv.putMessageTimestamp(Long.parseLong(pd.getTimestamp()));
        mcv.putMessageTopicId(tc.getId());
        mcv.putUnixTimestamp(Long.parseLong(pd.getTimestamp()));
        mcv.putMessageFrom(pd.getFrom());
        mcv.putMessageHash(getMessageHash(pd.getPayload().getMessage(), pd.getPayload().getTopic(), Long.parseLong(pd.getTimestamp())));
        mcv.putFileType((pd.getFile_type() == null) ? "none" : pd.getFile_type());
        mcv.putFileUrl((pd.getFile_url() == null) ? "none" : pd.getFile_url());

        try {
            mcv.insert(STTarter.getInstance().getContext());
            // Once message inserted update the last active timestamp for that topic
            updateTopicActiveTime(pd);
            Log.d(getClass().getSimpleName(), "Inserted Message: " + pd.getPayload().getMessage());
        } catch (STTarter.ContextNotInitializedException e) {
            e.printStackTrace();
        }
    }


    public synchronized void insertUsers(ArrayList<Member_data> data) {

        //TopicsContentValues[] tcv = new TopicsContentValues[data.size()];

        ArrayList<UsersContentValues> tcv = new ArrayList<>();
        int count = 0;
        for (Member_data tempUser: data) {

            try {

                UsersSelection where = new UsersSelection();
                where.usersUserId(tempUser.getStt_id());

                // Check if current topic is already inserted
                Cursor c = STTarter.getInstance().getContext().getContentResolver().query(UsersColumns.CONTENT_URI, new String[]{UsersColumns._ID}, where.sel(), where.args(), null);
                UsersContentValues temp = new UsersContentValues();

                // If already present then update else insert
                if(c!=null && c.getCount()>0) {

                    // update subscribed status of topics from "mytopics" API
                    temp.putUsersUserId(tempUser.getStt_id());
                    temp.putUsersName((tempUser.getName()==null ? "":tempUser.getName()));
                    temp.putUsersUsername((tempUser.getUsername()==null ? "":tempUser.getUsername()));
                    temp.putUsersMobile((tempUser.getMobile()==null ? "":tempUser.getMobile()));
                    temp.putUsersAvatar((tempUser.getAvatar()==null ? "":tempUser.getAvatar()));

                        Gson gson = new Gson();
                        String sttJson = gson.toJson((tempUser.getMeta()==null ? "":tempUser.getMeta()));

                    temp.putUsersMeta(sttJson);
                    temp.putUsersEmail((tempUser.getEmail()==null ? "":tempUser.getEmail()));
                    temp.update(STTarter.getInstance().getContext().getContentResolver(), where);

                } else {

                    temp.putUsersUserId(tempUser.getStt_id());
                    temp.putUsersName((tempUser.getName()==null ? "":tempUser.getName()));
                    temp.putUsersUsername((tempUser.getUsername()==null ? "":tempUser.getUsername()));
                    temp.putUsersMobile((tempUser.getMobile()==null ? "":tempUser.getMobile()));
                    temp.putUsersAvatar((tempUser.getAvatar()==null ? "":tempUser.getAvatar()));

                    Gson gson = new Gson();
                    String sttJson = gson.toJson((tempUser.getMeta()==null ? "":tempUser.getMeta()));

                    temp.putUsersMeta(sttJson);
                    temp.putUsersEmail((tempUser.getEmail()==null ? "":tempUser.getEmail()));

                    tcv.add(temp);
                    count++;

                }


            } catch (STTarter.ContextNotInitializedException e) {
                e.printStackTrace();
            }

            // Bulk insert all rows that do not exist
            if(count>0) {
                ContentValues[] cv = new ContentValues[count];
                count = 0;
                for (UsersContentValues tempTcv : tcv) {
                    cv[count] = tempTcv.values();
                }
                try {
                    STTarter.getInstance().getContext().getContentResolver().bulkInsert(UsersColumns.CONTENT_URI, cv);
                } catch (STTarter.ContextNotInitializedException e) {
                    e.printStackTrace();
                }
            }

        }
    }


    public void updateMessageSentStatus(PayloadData pd) {

        MessagesSelection where = new MessagesSelection();
        where.messageHash(getMessageHash(pd.getPayload().getMessage(), pd.getPayload().getTopic(), Long.parseLong(pd.getTimestamp())));

        MessagesContentValues mcv = new MessagesContentValues();
        mcv.putMessageHash(getMessageHash(pd.getPayload().getMessage(), pd.getPayload().getTopic(), Long.parseLong(pd.getTimestamp())));
        try {
            mcv.update(STTarter.getInstance().getContext(), where);
        } catch (STTarter.ContextNotInitializedException e) {
            e.printStackTrace();
        }
    }

    public TopicsCursor getTopicData(String topicName) {
        TopicsSelection where = new TopicsSelection();
        where.topicName(topicName);
        try {
            return where.query(STTarter.getInstance().getContext());
        } catch (STTarter.ContextNotInitializedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int updateTopicSubscribe (String topic, int subscribed) {
        TopicsSelection where = new TopicsSelection();
        where.topicName(topic);

        TopicsContentValues tcv = new TopicsContentValues();
        tcv.putTopicIsSubscribed((subscribed == 1) ? true : false);
        try {
            return tcv.update(STTarter.getInstance().getContext(), where);
        } catch (STTarter.ContextNotInitializedException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public MessagesCursor getLastMessageForTopic(String topic) {
        MessagesSelection where = new MessagesSelection();
        // TODO modify query to fetch single row only
        where.topicsTopicName(topic).orderByUnixTimestamp(true);
        try {
            return where.query(STTarter.getInstance().getContext());
        } catch (STTarter.ContextNotInitializedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Finds if a message already exists in the local db, by checking its hash
     * @param message message text
     * @param topic name of the topic (as in Mqtt)
     * @param timestamp the unix timestamp
     * @return
     */
    public MessagesCursor findMessage(String message, String topic, Long timestamp) {
        MessagesSelection where = new MessagesSelection();
        where.messageHash(getMessageHash(message, topic, timestamp));
        try {
            return where.query(STTarter.getInstance().getContext());
        } catch (STTarter.ContextNotInitializedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateTopicActiveTime(PayloadData pd) {

        TopicsSelection where = new TopicsSelection();
        where.topicName(pd.getPayload().getTopic());

        TopicsContentValues tcv = new TopicsContentValues();
        tcv.putTopicUpdatedUnixTimestamp(Long.parseLong(pd.getTimestamp()));

        try {
            tcv.update(STTarter.getInstance().getContext(), where);
        } catch (STTarter.ContextNotInitializedException e) {
            e.printStackTrace();
        }
    }

    public Cursor getUnreadMessageCount() {

        try {
            return STTSQLiteOpenHelper.getInstance(STTarter.getInstance().getContext()).getReadableDatabase().rawQuery("SELECT messages.message_topic, count(messages.message_topic) as MESSAGE_COUNT, topics.topic_meta FROM messages JOIN topics ON (messages.message_topic_id=topics._id) WHERE is_read = 0 GROUP BY messages.message_topic", null);
        } catch (STTarter.ContextNotInitializedException e) {
            e.printStackTrace();
        }
        return null;

    }

    public int getUnreadMessageCountForTopic(String topic) {

        MessagesSelection where = new MessagesSelection();
        where.isRead(false).and().topicsTopicName(topic);

        try {
            Cursor temp = where.query(STTarter.getInstance().getContext());
            return temp.getCount();
        } catch (STTarter.ContextNotInitializedException e) {
            e.printStackTrace();
        }

        return 0;

    }

    public int updateMessageRead(String topic) {

        MessagesSelection where = new MessagesSelection();
        where.isRead(false).and().messageTopic(topic);

        MessagesContentValues mcv = new MessagesContentValues();
        mcv.putIsRead(true);

        try {
            return mcv.update(STTarter.getInstance().getContext(), where);
        } catch (STTarter.ContextNotInitializedException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public String getMessageHash(String message, String topic, Long timestamp) {
        try {
            return HashGeneratorUtils.generateMD5(topic + " - " + message + " - " + timestamp);
            //Log.d(this.getClass().getSimpleName(), "message: " + temp + ", generated hash: " + hash);
        } catch (HashGenerationException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void emptyAllTable() {
        MessagesSelection whereMessages = new MessagesSelection();
        whereMessages.addRaw("1");

        TopicsSelection whereTopics = new TopicsSelection();
        whereTopics.addRaw("1");

        UsersSelection whereUsers = new UsersSelection();
        whereUsers.addRaw("1");

        TopicUsersSelection whereTopicsUsers = new TopicUsersSelection();
        whereTopicsUsers.addRaw("1");

        try {
            whereMessages.delete(STTarter.getInstance().getContext());
            whereTopics.delete(STTarter.getInstance().getContext());
            whereUsers.delete(STTarter.getInstance().getContext());
            whereTopicsUsers.delete(STTarter.getInstance().getContext());
        } catch (STTarter.ContextNotInitializedException e) {
            e.printStackTrace();
        }
    }

}
