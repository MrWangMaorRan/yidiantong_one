package com.yidiantong.model.impl.home;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yidiantong.api.ApiConstants;
import com.yidiantong.bean.CluesDetailBean;
import com.yidiantong.bean.ResponseBean;
import com.yidiantong.bean.TalkTimeInfoBean;
import com.yidiantong.httpUtils.CallBackUtil;
import com.yidiantong.httpUtils.HttpUtil;
import com.yidiantong.model.impl.MainImpl;
import com.yidiantong.util.log.LogUtils;
import com.yidiantong.util.StringUtils;

import java.util.Map;

import okhttp3.Call;

public class CluesDetailImpl {

    /**
     * 线索详情
     *
     * @param mContext
     * @param map
     * @param onCallBackListener
     */
    public void cluesInfo(Context mContext, Map<String, String> map,
                          OnCallBackListener onCallBackListener) {

//        ApiHttpClientUtil.requestPost(mContext, map, ApiConstants.updateCluesInfo, new ApiCallback() {
//            @Override
//            public void onFailure(ApiRequest request, Exception e) {
//                Log.i("EditCompanyInfoImpl", "EditCompanyInfoImpl onFailure --> " + e.getMessage());
//                onUpdateCluesInfoListener.onUpdateCluesInfoFailure("");
//            }
//
//            @Override
//            public void onResponse(ApiRequest request, ApiResponse response) {
//                Log.i("EditCompanyInfoImpl", "EditCompanyInfoImpl onResponse ---> " + response.toString());
//                if (response.getCode() == 200) {
//                    onUpdateCluesInfoListener.onUpdateCluesInfoSuccess("");
//                } else {
//                    onUpdateCluesInfoListener.onUpdateCluesInfoFailure(response.getMessage());
//                }
//            }
//        });


        HttpUtil.okHttpPostJson(mContext, ApiConstants.cluesInfo, map, new CallBackUtil.CallBackString(mContext) {
            @Override
            public void onFailure(Call call, Exception e) {
                LogUtils.i("CluesDetailImpl", "CluesDetailImpl cluesInfo onFailure --> " + e.getMessage());
                onCallBackListener.onCluesInfoInfoFailure("");
            }

            @Override
            public void onResponse(String response) {
                try {
                    LogUtils.i("CluesDetailImpl", "CluesDetailImpl cluesInfo onResponse ---> " + response.toString());
                    if (!StringUtils.isNullOrBlank(response)) {
                        ResponseBean responseBean = new Gson().fromJson(
                                response, new TypeToken<ResponseBean>() {
                                }.getType());
                        if (responseBean.getCode() == 200) {
                            String jsonStr = new Gson().toJson(responseBean.getData());
                            if (!StringUtils.isNullOrBlank(jsonStr)) {
                                CluesDetailBean cluesDetailBean = new Gson().fromJson(
                                        jsonStr, new TypeToken<CluesDetailBean>() {
                                        }.getType());
                                onCallBackListener.onCluesInfoSuccess(cluesDetailBean);
                            }
                        } else {
                            onCallBackListener.onCluesInfoInfoFailure(responseBean.getMessage());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 更新通话记录
     *
     * @param mContext
     * @param object
     * @param onCallBackListener
     */
    public void updateCallRecords(Context mContext, Object object,
                                  OnCallBackListener onCallBackListener) {

        HttpUtil.okHttpPostObjJson(mContext, ApiConstants.updateCallRecords, object, new CallBackUtil.CallBackString(mContext) {
            @Override
            public void onFailure(Call call, Exception e) {
                LogUtils.i("CluesDetailImpl", "CluesDetailImpl updateCallRecords onFailure --> " + e.getMessage());
                onCallBackListener.onUpdateCallRecordsFailure("");
            }

            @Override
            public void onResponse(String response) {
                try {
                    LogUtils.i("CluesDetailImpl", "CluesDetailImpl updateCallRecords onResponse ---> " + response.toString());
                    if (!StringUtils.isNullOrBlank(response)) {
                        ResponseBean responseBean = new Gson().fromJson(
                                response, new TypeToken<ResponseBean>() {
                                }.getType());
                        if (responseBean.getCode() == 200) {
                            onCallBackListener.onUpdateCallRecordsSuccess();
                        } else {
                            onCallBackListener.onUpdateCallRecordsFailure(responseBean.getMessage());
                        }
                    } else {
                        onCallBackListener.onUpdateCallRecordsFailure("");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 账号剩余通话时长
     *
     * @param mContext
     * @param map
     * @param onCallBackListener
     */
    public void talkTimeInfo(Context mContext, Map<String, String> map, OnCallBackListener onCallBackListener) {

//        ApiHttpClientUtil.requestPost(mContext, map, ApiConstants.talktimeInfo, new ApiCallback() {
//            @Override
//            public void onFailure(ApiRequest request, Exception e) {
//                LogUtils.i("MainImpl", "MainImpl onFailure --> " + e.getMessage());
//                onGetTalkTimeInfoListener.onGetTalkTimeInfoFailure("");
//            }
//
//            @Override
//            public void onResponse(ApiRequest request, ApiResponse response) {
//                LogUtils.i("MainImpl", "MainImpl onResponse ---> " + response.toString());
//                if (response.getCode() == 200) {
//                    String jsonStr = response.getMessage();
//                    if (!StringUtils.isNullOrBlank(jsonStr)) {
//                        TalkTimeInfoBean talkTimeInfoBean = new Gson().fromJson(
//                                jsonStr, new TypeToken<TalkTimeInfoBean>() {
//                                }.getType());
//                        onGetTalkTimeInfoListener.onGetTalkTimeInfoSuccess(talkTimeInfoBean);
//                    }
//                } else {
//                    onGetTalkTimeInfoListener.onGetTalkTimeInfoFailure(response.getMessage());
//                }
//            }
//        });

        HttpUtil.okHttpGet(mContext, ApiConstants.talktimeInfo, map, new CallBackUtil.CallBackString(mContext) {
            @Override
            public void onFailure(Call call, Exception e) {
                LogUtils.i("CluesDetailImpl", "CluesDetailImpl talkTimeInfo onFailure --> " + e.getMessage());
                onCallBackListener.onGetTalkTimeInfoFailure("");
            }

            @Override
            public void onResponse(String response) {
                try {
                    LogUtils.i("CluesDetailImpl", "CluesDetailImpl talkTimeInfo onResponse ---> " + response.toString());
                    if (!StringUtils.isNullOrBlank(response)) {
                        ResponseBean responseBean = new Gson().fromJson(
                                response, new TypeToken<ResponseBean>() {
                                }.getType());
                        if (responseBean.getCode() == 200) {
                            String jsonStr = new Gson().toJson(responseBean.getData());
                            if (!StringUtils.isNullOrBlank(jsonStr)) {
                                TalkTimeInfoBean talkTimeInfoBean = new Gson().fromJson(
                                        jsonStr, new TypeToken<TalkTimeInfoBean>() {
                                        }.getType());
                                onCallBackListener.onGetTalkTimeInfoSuccess(talkTimeInfoBean);
                            }
                        } else {
                            onCallBackListener.onGetTalkTimeInfoFailure(responseBean.getMessage());
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
        void onCluesInfoSuccess(CluesDetailBean cluesDetailBean);
        // 失败
        void onCluesInfoInfoFailure(String msg);

        // 成功
        void onUpdateCallRecordsSuccess();
        // 失败
        void onUpdateCallRecordsFailure(String msg);

        // 成功
        void onGetTalkTimeInfoSuccess(TalkTimeInfoBean talkTimeInfoBean);
        // 失败
        void onGetTalkTimeInfoFailure(String msg);
    }


}
