package com.yidiantong.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;

import com.yidiantong.base.Constants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Utils {

    /**
     * 得到去除重复后的集合
     *
     * @param list
     * @return
     */
    public static List<String> getRemoveList(List<String> list) {
        Set set = new HashSet();
        List<String> newList = new ArrayList<>();
        for (Iterator iter = list.iterator(); iter.hasNext(); ) {
            String object = (String) iter.next();
            if (object != null && !object.equals("")
                    && !object.isEmpty()) {
                if (set.add(object)) {
                    newList.add(object);
                }
            }
        }
        return newList;
    }

    /**
     * 是否为LinPhone呼叫
     */
    public static boolean isAXB(Context mContext) {
        String callType = SharedPreferencesUtil.getSharedPreferences(mContext).getString("callType", "");
        // 非双呼才调用
        if (!StringUtils.isNullOrBlank(callType) && callType.equals("AXB")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取权限
     */
    public static void getPermission(Context mContext) {
        String[] permission = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,

                Manifest.permission.WAKE_LOCK,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.MODIFY_AUDIO_SETTINGS,

                Manifest.permission.KILL_BACKGROUND_PROCESSES,
                Manifest.permission.SET_WALLPAPER,
                // 打印日志用到
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,

                Manifest.permission.CALL_PHONE,
                Manifest.permission.VIBRATE,
                Manifest.permission.FOREGROUND_SERVICE,
                //通话记录
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.WRITE_CALL_LOG,


        };

        if (!PermissinsUtils.hasPermissions((Activity) mContext, permission)) {
            PermissinsUtils.requestPermissions((Activity) mContext, permission, 0xaa);
        }
    }


}
