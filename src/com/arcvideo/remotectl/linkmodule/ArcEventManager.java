package com.arcvideo.remotectl.linkmodule;

import android.content.Context;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;

import com.arcvideo.remotectl.linkmodule.camerastreammodule.CameraStreamService;
import com.arcvideo.remotectl.util.PropUtil;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ArcEventManager {
    private static final String TAG = "ArcEventManager";
    public static int ServerPort;
    public static PowerManager powerManager = null;
    private static PowerManager.WakeLock wakeLock = null;
    public static ServerSocket serverSocket = null;
    private static DataInputStream dis = null;
    private static Socket csocket = null;
    private static CameraStreamService cameraStreamService = null;

    public static void init(Context context){
        // 初始化屏幕 息屏/唤醒 模块
        if (powerManager == null) {
            powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        }
        if (wakeLock == null) {
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, context.getClass().getName());
        }
        wakeLock.acquire();

        // 初始化摄像头推流服务模块
	if (cameraStreamService == null){
            cameraStreamService = new CameraStreamService(context);
            cameraStreamService.initService();
        }

        Log.d(TAG, "init: ArcEventManager init success.");
    }

    public static void createSocketServer(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String clientStr = "";
                ServerPort = PropUtil.getIntFromProp(ArcHostInfo.CreatePort_Prop, ArcHostInfo.HostPort);
                try {
                    serverSocket = new ServerSocket(ServerPort);
                    Log.d(TAG, "create socket server for port："+ServerPort);
                    while(true){
                        csocket = serverSocket.accept();
                        dis = new DataInputStream(csocket.getInputStream());
                        clientStr = dis.readUTF();
                        String regex = "-?\\d+(\\.\\d+)?";
                        if (clientStr.matches(regex)){
                            acesskeyvalue(Integer.parseInt(clientStr));
                        }else {
                            Log.d(TAG, "the ArcEvent is not valide："+ clientStr);
                        }
                    }
                } catch (IOException e) {
                }
            }
        }).start();
    }

    public static void closeSocketServer(){
        try {
            if (dis != null) {
                dis.close();
            }
            if (csocket != null) {
                csocket.close();
            }
            if (serverSocket != null) {
                serverSocket.close();
            }
            wakeLock.release();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static void acesskeyvalue(int key){
        switch (key){
            case ArcEvent.TO_SLEEP:
                Log.d(TAG, "ArcEvent is TO_SLEEP");
                powerManager.goToSleep(SystemClock.uptimeMillis());
                break;
            case ArcEvent.WAKE_UP:
                Log.d(TAG, "ArcEvent is WAKE_UP");
                powerManager.wakeUp(SystemClock.uptimeMillis());
                break;
            case ArcEvent.START_PUSH_FLOW:
                Log.d(TAG, "ArcEvent is START_PUSH_FLOW");
                cameraStreamService.startAll();
                break;
            case ArcEvent.STOP_PUSH_FLOW:
                Log.d(TAG, "ArcEvent is STOP_PUSH_FLOW");
                cameraStreamService.stopAll();
                break;
            default:
        }
    }
}
