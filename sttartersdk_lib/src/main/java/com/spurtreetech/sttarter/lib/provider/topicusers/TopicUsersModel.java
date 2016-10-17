package com.spurtreetech.sttarter.lib.provider.topicusers;

import com.spurtreetech.sttarter.lib.provider.base.BaseModel;

import java.util.Date;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * User and topics id mapping.
 */
public interface TopicUsersModel extends BaseModel {

    /**
     * Get the {@code message_topic_id} value.
     */
    long getMessageTopicId();

    /**
     * Get the {@code user_id} value.
     */
    long getUserId();
}
