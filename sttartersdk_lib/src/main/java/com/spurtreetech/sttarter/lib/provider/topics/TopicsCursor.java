package com.spurtreetech.sttarter.lib.provider.topics;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.spurtreetech.sttarter.lib.provider.base.AbstractCursor;

/**
 * Cursor wrapper for the {@code topics} table.
 */
public class TopicsCursor extends AbstractCursor implements TopicsModel {
    public TopicsCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Primary key.
     */
    public long getId() {
        Long res = getLongOrNull(TopicsColumns._ID);
        if (res == null)
            throw new NullPointerException("The value of '_id' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code topic_name} value.
     * Cannot be {@code null}.
     */
    @NonNull
    public String getTopicName() {
        String res = getStringOrNull(TopicsColumns.TOPIC_NAME);
        if (res == null)
            throw new NullPointerException("The value of 'topic_name' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code topic_type} value.
     * Cannot be {@code null}.
     */
    @NonNull
    public String getTopicType() {
        String res = getStringOrNull(TopicsColumns.TOPIC_TYPE);
        if (res == null)
            throw new NullPointerException("The value of 'topic_type' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code topic_meta} value.
     * Cannot be {@code null}.
     */
    @NonNull
    public String getTopicMeta() {
        String res = getStringOrNull(TopicsColumns.TOPIC_META);
        if (res == null)
            throw new NullPointerException("The value of 'topic_meta' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code topic_is_subscribed} value.
     */
    public boolean getTopicIsSubscribed() {
        Boolean res = getBooleanOrNull(TopicsColumns.TOPIC_IS_SUBSCRIBED);
        if (res == null)
            throw new NullPointerException("The value of 'topic_is_subscribed' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code topic_is_public} value.
     */
    public boolean getTopicIsPublic() {
        Boolean res = getBooleanOrNull(TopicsColumns.TOPIC_IS_PUBLIC);
        if (res == null)
            throw new NullPointerException("The value of 'topic_is_public' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code topic_updated_unix_timestamp} value.
     */
    public long getTopicUpdatedUnixTimestamp() {
        Long res = getLongOrNull(TopicsColumns.TOPIC_UPDATED_UNIX_TIMESTAMP);
        if (res == null)
            throw new NullPointerException("The value of 'topic_updated_unix_timestamp' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code topic_group_members} value.
     * Cannot be {@code null}.
     */
    @NonNull
    public String getTopicGroupMembers() {
        String res = getStringOrNull(TopicsColumns.TOPIC_GROUP_MEMBERS);
        if (res == null)
            throw new NullPointerException("The value of 'topic_group_members' in the database was null, which is not allowed according to the model definition");
        return res;
    }
}
