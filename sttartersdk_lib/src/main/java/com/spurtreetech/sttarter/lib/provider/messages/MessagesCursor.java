package com.spurtreetech.sttarter.lib.provider.messages;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.spurtreetech.sttarter.lib.provider.base.AbstractCursor;
import com.spurtreetech.sttarter.lib.provider.topics.TopicsColumns;

import java.util.Date;

/**
 * Cursor wrapper for the {@code messages} table.
 */
public class MessagesCursor extends AbstractCursor implements MessagesModel {
    public MessagesCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Primary key.
     */
    public long getId() {
        Long res = getLongOrNull(MessagesColumns._ID);
        if (res == null)
            throw new NullPointerException("The value of '_id' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code message_type} value.
     * Cannot be {@code null}.
     */
    @NonNull
    public String getMessageType() {
        String res = getStringOrNull(MessagesColumns.MESSAGE_TYPE);
        if (res == null)
            throw new NullPointerException("The value of 'message_type' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code message_text} value.
     * Cannot be {@code null}.
     */
    @NonNull
    public String getMessageText() {
        String res = getStringOrNull(MessagesColumns.MESSAGE_TEXT);
        if (res == null)
            throw new NullPointerException("The value of 'message_text' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code message_topic} value.
     * Cannot be {@code null}.
     */
    @NonNull
    public String getMessageTopic() {
        String res = getStringOrNull(MessagesColumns.MESSAGE_TOPIC);
        if (res == null)
            throw new NullPointerException("The value of 'message_topic' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code message_topic_id} value.
     */
    public long getMessageTopicId() {
        Long res = getLongOrNull(MessagesColumns.MESSAGE_TOPIC_ID);
        if (res == null)
            throw new NullPointerException("The value of 'message_topic_id' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code topic_name} value.
     * Cannot be {@code null}.
     */
    @NonNull
    public String getTopicsTopicName() {
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
    public String getTopicsTopicType() {
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
    public String getTopicsTopicMeta() {
        String res = getStringOrNull(TopicsColumns.TOPIC_META);
        if (res == null)
            throw new NullPointerException("The value of 'topic_meta' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code topic_is_subscribed} value.
     */
    public boolean getTopicsTopicIsSubscribed() {
        Boolean res = getBooleanOrNull(TopicsColumns.TOPIC_IS_SUBSCRIBED);
        if (res == null)
            throw new NullPointerException("The value of 'topic_is_subscribed' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code topic_is_public} value.
     */
    public boolean getTopicsTopicIsPublic() {
        Boolean res = getBooleanOrNull(TopicsColumns.TOPIC_IS_PUBLIC);
        if (res == null)
            throw new NullPointerException("The value of 'topic_is_public' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code topic_updated_unix_timestamp} value.
     */
    public long getTopicsTopicUpdatedUnixTimestamp() {
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
    public String getTopicsTopicGroupMembers() {
        String res = getStringOrNull(TopicsColumns.TOPIC_GROUP_MEMBERS);
        if (res == null)
            throw new NullPointerException("The value of 'topic_group_members' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code file_type} value.
     * Cannot be {@code null}.
     */
    @NonNull
    public String getFileType() {
        String res = getStringOrNull(MessagesColumns.FILE_TYPE);
        if (res == null)
            throw new NullPointerException("The value of 'file_type' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code file_url} value.
     * Cannot be {@code null}.
     */
    @NonNull
    public String getFileUrl() {
        String res = getStringOrNull(MessagesColumns.FILE_URL);
        if (res == null)
            throw new NullPointerException("The value of 'file_url' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code is_sender} value.
     */
    public boolean getIsSender() {
        Boolean res = getBooleanOrNull(MessagesColumns.IS_SENDER);
        if (res == null)
            throw new NullPointerException("The value of 'is_sender' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code is_delivered} value.
     */
    public boolean getIsDelivered() {
        Boolean res = getBooleanOrNull(MessagesColumns.IS_DELIVERED);
        if (res == null)
            throw new NullPointerException("The value of 'is_delivered' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code is_read} value.
     */
    public boolean getIsRead() {
        Boolean res = getBooleanOrNull(MessagesColumns.IS_READ);
        if (res == null)
            throw new NullPointerException("The value of 'is_read' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code message_from} value.
     * Cannot be {@code null}.
     */
    @NonNull
    public String getMessageFrom() {
        String res = getStringOrNull(MessagesColumns.MESSAGE_FROM);
        if (res == null)
            throw new NullPointerException("The value of 'message_from' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code message_timestamp} value.
     * Cannot be {@code null}.
     */
    @NonNull
    public Date getMessageTimestamp() {
        Date res = getDateOrNull(MessagesColumns.MESSAGE_TIMESTAMP);
        if (res == null)
            throw new NullPointerException("The value of 'message_timestamp' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code unix_timestamp} value.
     * Cannot be {@code null}.
     */
    @NonNull
    public Date getUnixTimestamp() {
        Date res = getDateOrNull(MessagesColumns.UNIX_TIMESTAMP);
        if (res == null)
            throw new NullPointerException("The value of 'unix_timestamp' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code message_hash} value.
     * Cannot be {@code null}.
     */
    @NonNull
    public String getMessageHash() {
        String res = getStringOrNull(MessagesColumns.MESSAGE_HASH);
        if (res == null)
            throw new NullPointerException("The value of 'message_hash' in the database was null, which is not allowed according to the model definition");
        return res;
    }
}
