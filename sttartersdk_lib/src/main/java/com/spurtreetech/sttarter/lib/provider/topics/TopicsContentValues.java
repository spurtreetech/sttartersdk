package com.spurtreetech.sttarter.lib.provider.topics;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.spurtreetech.sttarter.lib.provider.base.AbstractContentValues;

/**
 * Content values wrapper for the {@code topics} table.
 */
public class TopicsContentValues extends AbstractContentValues {
    @Override
    public Uri uri() {
        return TopicsColumns.CONTENT_URI;
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(ContentResolver contentResolver, @Nullable TopicsSelection where) {
        return contentResolver.update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param //contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(Context context, @Nullable TopicsSelection where) {
        return context.getContentResolver().update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    public TopicsContentValues putTopicName(@NonNull String value) {
        if (value == null) throw new IllegalArgumentException("topicName must not be null");
        mContentValues.put(TopicsColumns.TOPIC_NAME, value);
        return this;
    }


    public TopicsContentValues putTopicType(@NonNull String value) {
        if (value == null) throw new IllegalArgumentException("topicType must not be null");
        mContentValues.put(TopicsColumns.TOPIC_TYPE, value);
        return this;
    }


    public TopicsContentValues putTopicMeta(@NonNull String value) {
        if (value == null) throw new IllegalArgumentException("topicMeta must not be null");
        mContentValues.put(TopicsColumns.TOPIC_META, value);
        return this;
    }


    public TopicsContentValues putTopicIsSubscribed(boolean value) {
        mContentValues.put(TopicsColumns.TOPIC_IS_SUBSCRIBED, value);
        return this;
    }


    public TopicsContentValues putTopicIsPublic(boolean value) {
        mContentValues.put(TopicsColumns.TOPIC_IS_PUBLIC, value);
        return this;
    }


    public TopicsContentValues putTopicUpdatedUnixTimestamp(long value) {
        mContentValues.put(TopicsColumns.TOPIC_UPDATED_UNIX_TIMESTAMP, value);
        return this;
    }


    public TopicsContentValues putTopicGroupMembers(@NonNull String value) {
        if (value == null) throw new IllegalArgumentException("topicGroupMembers must not be null");
        mContentValues.put(TopicsColumns.TOPIC_GROUP_MEMBERS, value);
        return this;
    }

}
