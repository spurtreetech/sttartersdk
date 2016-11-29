package com.sttarter.helper.utils;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.sttarter.provider.topics.TopicsColumns;
import com.sttarter.provider.topics.TopicsSelection;

import com.sttarter.helper.interfaces.GetCursor;

import static com.sttarter.helper.utils.CommonFunctions.cur2Json;

/**
 * Created by Shahbaz on 16-11-2016.
 */

public class GroupCursorLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    Context context;
    GetCursor getCursor;

    public GroupCursorLoader(Context context, GetCursor getCursor){
        this.context = context;
        this.getCursor = getCursor;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        TopicsSelection where = new TopicsSelection();
        switch (id) {
            case 0:
                where.topicIsSubscribed(true).and().topicTypeNot("master").and().topicTypeNot("org");
                where.orderBy(TopicsColumns.TOPIC_UPDATED_UNIX_TIMESTAMP, true);
                break;
            default:
                break;
        }

        CursorLoader cursorLoader = new CursorLoader(context.getApplicationContext(), TopicsColumns.CONTENT_URI, null, where.sel(), where.args(), where.order());
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        switch (loader.getId()) {
            case 0:

                Log.d("MessagesFragment", "Data Loaded: data size - " + data.getCount());
                data.setNotificationUri(
                        context.getContentResolver(),
                        TopicsColumns.CONTENT_URI);

                getCursor.getCursor(data);
                getCursor.getCursorJsonArrayString(cur2Json(data));


                break;

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
