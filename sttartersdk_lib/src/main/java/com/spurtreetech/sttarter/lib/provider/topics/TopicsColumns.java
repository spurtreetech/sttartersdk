package com.spurtreetech.sttarter.lib.provider.topics;

import android.net.Uri;
import android.provider.BaseColumns;

import com.spurtreetech.sttarter.lib.provider.STTContentProvider;

/**
 * A topic on Mqtt server which can be subscribed to or published on.
 */
public class TopicsColumns implements BaseColumns {
    public static final String TABLE_NAME = "topics";
    public static final Uri CONTENT_URI = Uri.parse(STTContentProvider.CONTENT_URI_BASE + "/" + TABLE_NAME);

    /**
     * Primary key.
     */
    public static final String _ID = BaseColumns._ID;

    public static final String TOPIC_NAME = "topic_name";

    public static final String TOPIC_TYPE = "topic_type";

    public static final String TOPIC_META = "topic_meta";

    public static final String TOPIC_IS_SUBSCRIBED = "topic_is_subscribed";

    public static final String TOPIC_IS_PUBLIC = "topic_is_public";

    public static final String TOPIC_UPDATED_UNIX_TIMESTAMP = "topic_updated_unix_timestamp";

    public static final String TOPIC_GROUP_MEMBERS = "topic_group_members";


    public static final String DEFAULT_ORDER = TABLE_NAME + "." +_ID;

    // @formatter:off
    public static final String[] ALL_COLUMNS = new String[] {
            _ID,
            TOPIC_NAME,
            TOPIC_TYPE,
            TOPIC_META,
            TOPIC_IS_SUBSCRIBED,
            TOPIC_IS_PUBLIC,
            TOPIC_UPDATED_UNIX_TIMESTAMP,
            TOPIC_GROUP_MEMBERS
    };
    // @formatter:on

    public static boolean hasColumns(String[] projection) {
        if (projection == null) return true;
        for (String c : projection) {
            if (c.equals(TOPIC_NAME) || c.contains("." + TOPIC_NAME)) return true;
            if (c.equals(TOPIC_TYPE) || c.contains("." + TOPIC_TYPE)) return true;
            if (c.equals(TOPIC_META) || c.contains("." + TOPIC_META)) return true;
            if (c.equals(TOPIC_IS_SUBSCRIBED) || c.contains("." + TOPIC_IS_SUBSCRIBED)) return true;
            if (c.equals(TOPIC_IS_PUBLIC) || c.contains("." + TOPIC_IS_PUBLIC)) return true;
            if (c.equals(TOPIC_UPDATED_UNIX_TIMESTAMP) || c.contains("." + TOPIC_UPDATED_UNIX_TIMESTAMP)) return true;
            if (c.equals(TOPIC_GROUP_MEMBERS) || c.contains("." + TOPIC_GROUP_MEMBERS)) return true;
        }
        return false;
    }

}
