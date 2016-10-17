package com.spurtreetech.sttarter.lib.helper;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

/**
 * Created by RahulT on 13-07-2015.
 */
public class BroadcastHelper {
    Context context;

    public BroadcastHelper(Context context) {
        this.context = context;
    }

    public void sendMessage(String successful) {
        Log.d("sender", "Broadcasting message");
        Intent intent = new Intent("test");
        intent.putExtra("message", successful);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public void sendSystemMessage(String message, SysMessage type, String topic) {
        Intent intent = new Intent("system");
        intent.putExtra("message", message);
        intent.putExtra("type", type.toString());
        intent.putExtra("topic", topic);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

   /* public void loadEvents() {
        Log.d("sender", "Broadcasting message");
        Intent intent = new Intent("load-events");
        // You can also include some extra data.
        intent.putExtra("message", "load-events");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public void refershAssignments() {
        Log.d("refesh", "Assignments");
        Intent intent = new Intent("refersh-assignments");
        // You can also include some extra data.
        intent.putExtra("message", "refersh-assignments");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public void loadNotification() {
        Log.d("sender", "loadNotification Broadcasting message");
        Intent intent = new Intent(Constants.BRDCST_NOTIFICATION);
        intent.putExtra(Constants.MESSAGE,Constants.BRDCST_NOTIFICATION);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }*/
}