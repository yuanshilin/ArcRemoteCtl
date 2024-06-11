package com.arcvideo.remotectl.linkmodule;

public class ArcEvent {
    // 系统功能标志 1000~1999
    public static final int TO_SLEEP = 1000;
    public static final int WAKE_UP = 1001;
    public static final int START_PUSH_FLOW = 1002;
    public static final int STOP_PUSH_FLOW = 1003;

    public static String getEventMassage(int event){
        switch (event) {
            case TO_SLEEP:
                return "TO_SLEEP";
            case WAKE_UP:
                return "WAKE_UP";
            case START_PUSH_FLOW:
                return "START_PUSH_FLOW";
            case STOP_PUSH_FLOW:
                return "STOP_PUSH_FLOW";
            default:
                return null;
        }
    }
}
