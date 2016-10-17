package com.spurtreetech.sttarter.lib.helper.experiment;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;

/**
 * Created by RahulT on 16-07-2015.
 */
public class ChatHistoryLoaderHelper implements LoaderManager.LoaderCallbacks<Cursor>{

    Activity activity;
    LoaderManager loaderManager;
    IChatLoader chatLoader;

    public static final String CHAT_DATA = "CHAT_DATA";
    public static final String CHAT_HISTORY_DATA = "CHAT_HISTORY_DATA";

    ChatHistoryLoaderHelper(Activity activity, IChatLoader chatLoader) {
        this.activity = activity;
        this.loaderManager = this.activity.getLoaderManager();
        this.chatLoader = chatLoader;
    }

    public void initiateChatHistoryLoader() {
        loaderManager.initLoader(0, null, this);
    }

    public void initiateChatLoader() {
        loaderManager.initLoader(1, null, this);
    }

    /*
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = null;
        switch (id) {
            case 0:
                uri = STTContentProvider.TOPICS_URI;
                break;
            case 1:
                uri = STTContentProvider.MESSAGES_URI;
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        switch(loader.getId()) {
            case 0:
                if(data != null) {
                    data.setNotificationUri(
                            activity.getContentResolver(),
                            STTContentProvider.TOPICS_URI);
                    // TODO call the interface, pass the cursor and chat tag
                    chatLoader.chatLoaded(data, CHAT_HISTORY_DATA);

                }
                break;
            case 1:
                data.setNotificationUri(
                        activity.getContentResolver(),
                        STTContentProvider.MESSAGES_URI);
                // TODO call the interface with the new cursor, and the tag for the kind of data it is
                chatLoader.chatLoaded(data, CHAT_DATA);

        }
    }
    */

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    interface IChatLoader {
        void chatLoaded(Cursor data, String dataTag);
    }
}
