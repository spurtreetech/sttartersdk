package com.spurtreetech.sttarter.lib.provider.topics;

import android.support.annotation.NonNull;

import com.spurtreetech.sttarter.lib.provider.base.BaseModel;

/**
 * A topic on Mqtt server which can be subscribed to or published on.
 */
public interface TopicsModel extends BaseModel {

    /**
     * Get the {@code topic_name} value.
     * Cannot be {@code null}.
     */
    @NonNull
    String getTopicName();

    /**
     * Get the {@code topic_type} value.
     * Cannot be {@code null}.
     */
    @NonNull
    String getTopicType();

    /**
     * Get the {@code topic_meta} value.
     * Cannot be {@code null}.
     */
    @NonNull
    String getTopicMeta();

    /**
     * Get the {@code topic_is_subscribed} value.
     */
    boolean getTopicIsSubscribed();

    /**
     * Get the {@code topic_is_public} value.
     */
    boolean getTopicIsPublic();

    /**
     * Get the {@code topic_updated_unix_timestamp} value.
     */
    long getTopicUpdatedUnixTimestamp();

    /**
     * Get the {@code topic_group_members} value.
     * Cannot be {@code null}.
     */
    @NonNull
    String getTopicGroupMembers();
}
