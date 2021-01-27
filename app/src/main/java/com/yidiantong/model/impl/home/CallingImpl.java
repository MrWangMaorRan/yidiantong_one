package com.yidiantong.model.impl.home;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yidiantong.api.ApiConstants;
import com.yidiantong.bean.CluesDetailBean;
import com.yidiantong.bean.ImportOnePhoneBean;
import com.yidiantong.bean.ResponseBean;
import com.yidiantong.bean.ResponseCallAllBean;
import com.yidiantong.httpUtils.CallBackUtil;
import com.yidiantong.httpUtils.HttpUtil;
import com.yidiantong.model.impl.MainImpl;
import com.yidiantong.util.log.LogUtils;
import com.yidiantong.util.StringUtils;

import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class CallingImpl {

    /**
     * 线索详情编辑
     *
     * @param mContext
     * @param object
     * @param onCallBackListener
     */
    public void updateCluesInfo(Context mContext, Object object,
                                OnCallBackListener onCallBackListener) {

//        ApiHttpClientUtil.requestPost(mContext, map, ApiConstants.updateCluesInfo, new ApiCallback() {
//            @Override
//            public void onFailure(ApiRequest request, Exception e) {
//                LogUtils.i("EditCompanyInfoImpl", "EditCompanyInfoImpl onFailure --> " + e.getMessage());
//                onUpdateCluesInfoListener.onUpdateCluesInfoFailure("");
//            }
//
//            @Override
//            public void onResponse(ApiRequest request, ApiResponse response) {
//                LogUtils.i("EditCompanyInfoImpl", "EditCompanyInfoImpl onResponse ---> " + response.toString());
//                if (response.getCode() == 200) {
//                    onUpdateCluesInfoListener.onUpdateCluesInfoSuccess("");
//                } else {
//                    onUpdateCluesInfoListener.onUpdateCluesInfoFailure(response.getMessage());
//                }
//            }
//        });


        HttpUtil.okHttpPostObjJson(mContext, ApiConstants.updateCluesInfo, object, new CallBackUtil.CallBackString(mContext) {
            @Override
            public void onFailure(Call call, Exception e) {
                LogUtils.i("CallingImpl", "CallingImpl updateCluesInfo onFailure --> " + e.getMessage());
                onCallBackListener.onUpdateCluesInfoFailure("");
            }

            @Override
            public void onResponse(String response) {
                try {
                    LogUtils.i("CallingImpl", "CallingImpl updateCluesInfo onResponse ---> " + response.toString());
                    if (!StringUtils.isNullOrBlank(response)) {
                        ResponseBean responseBean = new Gson().fromJson(
                                response, new TypeToken<ResponseBean>() {
                                }.getType());
                        if (responseBean.getCode() == 200) {
                            if (responseBean.isSuccess()) {
                                onCallBackListener.onUpdateCluesInfoSuccess("");
                            } else {
                                onCallBackListener.onUpdateCluesInfoFailure(responseBean.getMessage());
                            }
                        } else {
                            onCallBackListener.onUpdateCluesInfoFailure(responseBean.getMessage());
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
                LogUtils.i("CallingImpl", "CallingImpl updateCallRecords onFailure --> " + e.getMessage());
                onCallBackListener.onUpdateCallRecordsFailure("");
            }

            @Override
            public void onResponse(String response) {
                try {
                    LogUtils.i("CallingImpl", "CallingImpl updateCallRecords onResponse ---> " + response.toString());
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
     * 线索详情
     *
     * @param mContext
     * @param map
     * @param onCallBackListener
     */
    public void cluesInfo(Context mContext, Map<String, String> map, OnCallBackListener onCallBackListener) {

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
                LogUtils.i("CallingImpl", "CallingImpl cluesInfo onFailure --> " + e.getMessage());
                onCallBackListener.onCluesInfoInfoFailure("");
            }

            @Override
            public void onResponse(String response) {
                try {
                    LogUtils.i("CallingImpl", "CallingImpl cluesInfo onResponse ---> " + response.toString());
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
     * 呼叫接口
     *
     * @param mContext
     * @param object
     * @param onCallBackListener
     */
    public void callRequest(Context mContext, Object object,
                            OnCallBackListener onCallBackListener) {

        HttpUtil.okHttpPostObjJson_2(mContext, ApiConstants.callRequest, object, new CallBackUtil.CallBackString(mContext) {
            @Override
            public void onFailure(Call call, Exception e) {
                LogUtils.i("0. ", "CallingImpl callRequest onFailure --> " + e.getMessage());
                onCallBackListener.onCallRequestFailure("");
            }

            @Override
            public void onResponse(String response) {
                try {
                    LogUtils.i("CallingImpl", "CallingImpl callRequest onResponse ---> " + response);
                    if (!StringUtils.isNullOrBlank(response)) {
                        ResponseCallAllBean responseCallAllBean = new Gson().fromJson(
                                response, new TypeToken<ResponseCallAllBean>() {
                                }.getType());
                        if (responseCallAllBean.getCode() == 0) {
                            onCallBackListener.onCallRequestSuccess(responseCallAllBean);
                        } else {
                            onCallBackListener.onCallRequestFailure(responseCallAllBean.getMessage());
                        }
                    } else {
                        onCallBackListener.onCallRequestFailure("");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 拨打状态接口查询
     *
     * @param mContext
     * @param paramsMap
     * @param onCallBackListener
     */
    public void callStatus(Context mContext, Map<String, String> paramsMap,
                           OnCallBackListener onCallBackListener) {

        HttpUtil.okHttpGet_2(mContext, ApiConstants.callStatus, paramsMap, new CallBackUtil.CallBackString(mContext) {
            @Override
            public void onFailure(Call call, Exception e) {
                LogUtils.i("CallingImpl", "CallingImpl callStatus onFailure --> " + e.getMessage());
                onCallBackListener.onCallStatusFailure("");
            }

            @Override
            public void onResponse(String response) {
                try {
                    LogUtils.i("CallingImpl", "CallingImpl callStatus onResponse ---> " + response);
                    if (!StringUtils.isNullOrBlank(response)) {
                        ResponseCallAllBean responseCallAllBean = new Gson().fromJson(
                                response, new TypeToken<ResponseCallAllBean>() {
                                }.getType());
                        if (responseCallAllBean.getCode() == 0) {
                            onCallBackListener.onCallStatusSuccess(responseCallAllBean);
                        } else {
                            onCallBackListener.onCallStatusFailure(responseCallAllBean.getMessage());
                        }
                    } else {
                        onCallBackListener.onCallStatusFailure("");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 添加单个手机号
     *
     * @param mContext
     * @param object
     * @param onCallBackListener
     */
    public void importOnePhone(Context mContext, Object object, OnCallBackListener onCallBackListener) {
        HttpUtil.okHttpPostObjJson_2(mContext, ApiConstants.importOnePhone, object, new CallBackUtil.CallBackString(mContext) {
            @Override
            public void onFailure(Call call, Exception e) {
                LogUtils.i("CallingImpl", "CallingImpl importOnePhone onFailure --> " + e.getMessage());
                onCallBackListener.onImportOneContactsFailure("");
            }

            @Override
            public void onResponse(String response) {
                try {
                    LogUtils.i("CallingImpl", "CallingImpl importOnePhone onResponse ---> " + response.toString());
                    if (!StringUtils.isNullOrBlank(response)) {
                        ResponseCallAllBean responseCallAllBean = new Gson().fromJson(
                                response, new TypeToken<ResponseCallAllBean>() {
                                }.getType());
                        if (responseCallAllBean.getCode() == 0) {
                            String jsonStr = new Gson().toJson(responseCallAllBean.getData());
                            if (!StringUtils.isNullOrBlank(jsonStr)) {
                                // 可以导入的电话号码
                                ImportOnePhoneBean importOnePhoneBean = new Gson().fromJson(
                                        jsonStr, new TypeToken<ImportOnePhoneBean>() {
                                        }.getType());
                                onCallBackListener.onImportOneContactsSuccess(importOnePhoneBean);
                            } else {
                                onCallBackListener.onImportOneContactsFailure(responseCallAllBean.getMessage());
                            }


                        } else {
                            onCallBackListener.onImportOneContactsFailure(responseCallAllBean.getMessage());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 双呼拨打提交通话时长
     *
     * @param mContext
     * @param object
     * @param onCallBackListener
     */
    public void duration(Context mContext, Object object, OnCallBackListener onCallBackListener) {
        HttpUtil.okHttpPostObjJson_2(mContext, ApiConstants.duration, object, new CallBackUtil.CallBackString(mContext) {
            @Override
            public void onFailure(Call call, Exception e) {
                LogUtils.i("CallingImpl", "CallingImpl duration onFailure --> " + e.getMessage());
                onCallBackListener.onDurationFailure("");
            }

            @Override
            public void onResponse(String response) {
                try {
                    LogUtils.i("CallingImpl", "CallingImpl duration onResponse ---> " + response.toString());
                    if (!StringUtils.isNullOrBlank(response)) {
                        ResponseCallAllBean responseCallAllBean = new Gson().fromJson(
                                response, new TypeToken<ResponseCallAllBean>() {
                                }.getType());
                        if (responseCallAllBean.getCode() == 0) {
                            onCallBackListener.onDurationSuccess();
                        } else {
                            onCallBackListener.onDurationFailure(responseCallAllBean.getMessage());
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
        void onUpdateCluesInfoSuccess(String msg);

        // 失败
        void onUpdateCluesInfoFailure(String msg);

        // 成功
        void onUpdateCallRecordsSuccess();

        // 失败
        void onUpdateCallRecordsFailure(String msg);

        // 成功
        void onCallRequestSuccess(ResponseCallAllBean responseCallAllBean);

        // 失败
        void onCallRequestFailure(String msg);

        // 成功
        void onCallStatusSuccess(ResponseCallAllBean responseCallAllBean);

        // 失败
        void onCallStatusFailure(String msg);

        // 成功
        void onImportOneContactsSuccess(ImportOnePhoneBean importOnePhoneBean);

        // 失败
        void onImportOneContactsFailure(String msg);

        // 成功
        void onDurationSuccess();

        // 失败
        void onDurationFailure(String msg);
    }

}
