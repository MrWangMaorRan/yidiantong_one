package com.yidiantong.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import androidx.multidex.MultiDex;

import com.yidiantong.MainActivity;
import com.yidiantong.api.ApiHttpsApiClient;
import com.yidiantong.api.ApiWebSocketApiClient;
import com.yidiantong.util.log.CrashHandler;
import com.yidiantong.util.log.LogVariateUtils;


public class MainApplication extends Application {
    public static Context con;

    @Override
    public void onCreate() {
        super.onCreate();
        con = this;
        MultiDex.install(this);

        // alibabahttp.sdk 初始化
        ApiHttpsApiClient.init();
//        ApiWebSocketApiClient.init();

        // 错误日志收集
        CrashHandler.getInstance().init(this);
        LogVariateUtils.getInstance().isShowLog(true);
        LogVariateUtils.getInstance().isWriteLog(true);

        // 拨号linPhone初始化
//        MyLinPhoneManager.getInstance().init(this);

    }

}
