package com.yidiantong.util;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络判断工具类
 * Created by Administrator on 2018/9/12.
 */

public class NetWorkUtils {
    /**
     * 没有连接网络
     */
    public static final int NETWORK_NONE = -1;
    /**
     * 移动网络
     */
    public static final int NETWORK_MOBILE = 0;
    /**
     * 无线网络
     */
    public static final int NETWORK_WIFI = 1;

    /**
     * 网络状态
     *
     * @param context
     * @return
     */
    public static int getNetWorkState(Context context) {

        // 得到连接管理器对象
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_WIFI)) {
                return NETWORK_WIFI;
            } else if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_MOBILE)) {
                return NETWORK_MOBILE;
            }
        } else {
            return NETWORK_NONE;
        }
        return NETWORK_NONE;
    }

    /**
     * 检查网络
     *
     * @param context
     * @return
     */
    public static boolean checkNetwork(Context context) {
        int netWorkState = getNetWorkState(context);
        switch (netWorkState) {
            case -1:
            default:
                return false;
            case 0:
            case 1:
                return true;
        }
    }

    /**
     * 无网络显示
     */
    public static void setNetWorkShow(final Context context, NetWorkCallBack netWorkCallBack) {
        // 有网络
        if (checkNetwork(context)) {
            // 有网络回调
            if (netWorkCallBack != null)
                netWorkCallBack.netWorkCallBack();
        } else {
            // 无网络
            Intent intent = new Intent("no_net_work");
            context.sendBroadcast(intent);
            ToastUtils.showToast(context, "当前网络异常");
        }
    }

    /**
     * 网络监听接口
     */
    public interface NetWorkCallBack {
        void netWorkCallBack();
    }
}
