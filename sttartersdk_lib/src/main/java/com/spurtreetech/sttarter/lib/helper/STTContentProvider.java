package com.spurtreetech.sttarter.lib.helper;

/**
 * Created by RahulT on 26-06-2015.
 */
/*
public class STTContentProvider extends ContentProvider {

    // Creates a UriMatcher object.
    private static final UriMatcher sUriMatcher;

    public static final String AUTHORITY = "com.spurtreetech.sttarter.lib.helper";
    public static final Uri TOPICS_URI = Uri.parse("content://" + AUTHORITY + "/" + DatabaseHelper.TABLE_TOPICS);
    public static final Uri  MESSAGES_URI = Uri.parse("content://" + AUTHORITY + "/" + DatabaseHelper.TABLE_MESSAGES);



    private static final int TOPICS = 0;
    private static final int VIEWABLE_TOPICS = 1;
    private static final int MESSAGES = 2;
    private static final int SINGLE_TOPIC_MESSAGES = 3;
    private static final int MESSAGE_EXISTS = 4;
    private static final int MESSAGE_BY_SENDER = 5;
    private static final int UPDATE_MESSAGE_STATUS = 6;
    private static final int GET_MY_TOPICS = 7;
    private static final int GET_ALL_TOPICS = 8;
    private static final int GET_BUZZ_FEED = 9;
    private static final int TOPIC_EXISTS = 10;
    private static final int BUZZ_FEED_TOPIC = 11;
    private static final int GET_LATEST_MESSAGE = 12;
    private static final int UPDATE_SUBSCRIBE_STATUS = 13;
    private static final int IS_TOPIC_SUBSCRIBED = 14;
    private static final int ALLOW_REPLY = 15;
    private static final int UPDATE_ACTIVE_TIME = 16;
    private static final int GET_TOPIC_INFO = 17;

    static {

        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        sUriMatcher.addURI(AUTHORITY, DatabaseHelper.TABLE_TOPICS, TOPICS);
        sUriMatcher.addURI(AUTHORITY, DatabaseHelper.TABLE_TOPICS + "/viewable", VIEWABLE_TOPICS);
        //sUriMatcher.addURI(AUTHORITY, DatabaseHelper.TABLE_TOPICS, GET_ALL_VIEWABLE_TOPICS);
        //sUriMatcher.addURI(AUTHORITY, DatabaseHelper.TABLE_TOPICS, DELETE_ALL_TOPICS);
        //sUriMatcher.addURI(AUTHORITY, DatabaseHelper.TABLE_TOPICS, DELETE_ALL_VIEWABLE_TOPICS);

        sUriMatcher.addURI(AUTHORITY, DatabaseHelper.TABLE_MESSAGES, MESSAGES);
        //sUriMatcher.addURI(AUTHORITY, DatabaseHelper.TABLE_MESSAGES, INSERT_MESSAGE_ROW);

        sUriMatcher.addURI(AUTHORITY, DatabaseHelper.TABLE_MESSAGES + "/singleTopicMessages", SINGLE_TOPIC_MESSAGES);
        //sUriMatcher.addURI(AUTHORITY, DatabaseHelper.TABLE_MESSAGES+"/#", ALL_MESSAGES_FOR);
        sUriMatcher.addURI(AUTHORITY, DatabaseHelper.TABLE_MESSAGES + "/ifMessageExists", MESSAGE_EXISTS);

        sUriMatcher.addURI(AUTHORITY, DatabaseHelper.TABLE_MESSAGES + "/ifMessageBySender", MESSAGE_BY_SENDER);

        sUriMatcher.addURI(AUTHORITY, DatabaseHelper.TABLE_MESSAGES + "/updateMessageStatus", UPDATE_MESSAGE_STATUS);

        sUriMatcher.addURI(AUTHORITY, DatabaseHelper.TABLE_TOPICS + "/getMyTopics", GET_MY_TOPICS);

        sUriMatcher.addURI(AUTHORITY, DatabaseHelper.TABLE_TOPICS + "/getAllTopics", GET_ALL_TOPICS);

        sUriMatcher.addURI(AUTHORITY, DatabaseHelper.TABLE_MESSAGES + "/getBuzzFeed", GET_BUZZ_FEED);

        sUriMatcher.addURI(AUTHORITY, DatabaseHelper.TABLE_TOPICS + "/ifTopicExists", TOPIC_EXISTS);

        sUriMatcher.addURI(AUTHORITY, DatabaseHelper.TABLE_TOPICS + "/buzzFeedTopic", BUZZ_FEED_TOPIC);

        sUriMatcher.addURI(AUTHORITY, DatabaseHelper.TABLE_MESSAGES + "/getLatestMessage", GET_LATEST_MESSAGE);

        sUriMatcher.addURI(AUTHORITY, DatabaseHelper.TABLE_TOPICS + "/updateSubscribeStatus", UPDATE_SUBSCRIBE_STATUS);

        sUriMatcher.addURI(AUTHORITY, DatabaseHelper.TABLE_TOPICS + "/isTopicSubscribed", IS_TOPIC_SUBSCRIBED);

        sUriMatcher.addURI(AUTHORITY, DatabaseHelper.TABLE_TOPICS + "/updateActiveTime", UPDATE_ACTIVE_TIME);

        sUriMatcher.addURI(AUTHORITY, DatabaseHelper.TABLE_TOPICS + "/getTopicInfo", GET_TOPIC_INFO);
    }

    private static DatabaseHelper dbHelper = null;

    @Override
    public boolean onCreate() {
        return false;
    }

    public static SQLiteDatabase getDatabase() {
        try {
            if(dbHelper == null) {
                dbHelper = new DatabaseHelper(STTarter.getInstance().getContext());
            }
        } catch (STTarter.ContextNotInitializedException e) {
            e.printStackTrace();
        }

        return dbHelper.getWritableDatabase();
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String table = "";
        Uri content_uri = null;
        SQLiteDatabase db = getDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        String messageHash = "";

        //Log.d("STTContentProvider", " LatPathSegment: " + uri.getLastPathSegment());

        switch (sUriMatcher.match(uri)) {
            case TOPICS:
                table = DatabaseHelper.TABLE_TOPICS;
                content_uri = TOPICS_URI;
                queryBuilder.setTables(table);
                selection = DatabaseHelper.COLUMN_TOPIC_TYPE + " NOT LIKE '%master%'";
                break;
            case VIEWABLE_TOPICS:
                table = DatabaseHelper.TABLE_TOPICS;
                content_uri = TOPICS_URI;
                queryBuilder.setTables(table);
                selection = DatabaseHelper.COLUMN_TOPIC_TYPE + " NOT LIKE '%master%'";
                break;
            case MESSAGES:
                table = DatabaseHelper.TABLE_MESSAGES;
                content_uri = MESSAGES_URI;
                queryBuilder.setTables(table);
                break;
            case SINGLE_TOPIC_MESSAGES:
                table = DatabaseHelper.TABLE_MESSAGES;
                //content_uri = MESSAGES_URI;
                //String topicName = uri.getLastPathSegment();
                //selection = DatabaseHelper.COLUMN_MESSAGE_TOPIC + " LIKE '%" + topicName + "%'";
                sortOrder = DatabaseHelper.COLUMN_MESSAGE_UNIX_TIMESTAMP + " ASC";
                queryBuilder.setTables(table);
                break;
            case MESSAGE_EXISTS:
                table = DatabaseHelper.TABLE_MESSAGES;
                //content_uri = MESSAGES_URI;
                //messageHash = uri.getLastPathSegment();
                //selection = DatabaseHelper.COLUMN_MESSAGE_HASH + " LIKE '%" + messageHash + "%'";
                queryBuilder.setTables(table);
                break;
            case MESSAGE_BY_SENDER:
                table = DatabaseHelper.TABLE_MESSAGES;
                //content_uri = MESSAGES_URI;
                //messageHash = uri.getLastPathSegment();
                //selection = DatabaseHelper.COLUMN_MESSAGE_HASH + " LIKE '%" + messageHash + "%' AND " + DatabaseHelper.COLUMN_IS_SENDER + "==1";
                queryBuilder.setTables(table);
                break;
            case GET_MY_TOPICS:
                selection = DatabaseHelper.COLUMN_TOPIC_IS_SUBSCRIBED + " = 1 AND NOT " + DatabaseHelper.COLUMN_TOPIC_TYPE + " = 'org' AND NOT " + DatabaseHelper.COLUMN_TOPIC_TYPE + " = 'master'";
                queryBuilder.setTables(DatabaseHelper.TABLE_TOPICS);
                sortOrder = DatabaseHelper.COLUMN_TOPIC_UNIX_TIMESTAMP + " DESC";
                break;
            case GET_ALL_TOPICS:
                selection = "NOT " + DatabaseHelper.COLUMN_TOPIC_TYPE + " = 'org' AND NOT " + DatabaseHelper.COLUMN_TOPIC_TYPE + " = 'master' AND " + DatabaseHelper.COLUMN_TOPIC_IS_PUBLIC + " = 1";
                queryBuilder.setTables(DatabaseHelper.TABLE_TOPICS);
                //sortOrder = DatabaseHelper.COLUMN_TOPIC_UNIX_TIMESTAMP + " ASC";
                //queryBuilder.
                break;
            case GET_BUZZ_FEED:
                queryBuilder.setTables(DatabaseHelper.TABLE_MESSAGES);
                selection = "NOT " + DatabaseHelper.COLUMN_MESSAGE_TOPIC + " = 'org' AND NOT type='group'";
                break;
            case TOPIC_EXISTS:
                queryBuilder.setTables(DatabaseHelper.TABLE_TOPICS);
                break;
            case BUZZ_FEED_TOPIC:
                queryBuilder.setTables(DatabaseHelper.TABLE_TOPICS);
                selection = DatabaseHelper.COLUMN_TOPIC_TYPE + " = 'org'";
                break;
            case GET_LATEST_MESSAGE:
                queryBuilder.setTables(DatabaseHelper.TABLE_MESSAGES);
                break;
            case IS_TOPIC_SUBSCRIBED:
                queryBuilder.setTables(DatabaseHelper.TABLE_TOPICS);
                break;
            case GET_TOPIC_INFO:
                queryBuilder.setTables(DatabaseHelper.TABLE_TOPICS);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        getContext().getContentResolver().notifyChange(uri, null);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String table = "";
        Uri content_uri = null;
        SQLiteDatabase db = getDatabase();

        switch (sUriMatcher.match(uri)) {
            case TOPICS:
                table = DatabaseHelper.TABLE_TOPICS;
                content_uri = TOPICS_URI;
                break;
            case MESSAGES:
                table = DatabaseHelper.TABLE_MESSAGES;
                content_uri = MESSAGES_URI;
                //String topicname = uri.getLastPathSegment();
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        long id = 0;
        try {
            id = db.insertOrThrow(table, null, values);
            //getContext().getContentResolver().notifyChange(uri, null);
            getContext().getContentResolver().notifyChange(Uri.parse("content://" + STTContentProvider.AUTHORITY + "/" + DatabaseHelper.TABLE_TOPICS + "/getAllTopics"), null);
            getContext().getContentResolver().notifyChange(Uri.parse("content://" + STTContentProvider.AUTHORITY + "/" + DatabaseHelper.TABLE_TOPICS + "/getMyTopics"), null);
            getContext().getContentResolver().notifyChange(Uri.parse("content://" + STTContentProvider.AUTHORITY + "/" + DatabaseHelper.TABLE_MESSAGES + "/singleTopicMessages"), null);
            // Notify the provider

        } catch (SQLiteConstraintException e) {
            e.printStackTrace();
        }

        return Uri.parse(content_uri + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String table = "";
        Uri content_uri = null;
        SQLiteDatabase db = getDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (sUriMatcher.match(uri)) {
            case TOPICS:
                table = DatabaseHelper.TABLE_TOPICS;
                content_uri = TOPICS_URI;
                break;
            case VIEWABLE_TOPICS:
                table = DatabaseHelper.TABLE_TOPICS;
                content_uri = TOPICS_URI;
                break;
            case MESSAGES:
                table = DatabaseHelper.TABLE_MESSAGES;
                content_uri = MESSAGES_URI;
                break;
            case SINGLE_TOPIC_MESSAGES:
                table = DatabaseHelper.TABLE_MESSAGES;
                queryBuilder.setTables(DatabaseHelper.TABLE_MESSAGES);
                queryBuilder.appendWhere(DatabaseHelper.TABLE_MESSAGES + "." + DatabaseHelper.COLUMN_MESSAGE_TOPIC + " = " + uri.getLastPathSegment());
                //rowId = uri.getLastPathSegment();
                content_uri = MESSAGES_URI;
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        int deleteCount = db.delete(table, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);

        return deleteCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        String table = "";
        Uri content_uri = null;
        SQLiteDatabase db = getDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (sUriMatcher.match(uri)) {
            case UPDATE_MESSAGE_STATUS:
                table = DatabaseHelper.TABLE_MESSAGES;
                queryBuilder.setTables(DatabaseHelper.TABLE_MESSAGES);
                //rowId = uri.getLastPathSegment();
                //content_uri = MESSAGES_URI;
                break;
            case TOPICS:
                table = DatabaseHelper.TABLE_TOPICS;
                queryBuilder.setTables(DatabaseHelper.TABLE_TOPICS);
                break;
            case UPDATE_SUBSCRIBE_STATUS:
                table = DatabaseHelper.TABLE_TOPICS;
                break;
            case UPDATE_ACTIVE_TIME:
                table = DatabaseHelper.TABLE_TOPICS;
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        int updateCount = db.update(table, values, selection, selectionArgs);
        //getContext().getContentResolver().notifyChange(uri, null);
        getContext().getContentResolver().notifyChange(Uri.parse("content://" + STTContentProvider.AUTHORITY + "/" + DatabaseHelper.TABLE_TOPICS + "/getAllTopics"), null);
        getContext().getContentResolver().notifyChange(Uri.parse("content://" + STTContentProvider.AUTHORITY + "/" + DatabaseHelper.TABLE_TOPICS + "/getMyTopics"), null);

        return updateCount;
    }
}
*/