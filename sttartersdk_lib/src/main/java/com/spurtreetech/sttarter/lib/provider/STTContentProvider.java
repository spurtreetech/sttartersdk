package com.spurtreetech.sttarter.lib.provider;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import com.spurtreetech.sttarter.lib.helper.BuildConfig;
import com.spurtreetech.sttarter.lib.provider.base.BaseContentProvider;
import com.spurtreetech.sttarter.lib.provider.messages.MessagesColumns;
import com.spurtreetech.sttarter.lib.provider.topics.TopicsColumns;
import com.spurtreetech.sttarter.lib.provider.topicusers.TopicUsersColumns;
import com.spurtreetech.sttarter.lib.provider.users.UsersColumns;

import java.util.Arrays;

public class STTContentProvider extends BaseContentProvider {
    private static final String TAG = STTContentProvider.class.getSimpleName();

    private static final boolean DEBUG = BuildConfig.DEBUG;

    private static final String TYPE_CURSOR_ITEM = "vnd.android.cursor.item/";
    private static final String TYPE_CURSOR_DIR = "vnd.android.cursor.dir/";

    public static final String AUTHORITY = "com.spurtreetech.sttarter.lib.provider";
    public static final String CONTENT_URI_BASE = "content://" + AUTHORITY;

    private static final int URI_TYPE_MESSAGES = 0;
    private static final int URI_TYPE_MESSAGES_ID = 1;

    private static final int URI_TYPE_TOPIC_USERS = 2;
    private static final int URI_TYPE_TOPIC_USERS_ID = 3;

    private static final int URI_TYPE_TOPICS = 4;
    private static final int URI_TYPE_TOPICS_ID = 5;

    private static final int URI_TYPE_USERS = 6;
    private static final int URI_TYPE_USERS_ID = 7;



    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(AUTHORITY, MessagesColumns.TABLE_NAME, URI_TYPE_MESSAGES);
        URI_MATCHER.addURI(AUTHORITY, MessagesColumns.TABLE_NAME + "/#", URI_TYPE_MESSAGES_ID);
        URI_MATCHER.addURI(AUTHORITY, TopicUsersColumns.TABLE_NAME, URI_TYPE_TOPIC_USERS);
        URI_MATCHER.addURI(AUTHORITY, TopicUsersColumns.TABLE_NAME + "/#", URI_TYPE_TOPIC_USERS_ID);
        URI_MATCHER.addURI(AUTHORITY, TopicsColumns.TABLE_NAME, URI_TYPE_TOPICS);
        URI_MATCHER.addURI(AUTHORITY, TopicsColumns.TABLE_NAME + "/#", URI_TYPE_TOPICS_ID);
        URI_MATCHER.addURI(AUTHORITY, UsersColumns.TABLE_NAME, URI_TYPE_USERS);
        URI_MATCHER.addURI(AUTHORITY, UsersColumns.TABLE_NAME + "/#", URI_TYPE_USERS_ID);
    }

    @Override
    protected SQLiteOpenHelper createSqLiteOpenHelper() {
        return STTSQLiteOpenHelper.getInstance(getContext());
    }

    @Override
    protected boolean hasDebug() {
        return DEBUG;
    }

    @Override
    public String getType(Uri uri) {
        int match = URI_MATCHER.match(uri);
        switch (match) {
            case URI_TYPE_MESSAGES:
                return TYPE_CURSOR_DIR + MessagesColumns.TABLE_NAME;
            case URI_TYPE_MESSAGES_ID:
                return TYPE_CURSOR_ITEM + MessagesColumns.TABLE_NAME;

            case URI_TYPE_TOPIC_USERS:
                return TYPE_CURSOR_DIR + TopicUsersColumns.TABLE_NAME;
            case URI_TYPE_TOPIC_USERS_ID:
                return TYPE_CURSOR_ITEM + TopicUsersColumns.TABLE_NAME;

            case URI_TYPE_TOPICS:
                return TYPE_CURSOR_DIR + TopicsColumns.TABLE_NAME;
            case URI_TYPE_TOPICS_ID:
                return TYPE_CURSOR_ITEM + TopicsColumns.TABLE_NAME;

            case URI_TYPE_USERS:
                return TYPE_CURSOR_DIR + UsersColumns.TABLE_NAME;
            case URI_TYPE_USERS_ID:
                return TYPE_CURSOR_ITEM + UsersColumns.TABLE_NAME;

        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (DEBUG) Log.d(TAG, "insert uri=" + uri + " values=" + values);
        return super.insert(uri, values);
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        if (DEBUG) Log.d(TAG, "bulkInsert uri=" + uri + " values.length=" + values.length);
        return super.bulkInsert(uri, values);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (DEBUG) Log.d(TAG, "update uri=" + uri + " values=" + values + " selection=" + selection + " selectionArgs=" + Arrays.toString(selectionArgs));
        return super.update(uri, values, selection, selectionArgs);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (DEBUG) Log.d(TAG, "delete uri=" + uri + " selection=" + selection + " selectionArgs=" + Arrays.toString(selectionArgs));
        return super.delete(uri, selection, selectionArgs);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (DEBUG)
            Log.d(TAG, "query uri=" + uri + " selection=" + selection + " selectionArgs=" + Arrays.toString(selectionArgs) + " sortOrder=" + sortOrder
                    + " groupBy=" + uri.getQueryParameter(QUERY_GROUP_BY) + " having=" + uri.getQueryParameter(QUERY_HAVING) + " limit=" + uri.getQueryParameter(QUERY_LIMIT));
        return super.query(uri, projection, selection, selectionArgs, sortOrder);
    }

    @Override
    protected QueryParams getQueryParams(Uri uri, String selection, String[] projection) {
        QueryParams res = new QueryParams();
        String id = null;
        int matchedId = URI_MATCHER.match(uri);
        switch (matchedId) {
            case URI_TYPE_MESSAGES:
            case URI_TYPE_MESSAGES_ID:
                res.table = MessagesColumns.TABLE_NAME;
                res.idColumn = MessagesColumns._ID;
                res.tablesWithJoins = MessagesColumns.TABLE_NAME;
                if (TopicsColumns.hasColumns(projection)) {
                    res.tablesWithJoins += " LEFT OUTER JOIN " + TopicsColumns.TABLE_NAME + " AS " + MessagesColumns.PREFIX_TOPICS + " ON " + MessagesColumns.TABLE_NAME + "." + MessagesColumns.MESSAGE_TOPIC_ID + "=" + MessagesColumns.PREFIX_TOPICS + "." + TopicsColumns._ID;
                }
                res.orderBy = MessagesColumns.DEFAULT_ORDER;
                break;

            case URI_TYPE_TOPIC_USERS:
            case URI_TYPE_TOPIC_USERS_ID:
                res.table = TopicUsersColumns.TABLE_NAME;
                res.idColumn = TopicUsersColumns._ID;
                res.tablesWithJoins = TopicUsersColumns.TABLE_NAME;
                if (TopicsColumns.hasColumns(projection)) {
                    res.tablesWithJoins += " LEFT OUTER JOIN " + TopicsColumns.TABLE_NAME + " AS " + TopicUsersColumns.PREFIX_TOPICS + " ON " + TopicUsersColumns.TABLE_NAME + "." + TopicUsersColumns.MESSAGE_TOPIC_ID + "=" + TopicUsersColumns.PREFIX_TOPICS + "." + TopicsColumns._ID;
                }
                if (UsersColumns.hasColumns(projection)) {
                    res.tablesWithJoins += " LEFT OUTER JOIN " + UsersColumns.TABLE_NAME + " AS " + TopicUsersColumns.PREFIX_USERS + " ON " + TopicUsersColumns.TABLE_NAME + "." + TopicUsersColumns.USER_ID + "=" + TopicUsersColumns.PREFIX_USERS + "." + UsersColumns._ID;
                }
                res.orderBy = TopicUsersColumns.DEFAULT_ORDER;
                break;

            case URI_TYPE_TOPICS:
            case URI_TYPE_TOPICS_ID:
                res.table = TopicsColumns.TABLE_NAME;
                res.idColumn = TopicsColumns._ID;
                res.tablesWithJoins = TopicsColumns.TABLE_NAME;
                res.orderBy = TopicsColumns.DEFAULT_ORDER;
                break;

            case URI_TYPE_USERS:
            case URI_TYPE_USERS_ID:
                res.table = UsersColumns.TABLE_NAME;
                res.idColumn = UsersColumns._ID;
                res.tablesWithJoins = UsersColumns.TABLE_NAME;
                res.orderBy = UsersColumns.DEFAULT_ORDER;
                break;

            default:
                throw new IllegalArgumentException("The uri '" + uri + "' is not supported by this ContentProvider");
        }

        switch (matchedId) {
            case URI_TYPE_MESSAGES_ID:
            case URI_TYPE_TOPIC_USERS_ID:
            case URI_TYPE_TOPICS_ID:
            case URI_TYPE_USERS_ID:
                id = uri.getLastPathSegment();
        }
        if (id != null) {
            if (selection != null) {
                res.selection = res.table + "." + res.idColumn + "=" + id + " and (" + selection + ")";
            } else {
                res.selection = res.table + "." + res.idColumn + "=" + id;
            }
        } else {
            res.selection = selection;
        }
        return res;
    }
}
