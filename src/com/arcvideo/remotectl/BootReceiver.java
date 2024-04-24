package com.arcvideo.remotectl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
    private final String TAG = "ArcRemoteCtl";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "ready to create ArcRemote Controller for receiveing ArcEvent.");
        context.startService(new Intent(context, RemoteService.class));
    }
}
