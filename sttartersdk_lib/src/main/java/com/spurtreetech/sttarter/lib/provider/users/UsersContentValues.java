package com.spurtreetech.sttarter.lib.provider.users;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.spurtreetech.sttarter.lib.provider.base.AbstractContentValues;

/**
 * Content values wrapper for the {@code users} table.
 */
public class UsersContentValues extends AbstractContentValues {
    @Override
    public Uri uri() {
        return UsersColumns.CONTENT_URI;
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(ContentResolver contentResolver, @Nullable UsersSelection where) {
        return contentResolver.update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param //contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(Context context, @Nullable UsersSelection where) {
        return context.getContentResolver().update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    public UsersContentValues putUsersUserId(long value) {
        mContentValues.put(UsersColumns.USERS_USER_ID, value);
        return this;
    }


    public UsersContentValues putUsersName(@NonNull String value) {
        if (value == null) throw new IllegalArgumentException("usersName must not be null");
        mContentValues.put(UsersColumns.USERS_NAME, value);
        return this;
    }


    public UsersContentValues putUsersUsername(@NonNull String value) {
        if (value == null) throw new IllegalArgumentException("usersUsername must not be null");
        mContentValues.put(UsersColumns.USERS_USERNAME, value);
        return this;
    }


    public UsersContentValues putUsersEmail(@NonNull String value) {
        if (value == null) throw new IllegalArgumentException("usersEmail must not be null");
        mContentValues.put(UsersColumns.USERS_EMAIL, value);
        return this;
    }


    public UsersContentValues putUsersMobile(@NonNull String value) {
        if (value == null) throw new IllegalArgumentException("usersMobile must not be null");
        mContentValues.put(UsersColumns.USERS_MOBILE, value);
        return this;
    }


    public UsersContentValues putUsersAvatar(@NonNull String value) {
        if (value == null) throw new IllegalArgumentException("usersAvatar must not be null");
        mContentValues.put(UsersColumns.USERS_AVATAR, value);
        return this;
    }


    public UsersContentValues putUsersMeta(@NonNull String value) {
        if (value == null) throw new IllegalArgumentException("usersMeta must not be null");
        mContentValues.put(UsersColumns.USERS_META, value);
        return this;
    }

}
