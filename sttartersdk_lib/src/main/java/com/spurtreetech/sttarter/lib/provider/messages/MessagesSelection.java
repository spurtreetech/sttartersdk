package com.spurtreetech.sttarter.lib.provider.messages;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.spurtreetech.sttarter.lib.provider.base.AbstractSelection;
import com.spurtreetech.sttarter.lib.provider.topics.TopicsColumns;

import java.util.Date;

/**
 * Selection for the {@code messages} table.
 */
public class MessagesSelection extends AbstractSelection<MessagesSelection> {
    @Override
    protected Uri baseUri() {
        return MessagesColumns.CONTENT_URI;
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param contentResolver The content resolver to query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code MessagesCursor} object, which is positioned before the first entry, or null.
     */
    public MessagesCursor query(ContentResolver contentResolver, String[] projection) {
        Cursor cursor = contentResolver.query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new MessagesCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, null)}.
     */
    public MessagesCursor query(ContentResolver contentResolver) {
        return query(contentResolver, null);
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param context The context to use for the query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code MessagesCursor} object, which is positioned before the first entry, or null.
     */
    public MessagesCursor query(Context context, String[] projection) {
        Cursor cursor = context.getContentResolver().query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new MessagesCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(context, null)}.
     */
    public MessagesCursor query(Context context) {
        return query(context, null);
    }


    public MessagesSelection id(long... value) {
        addEquals("messages." + MessagesColumns._ID, toObjectArray(value));
        return this;
    }

    public MessagesSelection idNot(long... value) {
        addNotEquals("messages." + MessagesColumns._ID, toObjectArray(value));
        return this;
    }

    public MessagesSelection orderById(boolean desc) {
        orderBy("messages." + MessagesColumns._ID, desc);
        return this;
    }

    public MessagesSelection orderById() {
        return orderById(false);
    }

    public MessagesSelection messageType(String... value) {
        addEquals(MessagesColumns.MESSAGE_TYPE, value);
        return this;
    }

    public MessagesSelection messageTypeNot(String... value) {
        addNotEquals(MessagesColumns.MESSAGE_TYPE, value);
        return this;
    }

    public MessagesSelection messageTypeLike(String... value) {
        addLike(MessagesColumns.MESSAGE_TYPE, value);
        return this;
    }

    public MessagesSelection messageTypeContains(String... value) {
        addContains(MessagesColumns.MESSAGE_TYPE, value);
        return this;
    }

    public MessagesSelection messageTypeStartsWith(String... value) {
        addStartsWith(MessagesColumns.MESSAGE_TYPE, value);
        return this;
    }

    public MessagesSelection messageTypeEndsWith(String... value) {
        addEndsWith(MessagesColumns.MESSAGE_TYPE, value);
        return this;
    }

    public MessagesSelection orderByMessageType(boolean desc) {
        orderBy(MessagesColumns.MESSAGE_TYPE, desc);
        return this;
    }

    public MessagesSelection orderByMessageType() {
        orderBy(MessagesColumns.MESSAGE_TYPE, false);
        return this;
    }

    public MessagesSelection messageText(String... value) {
        addEquals(MessagesColumns.MESSAGE_TEXT, value);
        return this;
    }

    public MessagesSelection messageTextNot(String... value) {
        addNotEquals(MessagesColumns.MESSAGE_TEXT, value);
        return this;
    }

    public MessagesSelection messageTextLike(String... value) {
        addLike(MessagesColumns.MESSAGE_TEXT, value);
        return this;
    }

    public MessagesSelection messageTextContains(String... value) {
        addContains(MessagesColumns.MESSAGE_TEXT, value);
        return this;
    }

    public MessagesSelection messageTextStartsWith(String... value) {
        addStartsWith(MessagesColumns.MESSAGE_TEXT, value);
        return this;
    }

    public MessagesSelection messageTextEndsWith(String... value) {
        addEndsWith(MessagesColumns.MESSAGE_TEXT, value);
        return this;
    }

    public MessagesSelection orderByMessageText(boolean desc) {
        orderBy(MessagesColumns.MESSAGE_TEXT, desc);
        return this;
    }

    public MessagesSelection orderByMessageText() {
        orderBy(MessagesColumns.MESSAGE_TEXT, false);
        return this;
    }

    public MessagesSelection messageTopic(String... value) {
        addEquals(MessagesColumns.MESSAGE_TOPIC, value);
        return this;
    }

    public MessagesSelection messageTopicNot(String... value) {
        addNotEquals(MessagesColumns.MESSAGE_TOPIC, value);
        return this;
    }

    public MessagesSelection messageTopicLike(String... value) {
        addLike(MessagesColumns.MESSAGE_TOPIC, value);
        return this;
    }

    public MessagesSelection messageTopicContains(String... value) {
        addContains(MessagesColumns.MESSAGE_TOPIC, value);
        return this;
    }

    public MessagesSelection messageTopicStartsWith(String... value) {
        addStartsWith(MessagesColumns.MESSAGE_TOPIC, value);
        return this;
    }

    public MessagesSelection messageTopicEndsWith(String... value) {
        addEndsWith(MessagesColumns.MESSAGE_TOPIC, value);
        return this;
    }

    public MessagesSelection orderByMessageTopic(boolean desc) {
        orderBy(MessagesColumns.MESSAGE_TOPIC, desc);
        return this;
    }

    public MessagesSelection orderByMessageTopic() {
        orderBy(MessagesColumns.MESSAGE_TOPIC, false);
        return this;
    }

    public MessagesSelection messageTopicId(long... value) {
        addEquals(MessagesColumns.MESSAGE_TOPIC_ID, toObjectArray(value));
        return this;
    }

    public MessagesSelection messageTopicIdNot(long... value) {
        addNotEquals(MessagesColumns.MESSAGE_TOPIC_ID, toObjectArray(value));
        return this;
    }

    public MessagesSelection messageTopicIdGt(long value) {
        addGreaterThan(MessagesColumns.MESSAGE_TOPIC_ID, value);
        return this;
    }

    public MessagesSelection messageTopicIdGtEq(long value) {
        addGreaterThanOrEquals(MessagesColumns.MESSAGE_TOPIC_ID, value);
        return this;
    }

    public MessagesSelection messageTopicIdLt(long value) {
        addLessThan(MessagesColumns.MESSAGE_TOPIC_ID, value);
        return this;
    }

    public MessagesSelection messageTopicIdLtEq(long value) {
        addLessThanOrEquals(MessagesColumns.MESSAGE_TOPIC_ID, value);
        return this;
    }

    public MessagesSelection orderByMessageTopicId(boolean desc) {
        orderBy(MessagesColumns.MESSAGE_TOPIC_ID, desc);
        return this;
    }

    public MessagesSelection orderByMessageTopicId() {
        orderBy(MessagesColumns.MESSAGE_TOPIC_ID, false);
        return this;
    }

    public MessagesSelection topicsTopicName(String... value) {
        addEquals(TopicsColumns.TOPIC_NAME, value);
        return this;
    }

    public MessagesSelection topicsTopicNameNot(String... value) {
        addNotEquals(TopicsColumns.TOPIC_NAME, value);
        return this;
    }

    public MessagesSelection topicsTopicNameLike(String... value) {
        addLike(TopicsColumns.TOPIC_NAME, value);
        return this;
    }

    public MessagesSelection topicsTopicNameContains(String... value) {
        addContains(TopicsColumns.TOPIC_NAME, value);
        return this;
    }

    public MessagesSelection topicsTopicNameStartsWith(String... value) {
        addStartsWith(TopicsColumns.TOPIC_NAME, value);
        return this;
    }

    public MessagesSelection topicsTopicNameEndsWith(String... value) {
        addEndsWith(TopicsColumns.TOPIC_NAME, value);
        return this;
    }

    public MessagesSelection orderByTopicsTopicName(boolean desc) {
        orderBy(TopicsColumns.TOPIC_NAME, desc);
        return this;
    }

    public MessagesSelection orderByTopicsTopicName() {
        orderBy(TopicsColumns.TOPIC_NAME, false);
        return this;
    }

    public MessagesSelection topicsTopicType(String... value) {
        addEquals(TopicsColumns.TOPIC_TYPE, value);
        return this;
    }

    public MessagesSelection topicsTopicTypeNot(String... value) {
        addNotEquals(TopicsColumns.TOPIC_TYPE, value);
        return this;
    }

    public MessagesSelection topicsTopicTypeLike(String... value) {
        addLike(TopicsColumns.TOPIC_TYPE, value);
        return this;
    }

    public MessagesSelection topicsTopicTypeContains(String... value) {
        addContains(TopicsColumns.TOPIC_TYPE, value);
        return this;
    }

    public MessagesSelection topicsTopicTypeStartsWith(String... value) {
        addStartsWith(TopicsColumns.TOPIC_TYPE, value);
        return this;
    }

    public MessagesSelection topicsTopicTypeEndsWith(String... value) {
        addEndsWith(TopicsColumns.TOPIC_TYPE, value);
        return this;
    }

    public MessagesSelection orderByTopicsTopicType(boolean desc) {
        orderBy(TopicsColumns.TOPIC_TYPE, desc);
        return this;
    }

    public MessagesSelection orderByTopicsTopicType() {
        orderBy(TopicsColumns.TOPIC_TYPE, false);
        return this;
    }

    public MessagesSelection topicsTopicMeta(String... value) {
        addEquals(TopicsColumns.TOPIC_META, value);
        return this;
    }

    public MessagesSelection topicsTopicMetaNot(String... value) {
        addNotEquals(TopicsColumns.TOPIC_META, value);
        return this;
    }

    public MessagesSelection topicsTopicMetaLike(String... value) {
        addLike(TopicsColumns.TOPIC_META, value);
        return this;
    }

    public MessagesSelection topicsTopicMetaContains(String... value) {
        addContains(TopicsColumns.TOPIC_META, value);
        return this;
    }

    public MessagesSelection topicsTopicMetaStartsWith(String... value) {
        addStartsWith(TopicsColumns.TOPIC_META, value);
        return this;
    }

    public MessagesSelection topicsTopicMetaEndsWith(String... value) {
        addEndsWith(TopicsColumns.TOPIC_META, value);
        return this;
    }

    public MessagesSelection orderByTopicsTopicMeta(boolean desc) {
        orderBy(TopicsColumns.TOPIC_META, desc);
        return this;
    }

    public MessagesSelection orderByTopicsTopicMeta() {
        orderBy(TopicsColumns.TOPIC_META, false);
        return this;
    }

    public MessagesSelection topicsTopicIsSubscribed(boolean value) {
        addEquals(TopicsColumns.TOPIC_IS_SUBSCRIBED, toObjectArray(value));
        return this;
    }

    public MessagesSelection orderByTopicsTopicIsSubscribed(boolean desc) {
        orderBy(TopicsColumns.TOPIC_IS_SUBSCRIBED, desc);
        return this;
    }

    public MessagesSelection orderByTopicsTopicIsSubscribed() {
        orderBy(TopicsColumns.TOPIC_IS_SUBSCRIBED, false);
        return this;
    }

    public MessagesSelection topicsTopicIsPublic(boolean value) {
        addEquals(TopicsColumns.TOPIC_IS_PUBLIC, toObjectArray(value));
        return this;
    }

    public MessagesSelection orderByTopicsTopicIsPublic(boolean desc) {
        orderBy(TopicsColumns.TOPIC_IS_PUBLIC, desc);
        return this;
    }

    public MessagesSelection orderByTopicsTopicIsPublic() {
        orderBy(TopicsColumns.TOPIC_IS_PUBLIC, false);
        return this;
    }

    public MessagesSelection topicsTopicUpdatedUnixTimestamp(long... value) {
        addEquals(TopicsColumns.TOPIC_UPDATED_UNIX_TIMESTAMP, toObjectArray(value));
        return this;
    }

    public MessagesSelection topicsTopicUpdatedUnixTimestampNot(long... value) {
        addNotEquals(TopicsColumns.TOPIC_UPDATED_UNIX_TIMESTAMP, toObjectArray(value));
        return this;
    }

    public MessagesSelection topicsTopicUpdatedUnixTimestampGt(long value) {
        addGreaterThan(TopicsColumns.TOPIC_UPDATED_UNIX_TIMESTAMP, value);
        return this;
    }

    public MessagesSelection topicsTopicUpdatedUnixTimestampGtEq(long value) {
        addGreaterThanOrEquals(TopicsColumns.TOPIC_UPDATED_UNIX_TIMESTAMP, value);
        return this;
    }

    public MessagesSelection topicsTopicUpdatedUnixTimestampLt(long value) {
        addLessThan(TopicsColumns.TOPIC_UPDATED_UNIX_TIMESTAMP, value);
        return this;
    }

    public MessagesSelection topicsTopicUpdatedUnixTimestampLtEq(long value) {
        addLessThanOrEquals(TopicsColumns.TOPIC_UPDATED_UNIX_TIMESTAMP, value);
        return this;
    }

    public MessagesSelection orderByTopicsTopicUpdatedUnixTimestamp(boolean desc) {
        orderBy(TopicsColumns.TOPIC_UPDATED_UNIX_TIMESTAMP, desc);
        return this;
    }

    public MessagesSelection orderByTopicsTopicUpdatedUnixTimestamp() {
        orderBy(TopicsColumns.TOPIC_UPDATED_UNIX_TIMESTAMP, false);
        return this;
    }

    public MessagesSelection topicsTopicGroupMembers(String... value) {
        addEquals(TopicsColumns.TOPIC_GROUP_MEMBERS, value);
        return this;
    }

    public MessagesSelection topicsTopicGroupMembersNot(String... value) {
        addNotEquals(TopicsColumns.TOPIC_GROUP_MEMBERS, value);
        return this;
    }

    public MessagesSelection topicsTopicGroupMembersLike(String... value) {
        addLike(TopicsColumns.TOPIC_GROUP_MEMBERS, value);
        return this;
    }

    public MessagesSelection topicsTopicGroupMembersContains(String... value) {
        addContains(TopicsColumns.TOPIC_GROUP_MEMBERS, value);
        return this;
    }

    public MessagesSelection topicsTopicGroupMembersStartsWith(String... value) {
        addStartsWith(TopicsColumns.TOPIC_GROUP_MEMBERS, value);
        return this;
    }

    public MessagesSelection topicsTopicGroupMembersEndsWith(String... value) {
        addEndsWith(TopicsColumns.TOPIC_GROUP_MEMBERS, value);
        return this;
    }

    public MessagesSelection orderByTopicsTopicGroupMembers(boolean desc) {
        orderBy(TopicsColumns.TOPIC_GROUP_MEMBERS, desc);
        return this;
    }

    public MessagesSelection orderByTopicsTopicGroupMembers() {
        orderBy(TopicsColumns.TOPIC_GROUP_MEMBERS, false);
        return this;
    }

    public MessagesSelection fileType(String... value) {
        addEquals(MessagesColumns.FILE_TYPE, value);
        return this;
    }

    public MessagesSelection fileTypeNot(String... value) {
        addNotEquals(MessagesColumns.FILE_TYPE, value);
        return this;
    }

    public MessagesSelection fileTypeLike(String... value) {
        addLike(MessagesColumns.FILE_TYPE, value);
        return this;
    }

    public MessagesSelection fileTypeContains(String... value) {
        addContains(MessagesColumns.FILE_TYPE, value);
        return this;
    }

    public MessagesSelection fileTypeStartsWith(String... value) {
        addStartsWith(MessagesColumns.FILE_TYPE, value);
        return this;
    }

    public MessagesSelection fileTypeEndsWith(String... value) {
        addEndsWith(MessagesColumns.FILE_TYPE, value);
        return this;
    }

    public MessagesSelection orderByFileType(boolean desc) {
        orderBy(MessagesColumns.FILE_TYPE, desc);
        return this;
    }

    public MessagesSelection orderByFileType() {
        orderBy(MessagesColumns.FILE_TYPE, false);
        return this;
    }

    public MessagesSelection fileUrl(String... value) {
        addEquals(MessagesColumns.FILE_URL, value);
        return this;
    }

    public MessagesSelection fileUrlNot(String... value) {
        addNotEquals(MessagesColumns.FILE_URL, value);
        return this;
    }

    public MessagesSelection fileUrlLike(String... value) {
        addLike(MessagesColumns.FILE_URL, value);
        return this;
    }

    public MessagesSelection fileUrlContains(String... value) {
        addContains(MessagesColumns.FILE_URL, value);
        return this;
    }

    public MessagesSelection fileUrlStartsWith(String... value) {
        addStartsWith(MessagesColumns.FILE_URL, value);
        return this;
    }

    public MessagesSelection fileUrlEndsWith(String... value) {
        addEndsWith(MessagesColumns.FILE_URL, value);
        return this;
    }

    public MessagesSelection orderByFileUrl(boolean desc) {
        orderBy(MessagesColumns.FILE_URL, desc);
        return this;
    }

    public MessagesSelection orderByFileUrl() {
        orderBy(MessagesColumns.FILE_URL, false);
        return this;
    }

    public MessagesSelection isSender(boolean value) {
        addEquals(MessagesColumns.IS_SENDER, toObjectArray(value));
        return this;
    }

    public MessagesSelection orderByIsSender(boolean desc) {
        orderBy(MessagesColumns.IS_SENDER, desc);
        return this;
    }

    public MessagesSelection orderByIsSender() {
        orderBy(MessagesColumns.IS_SENDER, false);
        return this;
    }

    public MessagesSelection isDelivered(boolean value) {
        addEquals(MessagesColumns.IS_DELIVERED, toObjectArray(value));
        return this;
    }

    public MessagesSelection orderByIsDelivered(boolean desc) {
        orderBy(MessagesColumns.IS_DELIVERED, desc);
        return this;
    }

    public MessagesSelection orderByIsDelivered() {
        orderBy(MessagesColumns.IS_DELIVERED, false);
        return this;
    }

    public MessagesSelection isRead(boolean value) {
        addEquals(MessagesColumns.IS_READ, toObjectArray(value));
        return this;
    }

    public MessagesSelection orderByIsRead(boolean desc) {
        orderBy(MessagesColumns.IS_READ, desc);
        return this;
    }

    public MessagesSelection orderByIsRead() {
        orderBy(MessagesColumns.IS_READ, false);
        return this;
    }

    public MessagesSelection messageFrom(String... value) {
        addEquals(MessagesColumns.MESSAGE_FROM, value);
        return this;
    }

    public MessagesSelection messageFromNot(String... value) {
        addNotEquals(MessagesColumns.MESSAGE_FROM, value);
        return this;
    }

    public MessagesSelection messageFromLike(String... value) {
        addLike(MessagesColumns.MESSAGE_FROM, value);
        return this;
    }

    public MessagesSelection messageFromContains(String... value) {
        addContains(MessagesColumns.MESSAGE_FROM, value);
        return this;
    }

    public MessagesSelection messageFromStartsWith(String... value) {
        addStartsWith(MessagesColumns.MESSAGE_FROM, value);
        return this;
    }

    public MessagesSelection messageFromEndsWith(String... value) {
        addEndsWith(MessagesColumns.MESSAGE_FROM, value);
        return this;
    }

    public MessagesSelection orderByMessageFrom(boolean desc) {
        orderBy(MessagesColumns.MESSAGE_FROM, desc);
        return this;
    }

    public MessagesSelection orderByMessageFrom() {
        orderBy(MessagesColumns.MESSAGE_FROM, false);
        return this;
    }

    public MessagesSelection messageTimestamp(Date... value) {
        addEquals(MessagesColumns.MESSAGE_TIMESTAMP, value);
        return this;
    }

    public MessagesSelection messageTimestampNot(Date... value) {
        addNotEquals(MessagesColumns.MESSAGE_TIMESTAMP, value);
        return this;
    }

    public MessagesSelection messageTimestamp(long... value) {
        addEquals(MessagesColumns.MESSAGE_TIMESTAMP, toObjectArray(value));
        return this;
    }

    public MessagesSelection messageTimestampAfter(Date value) {
        addGreaterThan(MessagesColumns.MESSAGE_TIMESTAMP, value);
        return this;
    }

    public MessagesSelection messageTimestampAfterEq(Date value) {
        addGreaterThanOrEquals(MessagesColumns.MESSAGE_TIMESTAMP, value);
        return this;
    }

    public MessagesSelection messageTimestampBefore(Date value) {
        addLessThan(MessagesColumns.MESSAGE_TIMESTAMP, value);
        return this;
    }

    public MessagesSelection messageTimestampBeforeEq(Date value) {
        addLessThanOrEquals(MessagesColumns.MESSAGE_TIMESTAMP, value);
        return this;
    }

    public MessagesSelection orderByMessageTimestamp(boolean desc) {
        orderBy(MessagesColumns.MESSAGE_TIMESTAMP, desc);
        return this;
    }

    public MessagesSelection orderByMessageTimestamp() {
        orderBy(MessagesColumns.MESSAGE_TIMESTAMP, false);
        return this;
    }

    public MessagesSelection unixTimestamp(Date... value) {
        addEquals(MessagesColumns.UNIX_TIMESTAMP, value);
        return this;
    }

    public MessagesSelection unixTimestampNot(Date... value) {
        addNotEquals(MessagesColumns.UNIX_TIMESTAMP, value);
        return this;
    }

    public MessagesSelection unixTimestamp(long... value) {
        addEquals(MessagesColumns.UNIX_TIMESTAMP, toObjectArray(value));
        return this;
    }

    public MessagesSelection unixTimestampAfter(Date value) {
        addGreaterThan(MessagesColumns.UNIX_TIMESTAMP, value);
        return this;
    }

    public MessagesSelection unixTimestampAfterEq(Date value) {
        addGreaterThanOrEquals(MessagesColumns.UNIX_TIMESTAMP, value);
        return this;
    }

    public MessagesSelection unixTimestampBefore(Date value) {
        addLessThan(MessagesColumns.UNIX_TIMESTAMP, value);
        return this;
    }

    public MessagesSelection unixTimestampBeforeEq(Date value) {
        addLessThanOrEquals(MessagesColumns.UNIX_TIMESTAMP, value);
        return this;
    }

    public MessagesSelection orderByUnixTimestamp(boolean desc) {
        orderBy(MessagesColumns.UNIX_TIMESTAMP, desc);
        return this;
    }

    public MessagesSelection orderByUnixTimestamp() {
        orderBy(MessagesColumns.UNIX_TIMESTAMP, false);
        return this;
    }

    public MessagesSelection messageHash(String... value) {
        addEquals(MessagesColumns.MESSAGE_HASH, value);
        return this;
    }

    public MessagesSelection messageHashNot(String... value) {
        addNotEquals(MessagesColumns.MESSAGE_HASH, value);
        return this;
    }

    public MessagesSelection messageHashLike(String... value) {
        addLike(MessagesColumns.MESSAGE_HASH, value);
        return this;
    }

    public MessagesSelection messageHashContains(String... value) {
        addContains(MessagesColumns.MESSAGE_HASH, value);
        return this;
    }

    public MessagesSelection messageHashStartsWith(String... value) {
        addStartsWith(MessagesColumns.MESSAGE_HASH, value);
        return this;
    }

    public MessagesSelection messageHashEndsWith(String... value) {
        addEndsWith(MessagesColumns.MESSAGE_HASH, value);
        return this;
    }

    public MessagesSelection orderByMessageHash(boolean desc) {
        orderBy(MessagesColumns.MESSAGE_HASH, desc);
        return this;
    }

    public MessagesSelection orderByMessageHash() {
        orderBy(MessagesColumns.MESSAGE_HASH, false);
        return this;
    }
}
