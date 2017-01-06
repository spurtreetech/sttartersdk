package com.sttarter.init;

/**
 * Created by RahulT on 16-06-2015.
 */
public class STTKeys {

    public static String USER_ID = "USER_ID";
    public static String USER_EMAIL = "USER_EMAIL";
    public static String USER_NAME = "USER_NAME";
    public static String USER_TOKEN = "USER_TOKEN";
    public static String APP_KEY = "APP_KEY";
    public static String APP_SECRET = "APP_SECRET";
    public static String APP_AUTHTYPE = "APP_AUTHTYPE";

    public static String USER_PHONE = "user_phone";
    //public static String USER_TOKEN = "user_token";
    public static String IE_TOKEN = "ie_token";
    public static String STTARTER_TOKEN = "sttarter_token";
    public static String STTARTER_APP_KEY = "sttarter_app_key";
    public static String STTARTER_APP_SECRET = "sttarter_secret";

    public static String SERVER_URL = "sttarter.com";  //"10.1.3.174";
    private static String BASE_HOST = "http://"+SERVER_URL;//"http://sttarter.com";
    public static String USERDEFINED_BASE_HOST = "userbaseurl";

    private static String HOST = BASE_HOST+":9000/";  //"http://10.1.3.194:3000/"  //http://10.1.3.33:3000/  // http://sttarter.com/
    public static String BASE_URL = HOST + "app/mqtt/";

    public static String AUTH = HOST + "auth";
    public static String LOGIN = HOST + "login";
    public static String GET_OTP = HOST + "/mobile/getotp";
    public static String OTP_LOGIN = HOST + "/mobile/otplogin";
    public static String QUICK_LOGIN = HOST + "/mobile/quicklogin";
    public static String INIT = HOST + "init";
    public static String SIGNUP = HOST + "app/user";
    public static String GET_TOPICS = BASE_URL + "gettopics";
    public static String ALL_TOPICS = BASE_URL + "alltopics";
    public static String ALL_USERS = HOST + "app/user";
    public static String REGISTER = HOST + "app/client/register";
    public static String UNREGISTER = HOST + "app/client/unregister";
    public static String MY_TOPICS = BASE_URL + "mytopics";
    public static String GROUP = BASE_URL + "group";
    public static String SUB = BASE_URL + "group/join";
    public static String UNSUB = BASE_URL + "group/leave";
    public static String PUBLISH = BASE_URL + "pub";
    public static String INITCHAT = BASE_URL + "initchat";
    public static String INITGROUPCHAT = BASE_URL + "initgroupchat";
    public static String CHAT_UNSUBSCRIBE = BASE_URL + "chat/unsubscribe";
    public static String GROUPCHAT_UNSUBSCRIBE = BASE_URL + "groupchat/unsubscribe";
    public static String STTARTER_PREFERENCES = "com.spurtreetech.sttarter.lib";
    public static String SUBSCRIBED_TOPICS = "SUBSCRIBED_TOPICS";
    public static String ALL_TOPICS_LIST = "ALL_TOPICS_LIST";

    public static String REFERRAL_SIGNUP = HOST + "referral/newsignup";
    public static String GET_REFERRAL_CODE = HOST + "referral/getreferralcode";
    public static String ADD_REFERRAL_TRANSACTION = HOST + "referral/addtransaction";
    public static String CHANGE_REFERRAL_CODE = HOST + "referral/changereferralcode";
    public static String TRACK_REFERRAL_USAGE = HOST + "referral/trackusage";

    public static String COUPON_REDEEM = HOST + "coupons/couponredeem";

    /* chat server url */
    public static String PORT = "1884";
    public static boolean CLEAN_SESSION = false;
    public static String NOTIFICATION_TOPICS = "NOTIFICATION_TOPICS";

    /* expirable token */
    public static String AUTH_TOKEN = "STT_TOKEN";

    /* group name passed to initiate chat */
    public static String CHAT_TOPIC = "CHAT_TOPIC";
    public static String TOPIC_META = "TOPIC_META";

    public static String CLIENT_HANDLE = "CLIENT_HANDLE";

    public static String AUTO_ACCEPT = "AUTO_ACCEPT";

    // DEFAULT STTarterConstants Moved here....

    //public static final String DEFAULT_MOBILE_URL = BASE_HOST + ":3030/mobile/";   /*AUTOSENSE PORT-> :4040/mobile/*/
    public static final String DEFAULT_APP_URL = HOST + "app/";
    public static final String EMAIL = DEFAULT_APP_URL + "email";
    public static final String BUZZ_MESSAGES = DEFAULT_APP_URL + "messages";

    /* response codes */
    public static int USER_NOT_FOUND = 111;
    public static int ORGANISATION_NOT_FOUND = 222;
    public static int PERFECT_RESPONSE = 555;

    public static String BUZZ_TOPIC = "buzz_topic";
    public static String CHECK_DIFF = "check_diff";

    public static String HELP_DESK = BASE_URL + "helpdesk";

}