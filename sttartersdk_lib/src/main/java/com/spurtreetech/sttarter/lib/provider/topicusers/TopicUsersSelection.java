package com.spurtreetech.sttarter.lib.provider.topicusers;

import java.util.Date;

import android.content.Context;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.spurtreetech.sttarter.lib.provider.base.AbstractSelection;
import com.spurtreetech.sttarter.lib.provider.topics.*;
import com.spurtreetech.sttarter.lib.provider.users.*;

/**
 * Selection for the {@code topic_users} table.
 */
public class TopicUsersSelection extends AbstractSelection<TopicUsersSelection> {
    @Override
    protected Uri baseUri() {
        return TopicUsersColumns.CONTENT_URI;
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param contentResolver The content resolver to query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code TopicUsersCursor} object, which is positioned before the first entry, or null.
     */
    public TopicUsersCursor query(ContentResolver contentResolver, String[] projection) {
        Cursor cursor = contentResolver.query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new TopicUsersCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, null)}.
     */
    public TopicUsersCursor query(ContentResolver contentResolver) {
        return query(contentResolver, null);
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param context The context to use for the query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code TopicUsersCursor} object, which is positioned before the first entry, or null.
     */
    public TopicUsersCursor query(Context context, String[] projection) {
        Cursor cursor = context.getContentResolver().query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new TopicUsersCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(context, null)}.
     */
    public TopicUsersCursor query(Context context) {
        return query(context, null);
    }


    public TopicUsersSelection id(long... value) {
        addEquals("topic_users." + TopicUsersColumns._ID, toObjectArray(value));
        return this;
    }

    public TopicUsersSelection idNot(long... value) {
        addNotEquals("topic_users." + TopicUsersColumns._ID, toObjectArray(value));
        return this;
    }

    public TopicUsersSelection orderById(boolean desc) {
        orderBy("topic_users." + TopicUsersColumns._ID, desc);
        return this;
    }

    public TopicUsersSelection orderById() {
        return orderById(false);
    }

    public TopicUsersSelection messageTopicId(long... value) {
        addEquals(TopicUsersColumns.MESSAGE_TOPIC_ID, toObjectArray(value));
        return this;
    }

    public TopicUsersSelection messageTopicIdNot(long... value) {
        addNotEquals(TopicUsersColumns.MESSAGE_TOPIC_ID, toObjectArray(value));
        return this;
    }

    public TopicUsersSelection messageTopicIdGt(long value) {
        addGreaterThan(TopicUsersColumns.MESSAGE_TOPIC_ID, value);
        return this;
    }

    public TopicUsersSelection messageTopicIdGtEq(long value) {
        addGreaterThanOrEquals(TopicUsersColumns.MESSAGE_TOPIC_ID, value);
        return this;
    }

    public TopicUsersSelection messageTopicIdLt(long value) {
        addLessThan(TopicUsersColumns.MESSAGE_TOPIC_ID, value);
        return this;
    }

    public TopicUsersSelection messageTopicIdLtEq(long value) {
        addLessThanOrEquals(TopicUsersColumns.MESSAGE_TOPIC_ID, value);
        return this;
    }

    public TopicUsersSelection orderByMessageTopicId(boolean desc) {
        orderBy(TopicUsersColumns.MESSAGE_TOPIC_ID, desc);
        return this;
    }

    public TopicUsersSelection orderByMessageTopicId() {
        orderBy(TopicUsersColumns.MESSAGE_TOPIC_ID, false);
        return this;
    }

    public TopicUsersSelection topicsTopicName(String... value) {
        addEquals(TopicsColumns.TOPIC_NAME, value);
        return this;
    }

    public TopicUsersSelection topicsTopicNameNot(String... value) {
        addNotEquals(TopicsColumns.TOPIC_NAME, value);
        return this;
    }

    public TopicUsersSelection topicsTopicNameLike(String... value) {
        addLike(TopicsColumns.TOPIC_NAME, value);
        return this;
    }

    public TopicUsersSelection topicsTopicNameContains(String... value) {
        addContains(TopicsColumns.TOPIC_NAME, value);
        return this;
    }

    public TopicUsersSelection topicsTopicNameStartsWith(String... value) {
        addStartsWith(TopicsColumns.TOPIC_NAME, value);
        return this;
    }

    public TopicUsersSelection topicsTopicNameEndsWith(String... value) {
        addEndsWith(TopicsColumns.TOPIC_NAME, value);
        return this;
    }

    public TopicUsersSelection orderByTopicsTopicName(boolean desc) {
        orderBy(TopicsColumns.TOPIC_NAME, desc);
        return this;
    }

    public TopicUsersSelection orderByTopicsTopicName() {
        orderBy(TopicsColumns.TOPIC_NAME, false);
        return this;
    }

    public TopicUsersSelection topicsTopicType(String... value) {
        addEquals(TopicsColumns.TOPIC_TYPE, value);
        return this;
    }

    public TopicUsersSelection topicsTopicTypeNot(String... value) {
        addNotEquals(TopicsColumns.TOPIC_TYPE, value);
        return this;
    }

    public TopicUsersSelection topicsTopicTypeLike(String... value) {
        addLike(TopicsColumns.TOPIC_TYPE, value);
        return this;
    }

    public TopicUsersSelection topicsTopicTypeContains(String... value) {
        addContains(TopicsColumns.TOPIC_TYPE, value);
        return this;
    }

    public TopicUsersSelection topicsTopicTypeStartsWith(String... value) {
        addStartsWith(TopicsColumns.TOPIC_TYPE, value);
        return this;
    }

    public TopicUsersSelection topicsTopicTypeEndsWith(String... value) {
        addEndsWith(TopicsColumns.TOPIC_TYPE, value);
        return this;
    }

    public TopicUsersSelection orderByTopicsTopicType(boolean desc) {
        orderBy(TopicsColumns.TOPIC_TYPE, desc);
        return this;
    }

    public TopicUsersSelection orderByTopicsTopicType() {
        orderBy(TopicsColumns.TOPIC_TYPE, false);
        return this;
    }

    public TopicUsersSelection topicsTopicMeta(String... value) {
        addEquals(TopicsColumns.TOPIC_META, value);
        return this;
    }

    public TopicUsersSelection topicsTopicMetaNot(String... value) {
        addNotEquals(TopicsColumns.TOPIC_META, value);
        return this;
    }

    public TopicUsersSelection topicsTopicMetaLike(String... value) {
        addLike(TopicsColumns.TOPIC_META, value);
        return this;
    }

    public TopicUsersSelection topicsTopicMetaContains(String... value) {
        addContains(TopicsColumns.TOPIC_META, value);
        return this;
    }

    public TopicUsersSelection topicsTopicMetaStartsWith(String... value) {
        addStartsWith(TopicsColumns.TOPIC_META, value);
        return this;
    }

    public TopicUsersSelection topicsTopicMetaEndsWith(String... value) {
        addEndsWith(TopicsColumns.TOPIC_META, value);
        return this;
    }

    public TopicUsersSelection orderByTopicsTopicMeta(boolean desc) {
        orderBy(TopicsColumns.TOPIC_META, desc);
        return this;
    }

    public TopicUsersSelection orderByTopicsTopicMeta() {
        orderBy(TopicsColumns.TOPIC_META, false);
        return this;
    }

    public TopicUsersSelection topicsTopicIsSubscribed(boolean value) {
        addEquals(TopicsColumns.TOPIC_IS_SUBSCRIBED, toObjectArray(value));
        return this;
    }

    public TopicUsersSelection orderByTopicsTopicIsSubscribed(boolean desc) {
        orderBy(TopicsColumns.TOPIC_IS_SUBSCRIBED, desc);
        return this;
    }

    public TopicUsersSelection orderByTopicsTopicIsSubscribed() {
        orderBy(TopicsColumns.TOPIC_IS_SUBSCRIBED, false);
        return this;
    }

    public TopicUsersSelection topicsTopicIsPublic(boolean value) {
        addEquals(TopicsColumns.TOPIC_IS_PUBLIC, toObjectArray(value));
        return this;
    }

    public TopicUsersSelection orderByTopicsTopicIsPublic(boolean desc) {
        orderBy(TopicsColumns.TOPIC_IS_PUBLIC, desc);
        return this;
    }

    public TopicUsersSelection orderByTopicsTopicIsPublic() {
        orderBy(TopicsColumns.TOPIC_IS_PUBLIC, false);
        return this;
    }

    public TopicUsersSelection topicsTopicUpdatedUnixTimestamp(long... value) {
        addEquals(TopicsColumns.TOPIC_UPDATED_UNIX_TIMESTAMP, toObjectArray(value));
        return this;
    }

    public TopicUsersSelection topicsTopicUpdatedUnixTimestampNot(long... value) {
        addNotEquals(TopicsColumns.TOPIC_UPDATED_UNIX_TIMESTAMP, toObjectArray(value));
        return this;
    }

    public TopicUsersSelection topicsTopicUpdatedUnixTimestampGt(long value) {
        addGreaterThan(TopicsColumns.TOPIC_UPDATED_UNIX_TIMESTAMP, value);
        return this;
    }

    public TopicUsersSelection topicsTopicUpdatedUnixTimestampGtEq(long value) {
        addGreaterThanOrEquals(TopicsColumns.TOPIC_UPDATED_UNIX_TIMESTAMP, value);
        return this;
    }

    public TopicUsersSelection topicsTopicUpdatedUnixTimestampLt(long value) {
        addLessThan(TopicsColumns.TOPIC_UPDATED_UNIX_TIMESTAMP, value);
        return this;
    }

    public TopicUsersSelection topicsTopicUpdatedUnixTimestampLtEq(long value) {
        addLessThanOrEquals(TopicsColumns.TOPIC_UPDATED_UNIX_TIMESTAMP, value);
        return this;
    }

    public TopicUsersSelection orderByTopicsTopicUpdatedUnixTimestamp(boolean desc) {
        orderBy(TopicsColumns.TOPIC_UPDATED_UNIX_TIMESTAMP, desc);
        return this;
    }

    public TopicUsersSelection orderByTopicsTopicUpdatedUnixTimestamp() {
        orderBy(TopicsColumns.TOPIC_UPDATED_UNIX_TIMESTAMP, false);
        return this;
    }

    public TopicUsersSelection topicsTopicGroupMembers(String... value) {
        addEquals(TopicsColumns.TOPIC_GROUP_MEMBERS, value);
        return this;
    }

    public TopicUsersSelection topicsTopicGroupMembersNot(String... value) {
        addNotEquals(TopicsColumns.TOPIC_GROUP_MEMBERS, value);
        return this;
    }

    public TopicUsersSelection topicsTopicGroupMembersLike(String... value) {
        addLike(TopicsColumns.TOPIC_GROUP_MEMBERS, value);
        return this;
    }

    public TopicUsersSelection topicsTopicGroupMembersContains(String... value) {
        addContains(TopicsColumns.TOPIC_GROUP_MEMBERS, value);
        return this;
    }

    public TopicUsersSelection topicsTopicGroupMembersStartsWith(String... value) {
        addStartsWith(TopicsColumns.TOPIC_GROUP_MEMBERS, value);
        return this;
    }

    public TopicUsersSelection topicsTopicGroupMembersEndsWith(String... value) {
        addEndsWith(TopicsColumns.TOPIC_GROUP_MEMBERS, value);
        return this;
    }

    public TopicUsersSelection orderByTopicsTopicGroupMembers(boolean desc) {
        orderBy(TopicsColumns.TOPIC_GROUP_MEMBERS, desc);
        return this;
    }

    public TopicUsersSelection orderByTopicsTopicGroupMembers() {
        orderBy(TopicsColumns.TOPIC_GROUP_MEMBERS, false);
        return this;
    }

    public TopicUsersSelection userId(long... value) {
        addEquals(TopicUsersColumns.USER_ID, toObjectArray(value));
        return this;
    }

    public TopicUsersSelection userIdNot(long... value) {
        addNotEquals(TopicUsersColumns.USER_ID, toObjectArray(value));
        return this;
    }

    public TopicUsersSelection userIdGt(long value) {
        addGreaterThan(TopicUsersColumns.USER_ID, value);
        return this;
    }

    public TopicUsersSelection userIdGtEq(long value) {
        addGreaterThanOrEquals(TopicUsersColumns.USER_ID, value);
        return this;
    }

    public TopicUsersSelection userIdLt(long value) {
        addLessThan(TopicUsersColumns.USER_ID, value);
        return this;
    }

    public TopicUsersSelection userIdLtEq(long value) {
        addLessThanOrEquals(TopicUsersColumns.USER_ID, value);
        return this;
    }

    public TopicUsersSelection orderByUserId(boolean desc) {
        orderBy(TopicUsersColumns.USER_ID, desc);
        return this;
    }

    public TopicUsersSelection orderByUserId() {
        orderBy(TopicUsersColumns.USER_ID, false);
        return this;
    }

    public TopicUsersSelection usersUsersUserId(long... value) {
        addEquals(UsersColumns.USERS_USER_ID, toObjectArray(value));
        return this;
    }

    public TopicUsersSelection usersUsersUserIdNot(long... value) {
        addNotEquals(UsersColumns.USERS_USER_ID, toObjectArray(value));
        return this;
    }

    public TopicUsersSelection usersUsersUserIdGt(long value) {
        addGreaterThan(UsersColumns.USERS_USER_ID, value);
        return this;
    }

    public TopicUsersSelection usersUsersUserIdGtEq(long value) {
        addGreaterThanOrEquals(UsersColumns.USERS_USER_ID, value);
        return this;
    }

    public TopicUsersSelection usersUsersUserIdLt(long value) {
        addLessThan(UsersColumns.USERS_USER_ID, value);
        return this;
    }

    public TopicUsersSelection usersUsersUserIdLtEq(long value) {
        addLessThanOrEquals(UsersColumns.USERS_USER_ID, value);
        return this;
    }

    public TopicUsersSelection orderByUsersUsersUserId(boolean desc) {
        orderBy(UsersColumns.USERS_USER_ID, desc);
        return this;
    }

    public TopicUsersSelection orderByUsersUsersUserId() {
        orderBy(UsersColumns.USERS_USER_ID, false);
        return this;
    }

    public TopicUsersSelection usersUsersName(String... value) {
        addEquals(UsersColumns.USERS_NAME, value);
        return this;
    }

    public TopicUsersSelection usersUsersNameNot(String... value) {
        addNotEquals(UsersColumns.USERS_NAME, value);
        return this;
    }

    public TopicUsersSelection usersUsersNameLike(String... value) {
        addLike(UsersColumns.USERS_NAME, value);
        return this;
    }

    public TopicUsersSelection usersUsersNameContains(String... value) {
        addContains(UsersColumns.USERS_NAME, value);
        return this;
    }

    public TopicUsersSelection usersUsersNameStartsWith(String... value) {
        addStartsWith(UsersColumns.USERS_NAME, value);
        return this;
    }

    public TopicUsersSelection usersUsersNameEndsWith(String... value) {
        addEndsWith(UsersColumns.USERS_NAME, value);
        return this;
    }

    public TopicUsersSelection orderByUsersUsersName(boolean desc) {
        orderBy(UsersColumns.USERS_NAME, desc);
        return this;
    }

    public TopicUsersSelection orderByUsersUsersName() {
        orderBy(UsersColumns.USERS_NAME, false);
        return this;
    }

    public TopicUsersSelection usersUsersUsername(String... value) {
        addEquals(UsersColumns.USERS_USERNAME, value);
        return this;
    }

    public TopicUsersSelection usersUsersUsernameNot(String... value) {
        addNotEquals(UsersColumns.USERS_USERNAME, value);
        return this;
    }

    public TopicUsersSelection usersUsersUsernameLike(String... value) {
        addLike(UsersColumns.USERS_USERNAME, value);
        return this;
    }

    public TopicUsersSelection usersUsersUsernameContains(String... value) {
        addContains(UsersColumns.USERS_USERNAME, value);
        return this;
    }

    public TopicUsersSelection usersUsersUsernameStartsWith(String... value) {
        addStartsWith(UsersColumns.USERS_USERNAME, value);
        return this;
    }

    public TopicUsersSelection usersUsersUsernameEndsWith(String... value) {
        addEndsWith(UsersColumns.USERS_USERNAME, value);
        return this;
    }

    public TopicUsersSelection orderByUsersUsersUsername(boolean desc) {
        orderBy(UsersColumns.USERS_USERNAME, desc);
        return this;
    }

    public TopicUsersSelection orderByUsersUsersUsername() {
        orderBy(UsersColumns.USERS_USERNAME, false);
        return this;
    }

    public TopicUsersSelection usersUsersEmail(String... value) {
        addEquals(UsersColumns.USERS_EMAIL, value);
        return this;
    }

    public TopicUsersSelection usersUsersEmailNot(String... value) {
        addNotEquals(UsersColumns.USERS_EMAIL, value);
        return this;
    }

    public TopicUsersSelection usersUsersEmailLike(String... value) {
        addLike(UsersColumns.USERS_EMAIL, value);
        return this;
    }

    public TopicUsersSelection usersUsersEmailContains(String... value) {
        addContains(UsersColumns.USERS_EMAIL, value);
        return this;
    }

    public TopicUsersSelection usersUsersEmailStartsWith(String... value) {
        addStartsWith(UsersColumns.USERS_EMAIL, value);
        return this;
    }

    public TopicUsersSelection usersUsersEmailEndsWith(String... value) {
        addEndsWith(UsersColumns.USERS_EMAIL, value);
        return this;
    }

    public TopicUsersSelection orderByUsersUsersEmail(boolean desc) {
        orderBy(UsersColumns.USERS_EMAIL, desc);
        return this;
    }

    public TopicUsersSelection orderByUsersUsersEmail() {
        orderBy(UsersColumns.USERS_EMAIL, false);
        return this;
    }

    public TopicUsersSelection usersUsersMobile(String... value) {
        addEquals(UsersColumns.USERS_MOBILE, value);
        return this;
    }

    public TopicUsersSelection usersUsersMobileNot(String... value) {
        addNotEquals(UsersColumns.USERS_MOBILE, value);
        return this;
    }

    public TopicUsersSelection usersUsersMobileLike(String... value) {
        addLike(UsersColumns.USERS_MOBILE, value);
        return this;
    }

    public TopicUsersSelection usersUsersMobileContains(String... value) {
        addContains(UsersColumns.USERS_MOBILE, value);
        return this;
    }

    public TopicUsersSelection usersUsersMobileStartsWith(String... value) {
        addStartsWith(UsersColumns.USERS_MOBILE, value);
        return this;
    }

    public TopicUsersSelection usersUsersMobileEndsWith(String... value) {
        addEndsWith(UsersColumns.USERS_MOBILE, value);
        return this;
    }

    public TopicUsersSelection orderByUsersUsersMobile(boolean desc) {
        orderBy(UsersColumns.USERS_MOBILE, desc);
        return this;
    }

    public TopicUsersSelection orderByUsersUsersMobile() {
        orderBy(UsersColumns.USERS_MOBILE, false);
        return this;
    }

    public TopicUsersSelection usersUsersAvatar(String... value) {
        addEquals(UsersColumns.USERS_AVATAR, value);
        return this;
    }

    public TopicUsersSelection usersUsersAvatarNot(String... value) {
        addNotEquals(UsersColumns.USERS_AVATAR, value);
        return this;
    }

    public TopicUsersSelection usersUsersAvatarLike(String... value) {
        addLike(UsersColumns.USERS_AVATAR, value);
        return this;
    }

    public TopicUsersSelection usersUsersAvatarContains(String... value) {
        addContains(UsersColumns.USERS_AVATAR, value);
        return this;
    }

    public TopicUsersSelection usersUsersAvatarStartsWith(String... value) {
        addStartsWith(UsersColumns.USERS_AVATAR, value);
        return this;
    }

    public TopicUsersSelection usersUsersAvatarEndsWith(String... value) {
        addEndsWith(UsersColumns.USERS_AVATAR, value);
        return this;
    }

    public TopicUsersSelection orderByUsersUsersAvatar(boolean desc) {
        orderBy(UsersColumns.USERS_AVATAR, desc);
        return this;
    }

    public TopicUsersSelection orderByUsersUsersAvatar() {
        orderBy(UsersColumns.USERS_AVATAR, false);
        return this;
    }

    public TopicUsersSelection usersUsersMeta(String... value) {
        addEquals(UsersColumns.USERS_META, value);
        return this;
    }

    public TopicUsersSelection usersUsersMetaNot(String... value) {
        addNotEquals(UsersColumns.USERS_META, value);
        return this;
    }

    public TopicUsersSelection usersUsersMetaLike(String... value) {
        addLike(UsersColumns.USERS_META, value);
        return this;
    }

    public TopicUsersSelection usersUsersMetaContains(String... value) {
        addContains(UsersColumns.USERS_META, value);
        return this;
    }

    public TopicUsersSelection usersUsersMetaStartsWith(String... value) {
        addStartsWith(UsersColumns.USERS_META, value);
        return this;
    }

    public TopicUsersSelection usersUsersMetaEndsWith(String... value) {
        addEndsWith(UsersColumns.USERS_META, value);
        return this;
    }

    public TopicUsersSelection orderByUsersUsersMeta(boolean desc) {
        orderBy(UsersColumns.USERS_META, desc);
        return this;
    }

    public TopicUsersSelection orderByUsersUsersMeta() {
        orderBy(UsersColumns.USERS_META, false);
        return this;
    }
}
