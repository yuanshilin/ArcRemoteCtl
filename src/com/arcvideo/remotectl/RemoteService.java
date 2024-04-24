package com.arcvideo.remotectl;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.arcvideo.remotectl.linkmodule.ArcEventManager;

public class RemoteService extends Service {
    private final String TAG = "ArcRemoteCtl";

    @Override
    public void onCreate() {
        Log.d(TAG, "create ArcRemote Controller.");
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d(TAG, "kepp ArcRemote Controller running on the background.");
            NotificationChannel channel = new NotificationChannel("ArcRemoteCtl", "ArcRemote", NotificationManager.IMPORTANCE_NONE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, "ArcRemoteCtl")
                    .setContentTitle("ArcService")
                    .setContentText("ArcRemote is running in the background")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .build();

            startForeground(1, notification);
        }
        ArcEventManager.init(getApplicationContext());
        ArcEventManager.createSocketServer();
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ArcEventManager.closeSocketServer();
    }
}
