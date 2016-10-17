package com.spurtreetech.sttarter.lib.provider.topicusers;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.spurtreetech.sttarter.lib.provider.base.AbstractContentValues;

/**
 * Content values wrapper for the {@code topic_users} table.
 */
public class TopicUsersContentValues extends AbstractContentValues {
    @Override
    public Uri uri() {
        return TopicUsersColumns.CONTENT_URI;
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(ContentResolver contentResolver, @Nullable TopicUsersSelection where) {
        return contentResolver.update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param //contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(Context context, @Nullable TopicUsersSelection where) {
        return context.getContentResolver().update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    public TopicUsersContentValues putMessageTopicId(long value) {
        mContentValues.put(TopicUsersColumns.MESSAGE_TOPIC_ID, value);
        return this;
    }


    public TopicUsersContentValues putUserId(long value) {
        mContentValues.put(TopicUsersColumns.USER_ID, value);
        return this;
    }

}
