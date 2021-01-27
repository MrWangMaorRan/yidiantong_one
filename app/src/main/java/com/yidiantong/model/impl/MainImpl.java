package com.yidiantong.model.impl;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yidiantong.api.ApiConstants;
import com.yidiantong.bean.CluesBean;
import com.yidiantong.bean.FilterListBean;
import com.yidiantong.bean.IndustryBean;
import com.yidiantong.bean.RegionBean;
import com.yidiantong.bean.ResponseBean;
import com.yidiantong.bean.TalkTimeInfoBean;
import com.yidiantong.bean.UserInfoBean;
import com.yidiantong.httpUtils.CallBackUtil;
import com.yidiantong.httpUtils.HttpUtil;
import com.yidiantong.util.log.LogUtils;
import com.yidiantong.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class MainImpl {

    /**
     * 线索列表
     *
     * @param mContext
     * @param object
     * @param onCallBackListener
     */
    public void getCluesList(Context mContext, Object object,
                             OnCallBackListener onCallBackListener) {
//        ApiHttpClientUtil.requestPost(mContext, map, ApiConstants.cluesList, new ApiCallback() {
//            @Override
//            public void onFailure(ApiRequest request, Exception e) {
//                LogUtils.i("MainImpl", "MainImpl onFailure --> " + e.getMessage());
//                onGetCluesListListener.onGetCluesListFailure("");
//            }
//
//            @Override
//            public void onResponse(ApiRequest request, ApiResponse response) {
//                LogUtils.i("MainImpl", "MainImpl onResponse ---> " + response.toString());
//                if (response.getCode() == 200) {
//                    String jsonStr = response.getMessage();
//                    if (!StringUtils.isNullOrBlank(jsonStr)) {
////                        UserInfoBean userInfoBean = new Gson().fromJson(
////                                jsonStr, new TypeToken<UserInfoBean>() {
////                                }.getType());
////                        onGetCluesListListener.onGetCluesListSuccess(userInfoBean);
//                    }
//                    onGetCluesListListener.onGetCluesListSuccess(new UserInfoBean());
//                } else {
//                    onGetCluesListListener.onGetCluesListFailure(response.getMessage());
//                }
//            }
//        });

        HttpUtil.okHttpPostObjJson(mContext, ApiConstants.cluesList, object, new CallBackUtil.CallBackString(mContext) {
            @Override
            public void onFailure(Call call, Exception e) {
                LogUtils.i("MainImpl", "MainImpl cluesList onFailure --> " + e.getMessage());
                onCallBackListener.onGetCluesListFailure("");
            }

            @Override
            public void onResponse(String response) {
                try {
                    LogUtils.i("MainImpl", "MainImpl cluesList onResponse ---> " + response);
                    if (!StringUtils.isNullOrBlank(response)) {
                        ResponseBean responseBean = new Gson().fromJson(
                                response, new TypeToken<ResponseBean>() {
                                }.getType());
                        if (responseBean.getCode() == 200) {
                            // {"cluesList":[{"number":"21234567890","numberLocation":"广东深圳",
                            // "id":"91a9118e-0874-4a51-b3ba-f7f98f6bd1c7","callTime":"2020-06-25","status":"待分配"}]}
                            String jsonStr = new Gson().toJson(responseBean.getData());
                            if (!StringUtils.isNullOrBlank(jsonStr)) {
                                CluesBean cluesBean = new Gson().fromJson(
                                        jsonStr, new TypeToken<CluesBean>() {
                                        }.getType());
                                onCallBackListener.onGetCluesListSuccess(cluesBean);
                            } else {
                                onCallBackListener.onGetCluesListSuccess(new CluesBean());
                            }
                        } else {
                            onCallBackListener.onGetCluesListFailure(responseBean.getMessage());
                        }
                    } else {
                        onCallBackListener.onGetCluesListFailure("");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 获取产品列表接口
     *
     * @param onCallBackListener
     */
    public void getIndustryList(Context mContext, OnCallBackListener onCallBackListener) {
//        ApiHttpClientUtil.requestPost(mContext, new HashMap<>(), ApiConstants.getIndustrylist, new ApiCallback() {
//            @Override
//            public void onFailure(ApiRequest request, Exception e) {
//                LogUtils.i("MainImpl", "MainImpl onFailure --> " + e.getMessage());
//                onIndustryListListener.onIndustryListFailure("");
//            }
//
//            @Override
//            public void onResponse(ApiRequest request, ApiResponse response) {
//                LogUtils.i("MainImpl", "MainImpl onResponse ---> " + response.toString());
//                if (response.getCode() == 200) {
//                    String jsonStr = response.getMessage();
//                    if (!StringUtils.isNullOrBlank(jsonStr)) {
//                        List<String> list = new Gson().fromJson(
//                                jsonStr, new TypeToken<List<String>>() {
//                                }.getType());
//                        onIndustryListListener.onIndustryListSuccess(list);
//                    }
//                } else {
//                    onIndustryListListener.onIndustryListFailure(response.getMessage());
//                }
//            }
//        });


        HttpUtil.okHttpGet(mContext, ApiConstants.getIndustrylist, new HashMap<>(), new CallBackUtil.CallBackString(mContext) {
            @Override
            public void onFailure(Call call, Exception e) {
                LogUtils.i("MainImpl", "MainImpl getIndustrylist onFailure --> " + e.getMessage());
                onCallBackListener.onIndustryListFailure("");
            }

            @Override
            public void onResponse(String response) {
                LogUtils.i("MainImpl", "MainImpl getIndustrylist onResponse ---> " + response.toString());
                try {
                    if (!StringUtils.isNullOrBlank(response)) {
                        ResponseBean responseBean = new Gson().fromJson(
                                response, new TypeToken<ResponseBean>() {
                                }.getType());
                        if (responseBean.getCode() == 200) {
                            String jsonStr = new Gson().toJson(responseBean.getData());
                            if (!StringUtils.isNullOrBlank(jsonStr)) {
                                // {"industryList":["A产品","B产品"]}
                                IndustryBean industryBean = new Gson().fromJson(
                                        jsonStr, new TypeToken<IndustryBean>() {
                                        }.getType());
                                onCallBackListener.onIndustryListSuccess(industryBean);
                            }
                        } else {
                            onCallBackListener.onIndustryListFailure(responseBean.getMessage());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }


    /**
     * 获取地区列表
     *
     * @param onCallBackListener
     */
    public void getRegionList(Context mContext, Map<String, String> map, OnCallBackListener onCallBackListener) {
//        ApiHttpClientUtil.requestPost(mContext, map, ApiConstants.getRegionList, new ApiCallback() {
//            @Override
//            public void onFailure(ApiRequest request, Exception e) {
//                LogUtils.i("MainImpl", "MainImpl onFailure --> " + e.getMessage());
//                onRegionListListener.onRegionListFailure("");
//            }
//
//            @Override
//            public void onResponse(ApiRequest request, ApiResponse response) {
//                LogUtils.i("MainImpl", "MainImpl onResponse ---> " + response.toString());
//                if (response.getCode() == 200) {
//                    String jsonStr = response.getMessage();
//                    if (!StringUtils.isNullOrBlank(jsonStr)) {
//                        List<RegionListBean> list = new Gson().fromJson(
//                                jsonStr, new TypeToken<List<RegionListBean>>() {
//                                }.getType());
//                        onRegionListListener.onRegionListSuccess(list);
//                    }
//                } else {
//                    onRegionListListener.onRegionListFailure(response.getMessage());
//                }
//            }
//        });

        HttpUtil.okHttpPostJson(mContext, ApiConstants.getRegionList, map, new CallBackUtil.CallBackString(mContext) {
            @Override
            public void onFailure(Call call, Exception e) {
                LogUtils.i("MainImpl", "MainImpl getRegionList onFailure --> " + e.getMessage());
                onCallBackListener.onRegionListFailure("");
            }

            @Override
            public void onResponse(String response) {
                try {
                    LogUtils.i("MainImpl", "MainImpl getRegionList onResponse ---> " + response.toString());
                    if (!StringUtils.isNullOrBlank(response)) {
                        ResponseBean responseBean = new Gson().fromJson(
                                response, new TypeToken<ResponseBean>() {
                                }.getType());
                        if (responseBean.getCode() == 200) {
                            String jsonStr = new Gson().toJson(responseBean.getData());
                            if (!StringUtils.isNullOrBlank(jsonStr)) {
                                RegionBean regionBean = new Gson().fromJson(
                                        jsonStr, new TypeToken<RegionBean>() {
                                        }.getType());
                                onCallBackListener.onRegionListSuccess(regionBean);
                            }
                        } else {
                            onCallBackListener.onRegionListFailure(responseBean.getMessage());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 获取个人信息
     *
     * @param mContext
     * @param map
     * @param onCallBackListener
     */
    public void getUserInfo(Context mContext, Map<String, String> map,
                            OnCallBackListener onCallBackListener) {

//        ApiHttpClientUtil.requestPost(mContext, map, ApiConstants.userInfo, new ApiCallback() {
//            @Override
//            public void onFailure(ApiRequest request, Exception e) {
//                LogUtils.i("MainImpl", "MainImpl onFailure --> " + e.getMessage());
//                onGetUserInfoListener.onGetUserInfoFailure("");
//            }
//
//            @Override
//            public void onResponse(ApiRequest request, ApiResponse response) {
//                LogUtils.i("MainImpl", "MainImpl onResponse ---> " + response.toString());
//                if (response.getCode() == 200) {
//                    String jsonStr = response.getMessage();
//                    if (!StringUtils.isNullOrBlank(jsonStr)) {
//                        UserInfoBean userInfoBean = new Gson().fromJson(
//                                jsonStr, new TypeToken<UserInfoBean>() {
//                                }.getType());
//                        onGetUserInfoListener.onGetUserInfoSuccess(userInfoBean);
//                    }
//                    onGetUserInfoListener.onGetUserInfoSuccess(new UserInfoBean());
//                } else {
//                    onGetUserInfoListener.onGetUserInfoFailure(response.getMessage());
//                }
//            }
//        });

        HttpUtil.okHttpGet(mContext, ApiConstants.userInfo, map, new CallBackUtil.CallBackString(mContext) {
            @Override
            public void onFailure(Call call, Exception e) {
                LogUtils.i("MainImpl", "MainImpl userInfo onFailure --> " + e.getMessage());
                onCallBackListener.onGetUserInfoFailure("");
            }

            @Override
            public void onResponse(String response) {
                try {
                    LogUtils.i("MainImpl", "MainImpl userInfo onResponse ---> " + response.toString());
                    if (!StringUtils.isNullOrBlank(response)) {
                        ResponseBean responseBean = new Gson().fromJson(
                                response, new TypeToken<ResponseBean>() {
                                }.getType());
                        if (responseBean.getCode() == 200) {
                            String jsonStr = new Gson().toJson(responseBean.getData());
                            if (!StringUtils.isNullOrBlank(jsonStr)) {
                                UserInfoBean userInfoBean = new Gson().fromJson(
                                        jsonStr, new TypeToken<UserInfoBean>() {
                                        }.getType());
                                onCallBackListener.onGetUserInfoSuccess(userInfoBean);
                            }
                        } else {
                            onCallBackListener.onGetUserInfoFailure(responseBean.getMessage());
                        }
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
    public void talkTimeInfo(Context mContext, Map<String, String> map,
                             OnCallBackListener onCallBackListener) {

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
                LogUtils.i("MainImpl", "MainImpl talkTimeInfo onFailure --> " + e.getMessage());
                onCallBackListener.onGetTalkTimeInfoFailure("");
            }

            @Override
            public void onResponse(String response) {
                try {
                    LogUtils.i("MainImpl", "MainImpl talkTimeInfo onResponse ---> " + response.toString());
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

    /**
     * 获取筛选列表
     *
     * @param mContext
     * @param object
     * @param onCallBackListener
     */
    public void getFilterList(Context mContext, Object object,
                              OnCallBackListener onCallBackListener) {

        HttpUtil.okHttpPostObjJson(mContext, ApiConstants.getFilterList, object, new CallBackUtil.CallBackString(mContext) {
            @Override
            public void onFailure(Call call, Exception e) {
                LogUtils.i("MainImpl", "MainImpl getFilterList onFailure --> " + e.getMessage());
                onCallBackListener.onGetTalkTimeInfoFailure("");
            }

            @Override
            public void onResponse(String response) {
                try {
                    LogUtils.i("MainImpl", "MainImpl getFilterList onResponse ---> " + response.toString());
                    if (!StringUtils.isNullOrBlank(response)) {
                        ResponseBean responseBean = new Gson().fromJson(
                                response, new TypeToken<ResponseBean>() {
                                }.getType());
                        if (responseBean.getCode() == 200) {
                            String jsonStr = new Gson().toJson(responseBean.getData());
                            if (!StringUtils.isNullOrBlank(jsonStr)) {
                                FilterListBean filterListBean = new Gson().fromJson(
                                        jsonStr, new TypeToken<FilterListBean>() {
                                        }.getType());
                                onCallBackListener.onGetFilterListSuccess(filterListBean);
                            }
                        } else {
                            onCallBackListener.onGetFilterListFailure(responseBean.getMessage());
                        }
                    } else {
                        onCallBackListener.onGetFilterListFailure("");
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
                LogUtils.i("MainImpl", "MainImpl updateCallRecords onFailure --> " + e.getMessage());
                onCallBackListener.onUpdateCallRecordsFailure("");
            }

            @Override
            public void onResponse(String response) {
                try {
                    LogUtils.i("MainImpl", "MainImpl updateCallRecords onResponse ---> " + response.toString());
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

    // 回调
    public interface OnCallBackListener {
        // 成功
        void onGetCluesListSuccess(CluesBean cluesBean);
        // 失败
        void onGetCluesListFailure(String msg);

        // 成功
        void onIndustryListSuccess(IndustryBean industryBean);
        // 失败
        void onIndustryListFailure(String msg);

        // 成功
        void onRegionListSuccess(RegionBean regionBean);
        // 失败
        void onRegionListFailure(String msg);

        // 成功
        void onGetUserInfoSuccess(UserInfoBean userInfoBean);
        // 失败
        void onGetUserInfoFailure(String msg);

        // 成功
        void onGetTalkTimeInfoSuccess(TalkTimeInfoBean TalkTimeInfoBean);
        // 失败
        void onGetTalkTimeInfoFailure(String msg);

        // 成功
        void onGetFilterListSuccess(FilterListBean filterListBean);
        // 失败
        void onGetFilterListFailure(String msg);

        // 成功
        void onUpdateCallRecordsSuccess();
        // 失败
        void onUpdateCallRecordsFailure(String msg);
    }

}
