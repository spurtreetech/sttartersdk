package com.spurtreetech.sttarter.lib.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by RahulT on 24-07-2015.
 */
public class SystemMessageReceiver extends BroadcastReceiver {

    ISTTSystemEvent sysEventListener;
    public SystemMessageReceiver(ISTTSystemEvent sysEventListener) {
        this.sysEventListener = sysEventListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent!=null) {
            sysEventListener.systemMessageReceived(
                    intent.getStringExtra("message"),
                    SysMessage.valueOf(intent.getStringExtra("type")),
                    intent.getStringExtra("topic"));
        }
    }
}
