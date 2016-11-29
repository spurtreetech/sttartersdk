package com.sttarter.helper.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.sttarter.provider.messages.MessagesColumns;
import com.sttarter.provider.messages.MessagesSelection;

import com.sttarter.helper.interfaces.GetCursor;

import static com.sttarter.helper.utils.CommonFunctions.cur2Json;

/**
 * Created by Shahbaz on 16-11-2016.
 */

public class MessageCursorLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    Context context;
    String topic;
    GetCursor getCursor;

    public MessageCursorLoader(Context context, String topic, GetCursor getCursor){
        this.context = context;
        this.topic = topic;
        this.getCursor = getCursor;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri;
        String selection = null;
        MessagesSelection where = new MessagesSelection();
        switch (id) {
            case 0:
                where.topicsTopicName(topic);
                //uri = Uri.parse("content://" + STTContentProvider.AUTHORITY + "/" + DatabaseHelper.TABLE_MESSAGES + "/singleTopicMessages");
                //selection = DatabaseHelper.COLUMN_MESSAGE_TOPIC + " LIKE '%" + topic + "%'";
                Log.d("ChatActivity Loader", "CursorLoader initialized");
                break;
            default:
                uri = null;
        }
        CursorLoader cursorLoader = new CursorLoader(context, MessagesColumns.CONTENT_URI, null, where.sel(), where.args(), null);

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case 0:
                data.setNotificationUri(
                        context.getContentResolver(),
                        MessagesColumns.CONTENT_URI);
                 getCursor.getCursor(data);
                 getCursor.getCursorJsonArrayString(cur2Json(data));

                break;
            default:
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
