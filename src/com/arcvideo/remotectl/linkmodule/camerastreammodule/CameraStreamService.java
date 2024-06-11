package com.arcvideo.remotectl.linkmodule.camerastreammodule;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.arcvideo.ivi.carmera2streaming.ICameraStreamService;

public class CameraStreamService {
    private static final String TAG = "ArcVideo_CameraStreamService";
    private Context mContext;
    private ICameraStreamService ICameraBinder = null;
    private ICameraStreamService aLiveBinder = null;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(TAG, "onServiceConnected: binder connect success.");
            ICameraBinder = ICameraStreamService.Stub.asInterface(iBinder);
            aLiveBinder = ICameraStreamService.Stub.asInterface(iBinder);
            try {
                aLiveBinder.asBinder().linkToDeath(mDeathRecipient,0);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(TAG, "onServiceDisconnected: binder disconnect.");
        }
    };

    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (aLiveBinder == null){
                return;
            }
            aLiveBinder.asBinder().unlinkToDeath(mDeathRecipient, 0);
        }
    };

    public CameraStreamService(Context mContext) {
        this.mContext = mContext;
    }

    public void initService(){
        Intent intent = new Intent();
        intent.setPackage("com.arcvideo.ivi.carmera2streaming");
        intent.setAction("com.arcvideo.ivi.carmera2streaming.ArcVideoService");
        if (mContext.bindService(intent, mConnection, Context.BIND_AUTO_CREATE)) {
            Log.d(TAG, "initService: bindservice success.");
        }else{
            Log.d(TAG, "initService: bindservice failed.");
        }
    }

    public void startAll() {
        try {
            ICameraBinder.startAll();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
    public void stopAll() {
        try {
            ICameraBinder.stopAll();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
