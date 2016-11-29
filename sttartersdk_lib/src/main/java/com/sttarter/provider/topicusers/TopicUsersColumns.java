package com.sttarter.provider.topicusers;

import android.net.Uri;
import android.provider.BaseColumns;

import com.sttarter.provider.STTContentProvider;
import com.sttarter.provider.messages.MessagesColumns;
import com.sttarter.provider.topicusers.TopicUsersColumns;
import com.sttarter.provider.topics.TopicsColumns;
import com.sttarter.provider.users.UsersColumns;

/**
 * User and topics id mapping.
 */
public class TopicUsersColumns implements BaseColumns {
    public static final String TABLE_NAME = "topic_users";
    public static final Uri CONTENT_URI = Uri.parse(STTContentProvider.CONTENT_URI_BASE + "/" + TABLE_NAME);

    /**
     * Primary key.
     */
    public static final String _ID = BaseColumns._ID;

    public static final String MESSAGE_TOPIC_ID = "message_topic_id";

    public static final String USER_ID = "user_id";


    public static final String DEFAULT_ORDER = TABLE_NAME + "." +_ID;

    // @formatter:off
    public static final String[] ALL_COLUMNS = new String[] {
            _ID,
            MESSAGE_TOPIC_ID,
            USER_ID
    };
    // @formatter:on

    public static boolean hasColumns(String[] projection) {
        if (projection == null) return true;
        for (String c : projection) {
            if (c.equals(MESSAGE_TOPIC_ID) || c.contains("." + MESSAGE_TOPIC_ID)) return true;
            if (c.equals(USER_ID) || c.contains("." + USER_ID)) return true;
        }
        return false;
    }

    public static final String PREFIX_TOPICS = TABLE_NAME + "__" + TopicsColumns.TABLE_NAME;
    public static final String PREFIX_USERS = TABLE_NAME + "__" + UsersColumns.TABLE_NAME;
}
