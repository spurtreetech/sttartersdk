package com.spurtreetech.sttarter.lib.provider;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.DefaultDatabaseErrorHandler;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import com.spurtreetech.sttarter.lib.helper.BuildConfig;
import com.spurtreetech.sttarter.lib.provider.messages.MessagesColumns;
import com.spurtreetech.sttarter.lib.provider.topics.TopicsColumns;
import com.spurtreetech.sttarter.lib.provider.topicusers.TopicUsersColumns;
import com.spurtreetech.sttarter.lib.provider.users.UsersColumns;

import java.util.ArrayList;

public class STTSQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = STTSQLiteOpenHelper.class.getSimpleName();

    public static final String DATABASE_FILE_NAME = "sttarter.db";
    private static final int DATABASE_VERSION = 1;
    private static STTSQLiteOpenHelper sInstance;
    private final Context mContext;
    private final STTSQLiteOpenHelperCallbacks mOpenHelperCallbacks;

    // @formatter:off
    public static final String SQL_CREATE_TABLE_MESSAGES = "CREATE TABLE IF NOT EXISTS "
            + MessagesColumns.TABLE_NAME + " ( "
            + MessagesColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + MessagesColumns.MESSAGE_TYPE + " TEXT NOT NULL, "
            + MessagesColumns.MESSAGE_TEXT + " TEXT NOT NULL, "
            + MessagesColumns.MESSAGE_TOPIC + " TEXT NOT NULL, "
            + MessagesColumns.MESSAGE_TOPIC_ID + " INTEGER NOT NULL, "
            + MessagesColumns.FILE_TYPE + " TEXT NOT NULL DEFAULT 'none', "
            + MessagesColumns.FILE_URL + " TEXT NOT NULL, "
            + MessagesColumns.IS_SENDER + " INTEGER NOT NULL DEFAULT 0, "
            + MessagesColumns.IS_DELIVERED + " INTEGER NOT NULL DEFAULT 0, "
            + MessagesColumns.IS_READ + " INTEGER NOT NULL DEFAULT 0, "
            + MessagesColumns.MESSAGE_FROM + " TEXT NOT NULL DEFAULT 'false', "
            + MessagesColumns.MESSAGE_TIMESTAMP + " INTEGER NOT NULL, "
            + MessagesColumns.UNIX_TIMESTAMP + " INTEGER NOT NULL, "
            + MessagesColumns.MESSAGE_HASH + " TEXT NOT NULL "
            + ", CONSTRAINT fk_message_topic_id FOREIGN KEY (" + MessagesColumns.MESSAGE_TOPIC_ID + ") REFERENCES topics (_id) ON DELETE CASCADE"
            + " );";

    public static final String SQL_CREATE_TABLE_TOPIC_USERS = "CREATE TABLE IF NOT EXISTS "
            + TopicUsersColumns.TABLE_NAME + " ( "
            + TopicUsersColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TopicUsersColumns.MESSAGE_TOPIC_ID + " INTEGER NOT NULL, "
            + TopicUsersColumns.USER_ID + " INTEGER NOT NULL "
            + ", CONSTRAINT fk_message_topic_id FOREIGN KEY (" + TopicUsersColumns.MESSAGE_TOPIC_ID + ") REFERENCES topics (_id) ON DELETE CASCADE"
            + ", CONSTRAINT fk_user_id FOREIGN KEY (" + TopicUsersColumns.USER_ID + ") REFERENCES users (_id) ON DELETE CASCADE"
            + " );";

    public static final String SQL_CREATE_TABLE_TOPICS = "CREATE TABLE IF NOT EXISTS "
            + TopicsColumns.TABLE_NAME + " ( "
            + TopicsColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TopicsColumns.TOPIC_NAME + " TEXT NOT NULL, "
            + TopicsColumns.TOPIC_TYPE + " TEXT NOT NULL, "
            + TopicsColumns.TOPIC_META + " TEXT NOT NULL, "
            + TopicsColumns.TOPIC_IS_SUBSCRIBED + " INTEGER NOT NULL DEFAULT 0, "
            + TopicsColumns.TOPIC_IS_PUBLIC + " INTEGER NOT NULL DEFAULT 0, "
            + TopicsColumns.TOPIC_UPDATED_UNIX_TIMESTAMP + " INTEGER NOT NULL, "
            + TopicsColumns.TOPIC_GROUP_MEMBERS + " TEXT NOT NULL "
            + ", CONSTRAINT unique_topic UNIQUE (topic_name) ON CONFLICT REPLACE"
            + " );";

    public static final String SQL_CREATE_TABLE_USERS = "CREATE TABLE IF NOT EXISTS "
            + UsersColumns.TABLE_NAME + " ( "
            + UsersColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + UsersColumns.USERS_USER_ID + " INTEGER NOT NULL, "
            + UsersColumns.USERS_NAME + " TEXT NOT NULL, "
            + UsersColumns.USERS_USERNAME + " TEXT NOT NULL, "
            + UsersColumns.USERS_EMAIL + " TEXT NOT NULL, "
            + UsersColumns.USERS_MOBILE + " TEXT NOT NULL, "
            + UsersColumns.USERS_AVATAR + " TEXT NOT NULL, "
            + UsersColumns.USERS_META + " TEXT NOT NULL "
            + " );";

    // @formatter:on

    public static STTSQLiteOpenHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = newInstance(context.getApplicationContext());
        }
        return sInstance;
    }

    public static STTSQLiteOpenHelper newInstance(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return newInstancePreHoneycomb(context);
        }
        return newInstancePostHoneycomb(context);
    }


    /*
     * Pre Honeycomb.
     */
    private static STTSQLiteOpenHelper newInstancePreHoneycomb(Context context) {
        return new STTSQLiteOpenHelper(context);
    }

    private STTSQLiteOpenHelper(Context context) {
        super(context, DATABASE_FILE_NAME, null, DATABASE_VERSION);
        mContext = context;
        mOpenHelperCallbacks = new STTSQLiteOpenHelperCallbacks();
    }


    /*
     * Post Honeycomb.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static STTSQLiteOpenHelper newInstancePostHoneycomb(Context context) {
        return new STTSQLiteOpenHelper(context, new DefaultDatabaseErrorHandler());
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private STTSQLiteOpenHelper(Context context, DatabaseErrorHandler errorHandler) {
        super(context, DATABASE_FILE_NAME, null, DATABASE_VERSION, errorHandler);
        mContext = context;
        mOpenHelperCallbacks = new STTSQLiteOpenHelperCallbacks();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onCreate");
        mOpenHelperCallbacks.onPreCreate(mContext, db);
        db.execSQL(SQL_CREATE_TABLE_MESSAGES);
        db.execSQL(SQL_CREATE_TABLE_TOPIC_USERS);
        db.execSQL(SQL_CREATE_TABLE_TOPICS);
        db.execSQL(SQL_CREATE_TABLE_USERS);
        mOpenHelperCallbacks.onPostCreate(mContext, db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            setForeignKeyConstraintsEnabled(db);
        }
        mOpenHelperCallbacks.onOpen(mContext, db);
    }

    private void setForeignKeyConstraintsEnabled(SQLiteDatabase db) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            setForeignKeyConstraintsEnabledPreJellyBean(db);
        } else {
            setForeignKeyConstraintsEnabledPostJellyBean(db);
        }
    }

    private void setForeignKeyConstraintsEnabledPreJellyBean(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setForeignKeyConstraintsEnabledPostJellyBean(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        mOpenHelperCallbacks.onUpgrade(mContext, db, oldVersion, newVersion);
    }

    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "mesage" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);


        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);


            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {


                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }


    }

}
