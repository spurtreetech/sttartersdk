package com.spurtreetech.sttarter.lib.provider.users;

import com.spurtreetech.sttarter.lib.provider.base.BaseModel;

import java.util.Date;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * User data will all be stored in this table.
 */
public interface UsersModel extends BaseModel {

    /**
     * Get the {@code users_user_id} value.
     */
    long getUsersUserId();

    /**
     * Get the {@code users_name} value.
     * Cannot be {@code null}.
     */
    @NonNull
    String getUsersName();

    /**
     * Get the {@code users_username} value.
     * Cannot be {@code null}.
     */
    @NonNull
    String getUsersUsername();

    /**
     * Get the {@code users_email} value.
     * Cannot be {@code null}.
     */
    @NonNull
    String getUsersEmail();

    /**
     * Get the {@code users_mobile} value.
     * Cannot be {@code null}.
     */
    @NonNull
    String getUsersMobile();

    /**
     * Get the {@code users_avatar} value.
     * Cannot be {@code null}.
     */
    @NonNull
    String getUsersAvatar();

    /**
     * Get the {@code users_meta} value.
     * Cannot be {@code null}.
     */
    @NonNull
    String getUsersMeta();
}
