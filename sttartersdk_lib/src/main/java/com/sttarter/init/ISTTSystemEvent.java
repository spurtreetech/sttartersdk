package com.sttarter.init;

/**
 * Created by RahulT on 24-07-2015.
 */
// this interface has not been used anywhere and can be deleted
public interface ISTTSystemEvent {

    void systemMessageReceived(String message, SysMessage eventType, String topic);
}
