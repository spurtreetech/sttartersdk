package com.sttarter.common.utils;

/**
 * Created by kevalprabhu on 18/11/16.
 */

public class STTarterConstants {

    public enum AuthType {
        STTARTERACCOUNTAUTH, STTARTEROTPAUTH,STTARTERCUSTOMAUTH
    }

    public static String getAuthTypeStorageString(AuthType authType){
        if (authType == AuthType.STTARTERACCOUNTAUTH){
            return "STTARTERACCOUNTAUTH";
        }else if (authType == AuthType.STTARTEROTPAUTH){
            return "STTARTEROTPAUTH";
        }else if (authType == AuthType.STTARTERCUSTOMAUTH){
            return "STTARTERCUSTOMAUTH";
        }
        return "";
    }
}
