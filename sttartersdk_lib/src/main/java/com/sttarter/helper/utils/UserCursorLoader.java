package com.sttarter.helper.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.sttarter.helper.interfaces.GetCursor;
import com.sttarter.provider.users.UsersColumns;
import com.sttarter.provider.users.UsersSelection;

import static com.sttarter.helper.utils.CommonFunctions.cur2Json;

/**
 * Created by Shahbaz on 07-12-2016.
 */

public class UserCursorLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    Context context;
    String userString;
    GetCursor getCursor;

    public UserCursorLoader(Context context, String userString, GetCursor getCursor){
        this.context = context;
        this.userString = userString;
        this.getCursor = getCursor;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri;
        String selection = null;
        UsersSelection where = new UsersSelection();
        switch (id) {
            case 0:
                where.usersNameStartsWith(userString);
                //uri = Uri.parse("content://" + STTContentProvider.AUTHORITY + "/" + DatabaseHelper.TABLE_MESSAGES + "/singleTopicMessages");
                //selection = DatabaseHelper.COLUMN_MESSAGE_TOPIC + " LIKE '%" + topic + "%'";
                Log.d("ChatActivity Loader", "CursorLoader initialized");
                break;
            default:
                uri = null;
        }
        CursorLoader cursorLoader = new CursorLoader(context, UsersColumns.CONTENT_URI, null, where.sel(), where.args(), null);

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case 0:
                data.setNotificationUri(
                        context.getContentResolver(),
                        UsersColumns.CONTENT_URI);
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
