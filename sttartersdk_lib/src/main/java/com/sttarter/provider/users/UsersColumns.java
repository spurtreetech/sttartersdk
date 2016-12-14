package com.sttarter.provider.users;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.google.gson.Gson;
import com.sttarter.common.models.User;
import com.sttarter.provider.STTContentProvider;

/**
 * User data will all be stored in this table.
 */
public class UsersColumns implements BaseColumns {
    public static final String TABLE_NAME = "users";
    public static final Uri CONTENT_URI = Uri.parse(STTContentProvider.CONTENT_URI_BASE + "/" + TABLE_NAME);

    /**
     * Primary key.
     */
    public static final String _ID = BaseColumns._ID;

    public static final String USERS_USER_ID = "users_user_id";

    public static final String USERS_NAME = "users_name";

    public static final String USERS_USERNAME = "users_username";

    public static final String USERS_EMAIL = "users_email";

    public static final String USERS_MOBILE = "users_mobile";

    public static final String USERS_AVATAR = "users_avatar";

    public static final String USERS_META = "users_meta";


    public static final String DEFAULT_ORDER = TABLE_NAME + "." +_ID;

    // @formatter:off
    public static final String[] ALL_COLUMNS = new String[] {
            _ID,
            USERS_USER_ID,
            USERS_NAME,
            USERS_USERNAME,
            USERS_EMAIL,
            USERS_MOBILE,
            USERS_AVATAR,
            USERS_META
    };
    // @formatter:on

    public static boolean hasColumns(String[] projection) {
        if (projection == null) return true;
        for (String c : projection) {
            if (c.equals(USERS_USER_ID) || c.contains("." + USERS_USER_ID)) return true;
            if (c.equals(USERS_NAME) || c.contains("." + USERS_NAME)) return true;
            if (c.equals(USERS_USERNAME) || c.contains("." + USERS_USERNAME)) return true;
            if (c.equals(USERS_EMAIL) || c.contains("." + USERS_EMAIL)) return true;
            if (c.equals(USERS_MOBILE) || c.contains("." + USERS_MOBILE)) return true;
            if (c.equals(USERS_AVATAR) || c.contains("." + USERS_AVATAR)) return true;
            if (c.equals(USERS_META) || c.contains("." + USERS_META)) return true;
        }
        return false;
    }

        
public static User completeRow(Cursor cursor)
    {
        User user = new User();
        try
        {
            user.setStt_id(cursor.getInt(cursor.getColumnIndexOrThrow(USERS_USER_ID)));
            user.setName(cursor.getString(cursor.getColumnIndexOrThrow(USERS_NAME)));
            //user.setCreated_by(cursor.getColumnIndexOrThrow(TOPIC_UPDATED_UNIX_TIMESTAMP)+"");
            user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(USERS_USERNAME)));
            //user.setId("");
            Gson gson = new Gson();
            user.setMeta(cursor.getString(cursor.getColumnIndexOrThrow(USERS_META)));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(USERS_EMAIL)));
            user.setMobile(cursor.getString(cursor.getColumnIndexOrThrow(USERS_MOBILE)));
            user.setAvatar(cursor.getString(cursor.getColumnIndexOrThrow(USERS_AVATAR)));

            //user.setUsers();
            return user;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return user;
    }


}
