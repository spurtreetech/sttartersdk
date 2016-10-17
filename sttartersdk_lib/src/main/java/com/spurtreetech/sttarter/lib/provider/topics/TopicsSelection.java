package com.spurtreetech.sttarter.lib.provider.topics;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.spurtreetech.sttarter.lib.provider.base.AbstractSelection;

/**
 * Selection for the {@code topics} table.
 */
public class TopicsSelection extends AbstractSelection<TopicsSelection> {
    @Override
    protected Uri baseUri() {
        return TopicsColumns.CONTENT_URI;
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param contentResolver The content resolver to query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code TopicsCursor} object, which is positioned before the first entry, or null.
     */
    public TopicsCursor query(ContentResolver contentResolver, String[] projection) {
        Cursor cursor = contentResolver.query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new TopicsCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, null)}.
     */
    public TopicsCursor query(ContentResolver contentResolver) {
        return query(contentResolver, null);
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param context The context to use for the query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code TopicsCursor} object, which is positioned before the first entry, or null.
     */
    public TopicsCursor query(Context context, String[] projection) {
        Cursor cursor = context.getContentResolver().query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new TopicsCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(context, null)}.
     */
    public TopicsCursor query(Context context) {
        return query(context, null);
    }


    public TopicsSelection id(long... value) {
        addEquals("topics." + TopicsColumns._ID, toObjectArray(value));
        return this;
    }

    public TopicsSelection idNot(long... value) {
        addNotEquals("topics." + TopicsColumns._ID, toObjectArray(value));
        return this;
    }

    public TopicsSelection orderById(boolean desc) {
        orderBy("topics." + TopicsColumns._ID, desc);
        return this;
    }

    public TopicsSelection orderById() {
        return orderById(false);
    }

    public TopicsSelection topicName(String... value) {
        addEquals(TopicsColumns.TOPIC_NAME, value);
        return this;
    }

    public TopicsSelection topicNameNot(String... value) {
        addNotEquals(TopicsColumns.TOPIC_NAME, value);
        return this;
    }

    public TopicsSelection topicNameLike(String... value) {
        addLike(TopicsColumns.TOPIC_NAME, value);
        return this;
    }

    public TopicsSelection topicNameContains(String... value) {
        addContains(TopicsColumns.TOPIC_NAME, value);
        return this;
    }

    public TopicsSelection topicNameStartsWith(String... value) {
        addStartsWith(TopicsColumns.TOPIC_NAME, value);
        return this;
    }

    public TopicsSelection topicNameEndsWith(String... value) {
        addEndsWith(TopicsColumns.TOPIC_NAME, value);
        return this;
    }

    public TopicsSelection orderByTopicName(boolean desc) {
        orderBy(TopicsColumns.TOPIC_NAME, desc);
        return this;
    }

    public TopicsSelection orderByTopicName() {
        orderBy(TopicsColumns.TOPIC_NAME, false);
        return this;
    }

    public TopicsSelection topicType(String... value) {
        addEquals(TopicsColumns.TOPIC_TYPE, value);
        return this;
    }

    public TopicsSelection topicTypeNot(String... value) {
        addNotEquals(TopicsColumns.TOPIC_TYPE, value);
        return this;
    }

    public TopicsSelection topicTypeLike(String... value) {
        addLike(TopicsColumns.TOPIC_TYPE, value);
        return this;
    }

    public TopicsSelection topicTypeContains(String... value) {
        addContains(TopicsColumns.TOPIC_TYPE, value);
        return this;
    }

    public TopicsSelection topicTypeStartsWith(String... value) {
        addStartsWith(TopicsColumns.TOPIC_TYPE, value);
        return this;
    }

    public TopicsSelection topicTypeEndsWith(String... value) {
        addEndsWith(TopicsColumns.TOPIC_TYPE, value);
        return this;
    }

    public TopicsSelection orderByTopicType(boolean desc) {
        orderBy(TopicsColumns.TOPIC_TYPE, desc);
        return this;
    }

    public TopicsSelection orderByTopicType() {
        orderBy(TopicsColumns.TOPIC_TYPE, false);
        return this;
    }

    public TopicsSelection topicMeta(String... value) {
        addEquals(TopicsColumns.TOPIC_META, value);
        return this;
    }

    public TopicsSelection topicMetaNot(String... value) {
        addNotEquals(TopicsColumns.TOPIC_META, value);
        return this;
    }

    public TopicsSelection topicMetaLike(String... value) {
        addLike(TopicsColumns.TOPIC_META, value);
        return this;
    }

    public TopicsSelection topicMetaContains(String... value) {
        addContains(TopicsColumns.TOPIC_META, value);
        return this;
    }

    public TopicsSelection topicMetaStartsWith(String... value) {
        addStartsWith(TopicsColumns.TOPIC_META, value);
        return this;
    }

    public TopicsSelection topicMetaEndsWith(String... value) {
        addEndsWith(TopicsColumns.TOPIC_META, value);
        return this;
    }

    public TopicsSelection orderByTopicMeta(boolean desc) {
        orderBy(TopicsColumns.TOPIC_META, desc);
        return this;
    }

    public TopicsSelection orderByTopicMeta() {
        orderBy(TopicsColumns.TOPIC_META, false);
        return this;
    }

    public TopicsSelection topicIsSubscribed(boolean value) {
        addEquals(TopicsColumns.TOPIC_IS_SUBSCRIBED, toObjectArray(value));
        return this;
    }

    public TopicsSelection orderByTopicIsSubscribed(boolean desc) {
        orderBy(TopicsColumns.TOPIC_IS_SUBSCRIBED, desc);
        return this;
    }

    public TopicsSelection orderByTopicIsSubscribed() {
        orderBy(TopicsColumns.TOPIC_IS_SUBSCRIBED, false);
        return this;
    }

    public TopicsSelection topicIsPublic(boolean value) {
        addEquals(TopicsColumns.TOPIC_IS_PUBLIC, toObjectArray(value));
        return this;
    }

    public TopicsSelection orderByTopicIsPublic(boolean desc) {
        orderBy(TopicsColumns.TOPIC_IS_PUBLIC, desc);
        return this;
    }

    public TopicsSelection orderByTopicIsPublic() {
        orderBy(TopicsColumns.TOPIC_IS_PUBLIC, false);
        return this;
    }

    public TopicsSelection topicUpdatedUnixTimestamp(long... value) {
        addEquals(TopicsColumns.TOPIC_UPDATED_UNIX_TIMESTAMP, toObjectArray(value));
        return this;
    }

    public TopicsSelection topicUpdatedUnixTimestampNot(long... value) {
        addNotEquals(TopicsColumns.TOPIC_UPDATED_UNIX_TIMESTAMP, toObjectArray(value));
        return this;
    }

    public TopicsSelection topicUpdatedUnixTimestampGt(long value) {
        addGreaterThan(TopicsColumns.TOPIC_UPDATED_UNIX_TIMESTAMP, value);
        return this;
    }

    public TopicsSelection topicUpdatedUnixTimestampGtEq(long value) {
        addGreaterThanOrEquals(TopicsColumns.TOPIC_UPDATED_UNIX_TIMESTAMP, value);
        return this;
    }

    public TopicsSelection topicUpdatedUnixTimestampLt(long value) {
        addLessThan(TopicsColumns.TOPIC_UPDATED_UNIX_TIMESTAMP, value);
        return this;
    }

    public TopicsSelection topicUpdatedUnixTimestampLtEq(long value) {
        addLessThanOrEquals(TopicsColumns.TOPIC_UPDATED_UNIX_TIMESTAMP, value);
        return this;
    }

    public TopicsSelection orderByTopicUpdatedUnixTimestamp(boolean desc) {
        orderBy(TopicsColumns.TOPIC_UPDATED_UNIX_TIMESTAMP, desc);
        return this;
    }

    public TopicsSelection orderByTopicUpdatedUnixTimestamp() {
        orderBy(TopicsColumns.TOPIC_UPDATED_UNIX_TIMESTAMP, false);
        return this;
    }

    public TopicsSelection topicGroupMembers(String... value) {
        addEquals(TopicsColumns.TOPIC_GROUP_MEMBERS, value);
        return this;
    }

    public TopicsSelection topicGroupMembersNot(String... value) {
        addNotEquals(TopicsColumns.TOPIC_GROUP_MEMBERS, value);
        return this;
    }

    public TopicsSelection topicGroupMembersLike(String... value) {
        addLike(TopicsColumns.TOPIC_GROUP_MEMBERS, value);
        return this;
    }

    public TopicsSelection topicGroupMembersContains(String... value) {
        addContains(TopicsColumns.TOPIC_GROUP_MEMBERS, value);
        return this;
    }

    public TopicsSelection topicGroupMembersStartsWith(String... value) {
        addStartsWith(TopicsColumns.TOPIC_GROUP_MEMBERS, value);
        return this;
    }

    public TopicsSelection topicGroupMembersEndsWith(String... value) {
        addEndsWith(TopicsColumns.TOPIC_GROUP_MEMBERS, value);
        return this;
    }

    public TopicsSelection orderByTopicGroupMembers(boolean desc) {
        orderBy(TopicsColumns.TOPIC_GROUP_MEMBERS, desc);
        return this;
    }

    public TopicsSelection orderByTopicGroupMembers() {
        orderBy(TopicsColumns.TOPIC_GROUP_MEMBERS, false);
        return this;
    }
}
