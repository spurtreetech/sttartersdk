package com.spurtreetech.sttarter.lib.provider.messages;

import android.net.Uri;
import android.provider.BaseColumns;

import com.spurtreetech.sttarter.lib.provider.STTContentProvider;
import com.spurtreetech.sttarter.lib.provider.topics.TopicsColumns;

/**
 * User messages sent or received for current client handle
 */
public class MessagesColumns implements BaseColumns {
    public static final String TABLE_NAME = "messages";
    public static final Uri CONTENT_URI = Uri.parse(STTContentProvider.CONTENT_URI_BASE + "/" + TABLE_NAME);

    /**
     * Primary key.
     */
    public static final String _ID = BaseColumns._ID;

    public static final String MESSAGE_TYPE = "message_type";

    public static final String MESSAGE_TEXT = "message_text";

    public static final String MESSAGE_TOPIC = "message_topic";

    public static final String MESSAGE_TOPIC_ID = "message_topic_id";

    public static final String FILE_TYPE = "file_type";

    public static final String FILE_URL = "file_url";

    public static final String IS_SENDER = "is_sender";

    public static final String IS_DELIVERED = "is_delivered";

    public static final String IS_READ = "is_read";

    public static final String MESSAGE_FROM = "message_from";

    public static final String MESSAGE_TIMESTAMP = "message_timestamp";

    public static final String UNIX_TIMESTAMP = "unix_timestamp";

    public static final String MESSAGE_HASH = "message_hash";


    public static final String DEFAULT_ORDER = TABLE_NAME + "." +_ID;

    // @formatter:off
    public static final String[] ALL_COLUMNS = new String[] {
            _ID,
            MESSAGE_TYPE,
            MESSAGE_TEXT,
            MESSAGE_TOPIC,
            MESSAGE_TOPIC_ID,
            FILE_TYPE,
            FILE_URL,
            IS_SENDER,
            IS_DELIVERED,
            IS_READ,
            MESSAGE_FROM,
            MESSAGE_TIMESTAMP,
            UNIX_TIMESTAMP,
            MESSAGE_HASH
    };
    // @formatter:on

    public static boolean hasColumns(String[] projection) {
        if (projection == null) return true;
        for (String c : projection) {
            if (c.equals(MESSAGE_TYPE) || c.contains("." + MESSAGE_TYPE)) return true;
            if (c.equals(MESSAGE_TEXT) || c.contains("." + MESSAGE_TEXT)) return true;
            if (c.equals(MESSAGE_TOPIC) || c.contains("." + MESSAGE_TOPIC)) return true;
            if (c.equals(MESSAGE_TOPIC_ID) || c.contains("." + MESSAGE_TOPIC_ID)) return true;
            if (c.equals(FILE_TYPE) || c.contains("." + FILE_TYPE)) return true;
            if (c.equals(FILE_URL) || c.contains("." + FILE_URL)) return true;
            if (c.equals(IS_SENDER) || c.contains("." + IS_SENDER)) return true;
            if (c.equals(IS_DELIVERED) || c.contains("." + IS_DELIVERED)) return true;
            if (c.equals(IS_READ) || c.contains("." + IS_READ)) return true;
            if (c.equals(MESSAGE_FROM) || c.contains("." + MESSAGE_FROM)) return true;
            if (c.equals(MESSAGE_TIMESTAMP) || c.contains("." + MESSAGE_TIMESTAMP)) return true;
            if (c.equals(UNIX_TIMESTAMP) || c.contains("." + UNIX_TIMESTAMP)) return true;
            if (c.equals(MESSAGE_HASH) || c.contains("." + MESSAGE_HASH)) return true;
        }
        return false;
    }

    public static final String PREFIX_TOPICS = TABLE_NAME + "__" + TopicsColumns.TABLE_NAME;
}
