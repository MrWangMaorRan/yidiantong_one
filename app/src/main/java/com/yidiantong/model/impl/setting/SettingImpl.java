package com.yidiantong.model.impl.setting;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yidiantong.api.ApiConstants;
import com.yidiantong.bean.CheckVersionBean;
import com.yidiantong.bean.ResponseBean;
import com.yidiantong.httpUtils.CallBackUtil;
import com.yidiantong.httpUtils.HttpUtil;
import com.yidiantong.util.log.LogUtils;
import com.yidiantong.util.StringUtils;

import java.util.HashMap;

import okhttp3.Call;

public class SettingImpl {

    /**
     * 退出登录
     *
     * @param onCallBackListener
     */
    public void loginOut(Context mContext, OnCallBackListener onCallBackListener) {
//        ApiHttpClientUtil.requestPost(mContext, new HashMap<>(), ApiConstants.logout, new ApiCallback() {
//            @Override
//            public void onFailure(ApiRequest request, Exception e) {
//                Log.i("SettingImpl", "SettingImpl onFailure --> " + e.getMessage());
//                onLoginOutListener.onLoginOutFailure("");
//            }
//
//            @Override
//            public void onResponse(ApiRequest request, ApiResponse response) {
//                Log.i("SettingImpl", "SettingImpl onResponse ---> " + response.toString());
//                if (response.getCode() == 200) {
//                    onLoginOutListener.onLoginOutSuccess(response.getMessage());
//                } else {
//                    onLoginOutListener.onLoginOutFailure(response.getMessage());
//                }
//            }
//        });

        HttpUtil.okHttpDelete(mContext, ApiConstants.logout, new HashMap<>(), new CallBackUtil.CallBackString(mContext) {
            @Override
            public void onFailure(Call call, Exception e) {
                LogUtils.i("SettingImpl", "SettingImpl loginOut onFailure --> " + e.getMessage());
                onCallBackListener.onLoginOutFailure("");
            }

            @Override
            public void onResponse(String response) {
                try {
                    LogUtils.i("SettingImpl", "SettingImpl loginOut onResponse ---> " + response.toString());
                    if (!StringUtils.isNullOrBlank(response)) {
                        ResponseBean responseBean = new Gson().fromJson(
                                response, new TypeToken<ResponseBean>() {
                                }.getType());
                        if (responseBean.getCode() == 200) {
                            onCallBackListener.onLoginOutSuccess(responseBean.getMessage());
                        } else {
                            onCallBackListener.onLoginOutFailure(responseBean.getMessage());
                        }
                    }
                    onCallBackListener.onLoginOutFailure("");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

    public interface OnLoginOutListener {
    }


    /**
     * 检查更新
     *
     * @param onCallBackListener
     */
    public void checkVersion(Context mContext, OnCallBackListener onCallBackListener) {
//        ApiHttpClientUtil.requestPost(mContext, new HashMap<>(), ApiConstants.checkVersion, new ApiCallback() {
//            @Override
//            public void onFailure(ApiRequest request, Exception e) {
//                Log.i("SettingImpl", "SettingImpl onFailure --> " + e.getMessage());
//                onCheckVersionListener.onCheckVersionFailure("");
//            }
//
//            @Override
//            public void onResponse(ApiRequest request, ApiResponse response) {
//                Log.i("SettingImpl", "SettingImpl onResponse ---> " + response.toString());
//                if (response.getCode() == 200) {
//                    String jsonStr = response.getMessage();
//                    if (!StringUtils.isNullOrBlank(jsonStr)) {
//                        CheckVersionBean checkVersionBean = new Gson().fromJson(
//                                jsonStr, new TypeToken<CheckVersionBean>() {
//                                }.getType());
//                        onCheckVersionListener.onCheckVersionSuccess(checkVersionBean);
//                    }
//                } else {
//                    onCheckVersionListener.onCheckVersionFailure(response.getMessage());
//                }
//            }
//        });

        HttpUtil.okHttpGet(mContext, ApiConstants.checkVersion, new HashMap<>(), new CallBackUtil.CallBackString(mContext) {
            @Override
            public void onFailure(Call call, Exception e) {
                LogUtils.i("SettingImpl", "SettingImpl checkVersion onFailure --> " + e.getMessage());
                onCallBackListener.onCheckVersionFailure("");
            }

            @Override
            public void onResponse(String response) {
                try {
                    LogUtils.i("SettingImpl", "SettingImpl checkVersion onResponse ---> " + response.toString());
                    if (!StringUtils.isNullOrBlank(response)) {
                        ResponseBean responseBean = new Gson().fromJson(
                                response, new TypeToken<ResponseBean>() {
                                }.getType());
                        if (responseBean.getCode() == 200) {
                            String jsonStr = new Gson().toJson(responseBean.getData());
                            if (!StringUtils.isNullOrBlank(jsonStr)) {
                                CheckVersionBean checkVersionBean = new Gson().fromJson(
                                        jsonStr, new TypeToken<CheckVersionBean>() {
                                        }.getType());
                                onCallBackListener.onCheckVersionSuccess(checkVersionBean);
                            }
                        } else {
                            onCallBackListener.onCheckVersionFailure(responseBean.getMessage());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }


    public interface OnCallBackListener {

        // 成功
        void onLoginOutSuccess(String msg);

        // 失败
        void onLoginOutFailure(String msg);


        // 成功
        void onCheckVersionSuccess(CheckVersionBean checkVersionBean);

        // 失败
        void onCheckVersionFailure(String msg);
    }
}
