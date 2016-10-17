package com.spurtreetech.sttarter.lib.provider.messages;

import java.util.Date;

import android.content.Context;
import android.content.ContentResolver;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.spurtreetech.sttarter.lib.provider.base.AbstractContentValues;

/**
 * Content values wrapper for the {@code messages} table.
 */
public class MessagesContentValues extends AbstractContentValues {
    @Override
    public Uri uri() {
        return MessagesColumns.CONTENT_URI;
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(ContentResolver contentResolver, @Nullable MessagesSelection where) {
        return contentResolver.update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(Context context, @Nullable MessagesSelection where) {
        return context.getContentResolver().update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    public MessagesContentValues putMessageType(@NonNull String value) {
        if (value == null) throw new IllegalArgumentException("messageType must not be null");
        mContentValues.put(MessagesColumns.MESSAGE_TYPE, value);
        return this;
    }


    public MessagesContentValues putMessageText(@NonNull String value) {
        if (value == null) throw new IllegalArgumentException("messageText must not be null");
        mContentValues.put(MessagesColumns.MESSAGE_TEXT, value);
        return this;
    }


    public MessagesContentValues putMessageTopic(@NonNull String value) {
        if (value == null) throw new IllegalArgumentException("messageTopic must not be null");
        mContentValues.put(MessagesColumns.MESSAGE_TOPIC, value);
        return this;
    }


    public MessagesContentValues putMessageTopicId(long value) {
        mContentValues.put(MessagesColumns.MESSAGE_TOPIC_ID, value);
        return this;
    }


    public MessagesContentValues putFileType(@NonNull String value) {
        if (value == null) throw new IllegalArgumentException("fileType must not be null");
        mContentValues.put(MessagesColumns.FILE_TYPE, value);
        return this;
    }


    public MessagesContentValues putFileUrl(@NonNull String value) {
        if (value == null) throw new IllegalArgumentException("fileUrl must not be null");
        mContentValues.put(MessagesColumns.FILE_URL, value);
        return this;
    }


    public MessagesContentValues putIsSender(boolean value) {
        mContentValues.put(MessagesColumns.IS_SENDER, value);
        return this;
    }


    public MessagesContentValues putIsDelivered(boolean value) {
        mContentValues.put(MessagesColumns.IS_DELIVERED, value);
        return this;
    }


    public MessagesContentValues putIsRead(boolean value) {
        mContentValues.put(MessagesColumns.IS_READ, value);
        return this;
    }


    public MessagesContentValues putMessageFrom(@NonNull String value) {
        if (value == null) throw new IllegalArgumentException("messageFrom must not be null");
        mContentValues.put(MessagesColumns.MESSAGE_FROM, value);
        return this;
    }


    public MessagesContentValues putMessageTimestamp(@NonNull Date value) {
        if (value == null) throw new IllegalArgumentException("messageTimestamp must not be null");
        mContentValues.put(MessagesColumns.MESSAGE_TIMESTAMP, value.getTime());
        return this;
    }


    public MessagesContentValues putMessageTimestamp(long value) {
        mContentValues.put(MessagesColumns.MESSAGE_TIMESTAMP, value);
        return this;
    }

    public MessagesContentValues putUnixTimestamp(@NonNull Date value) {
        if (value == null) throw new IllegalArgumentException("unixTimestamp must not be null");
        mContentValues.put(MessagesColumns.UNIX_TIMESTAMP, value.getTime());
        return this;
    }


    public MessagesContentValues putUnixTimestamp(long value) {
        mContentValues.put(MessagesColumns.UNIX_TIMESTAMP, value);
        return this;
    }

    public MessagesContentValues putMessageHash(@NonNull String value) {
        if (value == null) throw new IllegalArgumentException("messageHash must not be null");
        mContentValues.put(MessagesColumns.MESSAGE_HASH, value);
        return this;
    }

}
