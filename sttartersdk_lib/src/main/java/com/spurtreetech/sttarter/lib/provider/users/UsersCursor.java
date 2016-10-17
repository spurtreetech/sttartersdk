package com.spurtreetech.sttarter.lib.provider.users;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.spurtreetech.sttarter.lib.provider.base.AbstractCursor;

/**
 * Cursor wrapper for the {@code users} table.
 */
public class UsersCursor extends AbstractCursor implements UsersModel {
    public UsersCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Primary key.
     */
    public long getId() {
        Long res = getLongOrNull(UsersColumns._ID);
        if (res == null)
            throw new NullPointerException("The value of '_id' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code users_user_id} value.
     */
    public long getUsersUserId() {
        Long res = getLongOrNull(UsersColumns.USERS_USER_ID);
        if (res == null)
            throw new NullPointerException("The value of 'users_user_id' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code users_name} value.
     * Cannot be {@code null}.
     */
    @NonNull
    public String getUsersName() {
        String res = getStringOrNull(UsersColumns.USERS_NAME);
        if (res == null)
            throw new NullPointerException("The value of 'users_name' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code users_username} value.
     * Cannot be {@code null}.
     */
    @NonNull
    public String getUsersUsername() {
        String res = getStringOrNull(UsersColumns.USERS_USERNAME);
        if (res == null)
            throw new NullPointerException("The value of 'users_username' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code users_email} value.
     * Cannot be {@code null}.
     */
    @NonNull
    public String getUsersEmail() {
        String res = getStringOrNull(UsersColumns.USERS_EMAIL);
        if (res == null)
            throw new NullPointerException("The value of 'users_email' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code users_mobile} value.
     * Cannot be {@code null}.
     */
    @NonNull
    public String getUsersMobile() {
        String res = getStringOrNull(UsersColumns.USERS_MOBILE);
        if (res == null)
            throw new NullPointerException("The value of 'users_mobile' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code users_avatar} value.
     * Cannot be {@code null}.
     */
    @NonNull
    public String getUsersAvatar() {
        String res = getStringOrNull(UsersColumns.USERS_AVATAR);
        if (res == null)
            throw new NullPointerException("The value of 'users_avatar' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code users_meta} value.
     * Cannot be {@code null}.
     */
    @NonNull
    public String getUsersMeta() {
        String res = getStringOrNull(UsersColumns.USERS_META);
        if (res == null)
            throw new NullPointerException("The value of 'users_meta' in the database was null, which is not allowed according to the model definition");
        return res;
    }
}
