package com.sia.qtsimageuploader;

import java.time.Instant;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class LocalEventLogging {
    public enum CustomEventType{
        Info, Error, Transaction, BarcodeScan, Account
    }
    public static class CustomEvent{
        public Date TimeStamp;
        public String Msg;
        public Exception Ex;
        public CustomEventType Type;
        public CustomEvent(String msg, CustomEventType type, Exception ex){
            Msg = msg;
            Type = type;
            Ex = ex;
            TimeStamp = Date.from(Instant.now());
        }
    }

    public static List<CustomEvent> EventList = new LinkedList<CustomEvent>();

    public static void LogEvent(String msg, CustomEventType type, Exception ex){
        if(EventList.size() > 1000){
            EventList.clear();
        }
        EventList.add(0, new CustomEvent(msg, type, ex));
    }
}
