package com.arcvideo.remotectl.linkmodule;

public class ArcEvent {
    // 系统功能标志 1000~1999
    public static final int TO_SLEEP = 1000;
    public static final int WAKE_UP = 1001;

    public static String getEventMassage(int event){
        switch (event) {
            case TO_SLEEP:
                return "TO_SLEEP";
            case WAKE_UP:
                return "WAKE_UP";
            default:
                return null;
        }
    }
}
