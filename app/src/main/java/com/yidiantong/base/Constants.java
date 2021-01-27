package com.yidiantong.base;

import com.yidiantong.app.MainApplication;

import java.io.File;

public class Constants {

    // 通话时长不足50分钟拨打运维电话
    public final static String phoneNum = "13729550670";

    // 全局判断sip是否登陆成功
    public static boolean sipIsLogin = false;
    public static boolean sipIsInit = false;

    /**
     * 广播
     */
    public final static String LOGIN_SUCCESS = "com.yidiantong.call.login.success";
    public final static String LOGIN_FAILURE = "com.yidiantong.call.login.failure";
    public final static String TOKEN_TIMEOUT = "com.okhttp.token.timeout";

    public final static String CALL_OUTGOING = "com.yidiantong.call.outgoing";
    public final static String CALL_CONNECT = "com.yidiantong.call.connect";
    public final static String CALL_EARLY_MEDIA = "com.yidiantong.call.early.media";
    public final static String CALL_END = "com.yidiantong.call.end";
    public final static String CALL_ERROR = "com.yidiantong.call.err";
    public final static String CALL_HANGUP = "com.yidiantong.call.hangup";


    public static final String DOWN_PATH = "/storage/emulated/0/yidiantong.apk";
}

