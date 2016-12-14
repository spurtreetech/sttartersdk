package com.sttarter.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.google.gson.Gson;
import com.sttarter.common.models.PayloadData;
import com.sttarter.common.models.User;
import com.sttarter.communicator.models.Group;
import com.sttarter.helper.hash.HashGenerationException;
import com.sttarter.helper.hash.HashGeneratorUtils;
import com.sttarter.init.STTarterManager;
import com.sttarter.provider.messages.MessagesContentValues;
import com.sttarter.provider.messages.MessagesCursor;
import com.sttarter.provider.messages.MessagesSelection;
import com.sttarter.provider.topics.TopicsColumns;
import com.sttarter.provider.topics.TopicsContentValues;
import com.sttarter.provider.topics.TopicsCursor;
import com.sttarter.provider.topics.TopicsSelection;
import com.sttarter.provider.topicusers.TopicUsersSelection;
import com.sttarter.provider.users.UsersColumns;
import com.sttarter.provider.users.UsersContentValues;
import com.sttarter.provider.users.UsersSelection;

import java.util.ArrayList;

/**
 * Created by rahul on 25/08/15.
 */
public class STTProviderHelper {



    public synchronized void insertTopics(ArrayList<Group> data, boolean subscribedTopics) {

        //TopicsContentValues[] tcv = new TopicsContentValues[data.size()];

        ArrayList<TopicsContentValues> tcv = new ArrayList<>();
        int count = 0;
        for (Group tempGroup : data) {

            try {

                TopicsSelection where = new TopicsSelection();
                where.topicName(tempGroup.getTopic());

                // Check if current topic is already inserted
                Cursor c = STTarterManager.getInstance().getContext().getContentResolver().query(TopicsColumns.CONTENT_URI, new String[]{TopicsColumns._ID}, where.sel(), where.args(), null);
                TopicsContentValues temp = new TopicsContentValues();

                // If already present then update else insert
                if(c!=null && c.getCount()>0) {

                    // update subscribed status of topics from "mytopics" API
                    if(subscribedTopics==true) {
                        temp.putTopicIsSubscribed(subscribedTopics);
                        temp.putTopicIsPublic((tempGroup.getIs_public() == 1) ? true : false);

                        Gson gson = new Gson();
                        String sttJson = gson.toJson(tempGroup.getMeta());
                        String groupMembersJson = gson.toJson(tempGroup.getGroup_members());

                        temp.putTopicMeta(sttJson);
                        temp.putTopicType(tempGroup.getType());
                        temp.putTopicGroupMembers(groupMembersJson);
                        temp.update(STTarterManager.getInstance().getContext().getContentResolver(), where);
                    }

                } else {

                    temp.putTopicName(tempGroup.getTopic());
                    temp.putTopicType(tempGroup.getType());
                    temp.putTopicIsPublic((tempGroup.getIs_public() == 1) ? true : false);
                    temp.putTopicUpdatedUnixTimestamp(1);
                    Gson gson = new Gson();
                    String sttJson = gson.toJson(tempGroup.getMeta());
                    String groupMembersJson = gson.toJson(tempGroup.getGroup_members());

                    temp.putTopicMeta(sttJson);
                    temp.putTopicGroupMembers(groupMembersJson);
                    if(subscribedTopics==true)
                        temp.putTopicIsSubscribed(subscribedTopics);

                    tcv.add(temp);
                    count++;

                }


            } catch (STTarterManager.ContextNotInitializedException e) {
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
                    STTarterManager.getInstance().getContext().getContentResolver().bulkInsert(TopicsColumns.CONTENT_URI, cv);
                } catch (STTarterManager.ContextNotInitializedException e) {
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
            tc = whereTopics.query(STTarterManager.getInstance().getContext());
            tc.moveToFirst();
        } catch (STTarterManager.ContextNotInitializedException e) {
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
            mcv.insert(STTarterManager.getInstance().getContext());
            // Once message inserted update the last active timestamp for that topic
            updateTopicActiveTime(pd);
            Log.d(getClass().getSimpleName(), "Inserted Message: " + pd.getPayload().getMessage());
        } catch (STTarterManager.ContextNotInitializedException e) {
            e.printStackTrace();
        }
    }


    public synchronized void insertUsers(ArrayList<User> data) {

        //TopicsContentValues[] tcv = new TopicsContentValues[data.size()];

        ArrayList<UsersContentValues> tcv = new ArrayList<>();
        int count = 0;
        for (User tempUser: data) {

            try {

                UsersSelection where = new UsersSelection();
                where.usersUserId(tempUser.getStt_id());

                // Check if current topic is already inserted
                Cursor c = STTarterManager.getInstance().getContext().getContentResolver().query(UsersColumns.CONTENT_URI, new String[]{UsersColumns._ID}, where.sel(), where.args(), null);
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
                    temp.update(STTarterManager.getInstance().getContext().getContentResolver(), where);

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


            } catch (STTarterManager.ContextNotInitializedException e) {
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
                    STTarterManager.getInstance().getContext().getContentResolver().bulkInsert(UsersColumns.CONTENT_URI, cv);
                } catch (STTarterManager.ContextNotInitializedException e) {
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
            mcv.update(STTarterManager.getInstance().getContext(), where);
        } catch (STTarterManager.ContextNotInitializedException e) {
            e.printStackTrace();
        }
    }

    public TopicsCursor getTopicData(String topicName) {
        TopicsSelection where = new TopicsSelection();
        where.topicName(topicName);
        try {
            return where.query(STTarterManager.getInstance().getContext());
        } catch (STTarterManager.ContextNotInitializedException e) {
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
            return tcv.update(STTarterManager.getInstance().getContext(), where);
        } catch (STTarterManager.ContextNotInitializedException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public MessagesCursor getLastMessageForTopic(String topic) {
        MessagesSelection where = new MessagesSelection();
        // TODO modify query to fetch single row only
        where.topicsTopicName(topic).orderByUnixTimestamp(true);
        try {
            return where.query(STTarterManager.getInstance().getContext());
        } catch (STTarterManager.ContextNotInitializedException e) {
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
            return where.query(STTarterManager.getInstance().getContext());
        } catch (STTarterManager.ContextNotInitializedException e) {
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
            tcv.update(STTarterManager.getInstance().getContext(), where);
        } catch (STTarterManager.ContextNotInitializedException e) {
            e.printStackTrace();
        }
    }

    public Cursor getUnreadMessageCount() {

        try {
            return STTSQLiteOpenHelper.getInstance(STTarterManager.getInstance().getContext()).getReadableDatabase().rawQuery("SELECT messages.message_topic, count(messages.message_topic) as MESSAGE_COUNT, topics.topic_meta FROM messages JOIN topics ON (messages.message_topic_id=topics._id) WHERE is_read = 0 GROUP BY messages.message_topic", null);
        } catch (STTarterManager.ContextNotInitializedException e) {
            e.printStackTrace();
        }
        return null;

    }

    public int getUnreadMessageCountForTopic(String topic) {

        MessagesSelection where = new MessagesSelection();
        where.isRead(false).and().topicsTopicName(topic);

        try {
            Cursor temp = where.query(STTarterManager.getInstance().getContext());
            return temp.getCount();
        } catch (STTarterManager.ContextNotInitializedException e) {
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
            return mcv.update(STTarterManager.getInstance().getContext(), where);
        } catch (STTarterManager.ContextNotInitializedException e) {
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

    public void deleteAllTopics() {
            TopicsSelection whereTopics = new TopicsSelection();
            whereTopics.addRaw("1");
            Log.d(getClass().getSimpleName(), "Deleted topics from SQLite DB");
        try {
            whereTopics.delete(STTarterManager.getInstance().getContext());
        } catch (STTarterManager.ContextNotInitializedException e) {
            e.printStackTrace();
        }
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
            whereMessages.delete(STTarterManager.getInstance().getContext());
            whereTopics.delete(STTarterManager.getInstance().getContext());
            whereUsers.delete(STTarterManager.getInstance().getContext());
            whereTopicsUsers.delete(STTarterManager.getInstance().getContext());
        } catch (STTarterManager.ContextNotInitializedException e) {
            e.printStackTrace();
        }
    }

}
