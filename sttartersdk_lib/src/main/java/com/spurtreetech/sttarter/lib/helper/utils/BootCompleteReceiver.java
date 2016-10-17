package com.spurtreetech.sttarter.lib.helper.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;

import org.eclipse.paho.android.service.MqttService;

/**
 * Created by rahul on 17/08/15.
 */
public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("BootCompletedReceiver", "Online, restart service.");

        // TODO STTarter start service
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm
                .newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MQTT");
        wl.acquire();
        if (isOnline(context)) {
            Log.d("BootCompletedReceiver", "Online, restart service.");

            // This is the Intent to deliver to our service.
            Intent service = new Intent(context, MqttService.class);

            // Start the service, keeping the device awake while it is launching.
            Log.i("BootCompletedReceiver", "Starting service - @ " + SystemClock.elapsedRealtime());
            context.startService(service);
            //startWakefulService(context, service);
        }
        wl.release();
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
