package com.spurtreetech.sttarter.lib.provider.users;

import java.util.Date;

import android.content.Context;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.spurtreetech.sttarter.lib.provider.base.AbstractSelection;

/**
 * Selection for the {@code users} table.
 */
public class UsersSelection extends AbstractSelection<UsersSelection> {
    @Override
    protected Uri baseUri() {
        return UsersColumns.CONTENT_URI;
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param contentResolver The content resolver to query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code UsersCursor} object, which is positioned before the first entry, or null.
     */
    public UsersCursor query(ContentResolver contentResolver, String[] projection) {
        Cursor cursor = contentResolver.query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new UsersCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, null)}.
     */
    public UsersCursor query(ContentResolver contentResolver) {
        return query(contentResolver, null);
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param context The context to use for the query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code UsersCursor} object, which is positioned before the first entry, or null.
     */
    public UsersCursor query(Context context, String[] projection) {
        Cursor cursor = context.getContentResolver().query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new UsersCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(context, null)}.
     */
    public UsersCursor query(Context context) {
        return query(context, null);
    }


    public UsersSelection id(long... value) {
        addEquals("users." + UsersColumns._ID, toObjectArray(value));
        return this;
    }

    public UsersSelection idNot(long... value) {
        addNotEquals("users." + UsersColumns._ID, toObjectArray(value));
        return this;
    }

    public UsersSelection orderById(boolean desc) {
        orderBy("users." + UsersColumns._ID, desc);
        return this;
    }

    public UsersSelection orderById() {
        return orderById(false);
    }

    public UsersSelection usersUserId(long... value) {
        addEquals(UsersColumns.USERS_USER_ID, toObjectArray(value));
        return this;
    }

    public UsersSelection usersUserIdNot(long... value) {
        addNotEquals(UsersColumns.USERS_USER_ID, toObjectArray(value));
        return this;
    }

    public UsersSelection usersUserIdGt(long value) {
        addGreaterThan(UsersColumns.USERS_USER_ID, value);
        return this;
    }

    public UsersSelection usersUserIdGtEq(long value) {
        addGreaterThanOrEquals(UsersColumns.USERS_USER_ID, value);
        return this;
    }

    public UsersSelection usersUserIdLt(long value) {
        addLessThan(UsersColumns.USERS_USER_ID, value);
        return this;
    }

    public UsersSelection usersUserIdLtEq(long value) {
        addLessThanOrEquals(UsersColumns.USERS_USER_ID, value);
        return this;
    }

    public UsersSelection orderByUsersUserId(boolean desc) {
        orderBy(UsersColumns.USERS_USER_ID, desc);
        return this;
    }

    public UsersSelection orderByUsersUserId() {
        orderBy(UsersColumns.USERS_USER_ID, false);
        return this;
    }

    public UsersSelection usersName(String... value) {
        addEquals(UsersColumns.USERS_NAME, value);
        return this;
    }

    public UsersSelection usersNameNot(String... value) {
        addNotEquals(UsersColumns.USERS_NAME, value);
        return this;
    }

    public UsersSelection usersNameLike(String... value) {
        addLike(UsersColumns.USERS_NAME, value);
        return this;
    }

    public UsersSelection usersNameContains(String... value) {
        addContains(UsersColumns.USERS_NAME, value);
        return this;
    }

    public UsersSelection usersNameStartsWith(String... value) {
        addStartsWith(UsersColumns.USERS_NAME, value);
        return this;
    }

    public UsersSelection usersNameEndsWith(String... value) {
        addEndsWith(UsersColumns.USERS_NAME, value);
        return this;
    }

    public UsersSelection orderByUsersName(boolean desc) {
        orderBy(UsersColumns.USERS_NAME, desc);
        return this;
    }

    public UsersSelection orderByUsersName() {
        orderBy(UsersColumns.USERS_NAME, false);
        return this;
    }

    public UsersSelection usersUsername(String... value) {
        addEquals(UsersColumns.USERS_USERNAME, value);
        return this;
    }

    public UsersSelection usersUsernameNot(String... value) {
        addNotEquals(UsersColumns.USERS_USERNAME, value);
        return this;
    }

    public UsersSelection usersUsernameLike(String... value) {
        addLike(UsersColumns.USERS_USERNAME, value);
        return this;
    }

    public UsersSelection usersUsernameContains(String... value) {
        addContains(UsersColumns.USERS_USERNAME, value);
        return this;
    }

    public UsersSelection usersUsernameStartsWith(String... value) {
        addStartsWith(UsersColumns.USERS_USERNAME, value);
        return this;
    }

    public UsersSelection usersUsernameEndsWith(String... value) {
        addEndsWith(UsersColumns.USERS_USERNAME, value);
        return this;
    }

    public UsersSelection orderByUsersUsername(boolean desc) {
        orderBy(UsersColumns.USERS_USERNAME, desc);
        return this;
    }

    public UsersSelection orderByUsersUsername() {
        orderBy(UsersColumns.USERS_USERNAME, false);
        return this;
    }

    public UsersSelection usersEmail(String... value) {
        addEquals(UsersColumns.USERS_EMAIL, value);
        return this;
    }

    public UsersSelection usersEmailNot(String... value) {
        addNotEquals(UsersColumns.USERS_EMAIL, value);
        return this;
    }

    public UsersSelection usersEmailLike(String... value) {
        addLike(UsersColumns.USERS_EMAIL, value);
        return this;
    }

    public UsersSelection usersEmailContains(String... value) {
        addContains(UsersColumns.USERS_EMAIL, value);
        return this;
    }

    public UsersSelection usersEmailStartsWith(String... value) {
        addStartsWith(UsersColumns.USERS_EMAIL, value);
        return this;
    }

    public UsersSelection usersEmailEndsWith(String... value) {
        addEndsWith(UsersColumns.USERS_EMAIL, value);
        return this;
    }

    public UsersSelection orderByUsersEmail(boolean desc) {
        orderBy(UsersColumns.USERS_EMAIL, desc);
        return this;
    }

    public UsersSelection orderByUsersEmail() {
        orderBy(UsersColumns.USERS_EMAIL, false);
        return this;
    }

    public UsersSelection usersMobile(String... value) {
        addEquals(UsersColumns.USERS_MOBILE, value);
        return this;
    }

    public UsersSelection usersMobileNot(String... value) {
        addNotEquals(UsersColumns.USERS_MOBILE, value);
        return this;
    }

    public UsersSelection usersMobileLike(String... value) {
        addLike(UsersColumns.USERS_MOBILE, value);
        return this;
    }

    public UsersSelection usersMobileContains(String... value) {
        addContains(UsersColumns.USERS_MOBILE, value);
        return this;
    }

    public UsersSelection usersMobileStartsWith(String... value) {
        addStartsWith(UsersColumns.USERS_MOBILE, value);
        return this;
    }

    public UsersSelection usersMobileEndsWith(String... value) {
        addEndsWith(UsersColumns.USERS_MOBILE, value);
        return this;
    }

    public UsersSelection orderByUsersMobile(boolean desc) {
        orderBy(UsersColumns.USERS_MOBILE, desc);
        return this;
    }

    public UsersSelection orderByUsersMobile() {
        orderBy(UsersColumns.USERS_MOBILE, false);
        return this;
    }

    public UsersSelection usersAvatar(String... value) {
        addEquals(UsersColumns.USERS_AVATAR, value);
        return this;
    }

    public UsersSelection usersAvatarNot(String... value) {
        addNotEquals(UsersColumns.USERS_AVATAR, value);
        return this;
    }

    public UsersSelection usersAvatarLike(String... value) {
        addLike(UsersColumns.USERS_AVATAR, value);
        return this;
    }

    public UsersSelection usersAvatarContains(String... value) {
        addContains(UsersColumns.USERS_AVATAR, value);
        return this;
    }

    public UsersSelection usersAvatarStartsWith(String... value) {
        addStartsWith(UsersColumns.USERS_AVATAR, value);
        return this;
    }

    public UsersSelection usersAvatarEndsWith(String... value) {
        addEndsWith(UsersColumns.USERS_AVATAR, value);
        return this;
    }

    public UsersSelection orderByUsersAvatar(boolean desc) {
        orderBy(UsersColumns.USERS_AVATAR, desc);
        return this;
    }

    public UsersSelection orderByUsersAvatar() {
        orderBy(UsersColumns.USERS_AVATAR, false);
        return this;
    }

    public UsersSelection usersMeta(String... value) {
        addEquals(UsersColumns.USERS_META, value);
        return this;
    }

    public UsersSelection usersMetaNot(String... value) {
        addNotEquals(UsersColumns.USERS_META, value);
        return this;
    }

    public UsersSelection usersMetaLike(String... value) {
        addLike(UsersColumns.USERS_META, value);
        return this;
    }

    public UsersSelection usersMetaContains(String... value) {
        addContains(UsersColumns.USERS_META, value);
        return this;
    }

    public UsersSelection usersMetaStartsWith(String... value) {
        addStartsWith(UsersColumns.USERS_META, value);
        return this;
    }

    public UsersSelection usersMetaEndsWith(String... value) {
        addEndsWith(UsersColumns.USERS_META, value);
        return this;
    }

    public UsersSelection orderByUsersMeta(boolean desc) {
        orderBy(UsersColumns.USERS_META, desc);
        return this;
    }

    public UsersSelection orderByUsersMeta() {
        orderBy(UsersColumns.USERS_META, false);
        return this;
    }
}
