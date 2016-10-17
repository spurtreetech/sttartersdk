package com.spurtreetech.sttarter.lib.provider.messages;

import com.spurtreetech.sttarter.lib.provider.base.BaseModel;

import java.util.Date;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * User messages sent or received for current client handle
 */
public interface MessagesModel extends BaseModel {

    /**
     * Get the {@code message_type} value.
     * Cannot be {@code null}.
     */
    @NonNull
    String getMessageType();

    /**
     * Get the {@code message_text} value.
     * Cannot be {@code null}.
     */
    @NonNull
    String getMessageText();

    /**
     * Get the {@code message_topic} value.
     * Cannot be {@code null}.
     */
    @NonNull
    String getMessageTopic();

    /**
     * Get the {@code message_topic_id} value.
     */
    long getMessageTopicId();

    /**
     * Get the {@code file_type} value.
     * Cannot be {@code null}.
     */
    @NonNull
    String getFileType();

    /**
     * Get the {@code file_url} value.
     * Cannot be {@code null}.
     */
    @NonNull
    String getFileUrl();

    /**
     * Get the {@code is_sender} value.
     */
    boolean getIsSender();

    /**
     * Get the {@code is_delivered} value.
     */
    boolean getIsDelivered();

    /**
     * Get the {@code is_read} value.
     */
    boolean getIsRead();

    /**
     * Get the {@code message_from} value.
     * Cannot be {@code null}.
     */
    @NonNull
    String getMessageFrom();

    /**
     * Get the {@code message_timestamp} value.
     * Cannot be {@code null}.
     */
    @NonNull
    Date getMessageTimestamp();

    /**
     * Get the {@code unix_timestamp} value.
     * Cannot be {@code null}.
     */
    @NonNull
    Date getUnixTimestamp();

    /**
     * Get the {@code message_hash} value.
     * Cannot be {@code null}.
     */
    @NonNull
    String getMessageHash();
}
