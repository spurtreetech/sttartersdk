package com.spurtreetech.sttarter.lib.helper.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.SystemClock;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import org.eclipse.paho.android.service.MqttService;

/**
 * Created by rahul on 18/08/15.
 */
public class WakeUpReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("WakeUpReceiver","Reconnect on Device wakeup.");

        if (isOnline(context)) {
            Log.d("WakeUpReceiver", "Online, restart service.");

            // This is the Intent to deliver to our service.
            Intent service = new Intent(context, MqttService.class);

            // Start the service, keeping the device awake while it is launching.
            Log.i("WakeUpReceiver", "Starting service - @ " + SystemClock.elapsedRealtime());
            startWakefulService(context, service);
        }

    }

    public boolean isOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            Log.d("ConDetect", "connectivity manager" + cm.toString());
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            Log.d("ConDetect", "NetworkInfo" + cm.toString());
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                Log.d("ConDetect", "boolean value : " + netInfo.isConnectedOrConnecting());
                return true;
            }
        } catch (Exception e) {
            for (StackTraceElement tempStack : e.getStackTrace()) {
                // Log.d("Exception thrown: Treeview Fetch", "" +
                // tempStack.getLineNumber());
                Log.d("Exception thrown: ",
                        "" + tempStack.getLineNumber() + " methodName: " + tempStack.getClassName() + "-"
                                + tempStack.getMethodName());
            }
        }
        return false;
    }
}
