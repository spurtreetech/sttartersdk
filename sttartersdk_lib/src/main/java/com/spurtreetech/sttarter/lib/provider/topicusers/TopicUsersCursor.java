package com.spurtreetech.sttarter.lib.provider.topicusers;

import java.util.Date;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.spurtreetech.sttarter.lib.provider.base.AbstractCursor;
import com.spurtreetech.sttarter.lib.provider.topics.*;
import com.spurtreetech.sttarter.lib.provider.users.*;

/**
 * Cursor wrapper for the {@code topic_users} table.
 */
public class TopicUsersCursor extends AbstractCursor implements TopicUsersModel {
    public TopicUsersCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Primary key.
     */
    public long getId() {
        Long res = getLongOrNull(TopicUsersColumns._ID);
        if (res == null)
            throw new NullPointerException("The value of '_id' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code message_topic_id} value.
     */
    public long getMessageTopicId() {
        Long res = getLongOrNull(TopicUsersColumns.MESSAGE_TOPIC_ID);
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
     * Get the {@code user_id} value.
     */
    public long getUserId() {
        Long res = getLongOrNull(TopicUsersColumns.USER_ID);
        if (res == null)
            throw new NullPointerException("The value of 'user_id' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code users_user_id} value.
     */
    public long getUsersUsersUserId() {
        Long res = getLongOrNull(UsersColumns.USERS_USER_ID);
        if (res == null)
            throw new NullPointerException("The value of 'users_user_id' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code users_name} value.
     * Cannot be {@code null}.
     */
    @NonNull
    public String getUsersUsersName() {
        String res = getStringOrNull(UsersColumns.USERS_NAME);
        if (res == null)
            throw new NullPointerException("The value of 'users_name' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code users_username} value.
     * Cannot be {@code null}.
     */
    @NonNull
    public String getUsersUsersUsername() {
        String res = getStringOrNull(UsersColumns.USERS_USERNAME);
        if (res == null)
            throw new NullPointerException("The value of 'users_username' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code users_email} value.
     * Cannot be {@code null}.
     */
    @NonNull
    public String getUsersUsersEmail() {
        String res = getStringOrNull(UsersColumns.USERS_EMAIL);
        if (res == null)
            throw new NullPointerException("The value of 'users_email' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code users_mobile} value.
     * Cannot be {@code null}.
     */
    @NonNull
    public String getUsersUsersMobile() {
        String res = getStringOrNull(UsersColumns.USERS_MOBILE);
        if (res == null)
            throw new NullPointerException("The value of 'users_mobile' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code users_avatar} value.
     * Cannot be {@code null}.
     */
    @NonNull
    public String getUsersUsersAvatar() {
        String res = getStringOrNull(UsersColumns.USERS_AVATAR);
        if (res == null)
            throw new NullPointerException("The value of 'users_avatar' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code users_meta} value.
     * Cannot be {@code null}.
     */
    @NonNull
    public String getUsersUsersMeta() {
        String res = getStringOrNull(UsersColumns.USERS_META);
        if (res == null)
            throw new NullPointerException("The value of 'users_meta' in the database was null, which is not allowed according to the model definition");
        return res;
    }
}
