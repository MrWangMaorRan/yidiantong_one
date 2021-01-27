package com.yidiantong.model.impl.login;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yidiantong.api.ApiConstants;
import com.yidiantong.bean.LoginBean;
import com.yidiantong.bean.ResponseBean;
import com.yidiantong.bean.UserInfoBean;
import com.yidiantong.httpUtils.CallBackUtil;
import com.yidiantong.httpUtils.HttpUtil;
import com.yidiantong.util.log.LogUtils;
import com.yidiantong.util.StringUtils;

import java.util.Map;

import okhttp3.Call;


public class LoginImpl {

    /**
     * 登录
     *
     * @param map
     * @param onCallBackListener
     */
    public void login(Context mContext, Map<String, String> map,
                      OnCallBackListener onCallBackListener) {

//        ApiHttpClientUtil.requestPost(mContext, map, ApiConstants.login, new ApiCallback() {
//            @Override
//            public void onFailure(ApiRequest request, Exception e) {
//                LogUtils.i("loginImpl", "loginImpl onFailure --> " + e.getMessage());
//                onLoginListener.onLoginFailure("");
//            }
//
//            @Override
//            public void onResponse(ApiRequest request, ApiResponse response) {
//                LogUtils.i("loginImpl", "loginImpl onResponse ---> " + response.toString());
//                if (response.getCode() == 200) {
//                    String jsonStr = response.getMessage();
//                    if (!StringUtils.isNullOrBlank(jsonStr)) {
//                        LoginBean loginBean = new Gson().fromJson(
//                                jsonStr, new TypeToken<LoginBean>() {
//                                }.getType());
//                        onLoginListener.onLoginSuccess(loginBean);
//                    }
//                    onLoginListener.onLoginSuccess(new LoginBean());
//                } else {
//                    onLoginListener.onLoginFailure(response.getMessage());
//                }
//            }
//        });

        HttpUtil.okHttpPostJson(mContext, ApiConstants.login, map, new CallBackUtil.CallBackString(mContext) {
            @Override
            public void onFailure(Call call, Exception e) {
                LogUtils.i("loginImpl", "loginImpl onFailure --> " + e.getMessage());
                onCallBackListener.onLoginFailure("");
            }

            @Override
            public void onResponse(String response) {
                try {
                    LogUtils.i("loginImpl", "loginImpl onResponse ---> " + response);
                    if (!StringUtils.isNullOrBlank(response)) {
                        ResponseBean responseBean = new Gson().fromJson(
                                response, new TypeToken<ResponseBean>() {
                                }.getType());
                        if (responseBean != null && responseBean.getCode() == 200) {
                            String jsonStr = new Gson().toJson(responseBean.getData());
                            if (!StringUtils.isNullOrBlank(jsonStr)) {
                                LoginBean loginBean = new Gson().fromJson(
                                        jsonStr, new TypeToken<LoginBean>() {
                                        }.getType());
                                onCallBackListener.onLoginSuccess(loginBean);
                            }
                        } else {
                            onCallBackListener.onLoginFailure(responseBean.getMessage());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }


    /**
     * 发送验证码
     *
     * @param map
     * @param onCallBackListener
     */
    public void sendCode(Context mContext, Map<String, String> map, OnCallBackListener onCallBackListener) {

//        ApiHttpClientUtil.requestPost(mContext, map, ApiConstants.sendCode, new ApiCallback() {
//            @Override
//            public void onFailure(ApiRequest request, Exception e) {
//                LogUtils.i("loginImpl", "loginImpl onFailure --> " + e.getMessage());
//                onSendCodeListener.onSendCodeFailure("");
//            }
//
//            @Override
//            public void onResponse(ApiRequest request, ApiResponse response) {
//                LogUtils.i("loginImpl", "loginImpl onResponse ---> " + response.toString());
//                if (response.getCode() == 200) {
//                    onSendCodeListener.onSendCodeSuccess(response.getMessage());
//                } else {
//                    onSendCodeListener.onSendCodeFailure(response.getMessage());
//                }
//            }
//        });


        HttpUtil.okHttpPostJson(mContext, ApiConstants.sendCode, map, new CallBackUtil.CallBackString(mContext) {
            @Override
            public void onFailure(Call call, Exception e) {
                LogUtils.i("loginImpl", "loginImpl onFailure --> " + e.getMessage());
                onCallBackListener.onSendCodeFailure("");
            }

            @Override
            public void onResponse(String response) {
                try {
                    LogUtils.i("loginImpl", "loginImpl onResponse ---> " + response.toString());
                    if (!StringUtils.isNullOrBlank(response)) {
                        ResponseBean responseBean = new Gson().fromJson(
                                response, new TypeToken<ResponseBean>() {
                                }.getType());
                        if (responseBean != null && responseBean.getCode() == 200) {
                            onCallBackListener.onSendCodeSuccess(responseBean.getMessage());
                        } else {
                            onCallBackListener.onSendCodeFailure(responseBean.getMessage());
                        }
                    } else {
                        onCallBackListener.onSendCodeFailure("");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

//    /**
//     * 获取个人信息 （登陆逻辑改动2020-07-29）
//     *
//     * @param mContext
//     * @param map
//     * @param onCallBackListener
//     */
//    public void getUserInfo(Context mContext, Map<String, String> map,
//                            OnCallBackListener onCallBackListener) {
//
////        ApiHttpClientUtil.requestPost(mContext, map, ApiConstants.userInfo, new ApiCallback() {
////            @Override
////            public void onFailure(ApiRequest request, Exception e) {
////                LogUtils.i("MainImpl", "MainImpl onFailure --> " + e.getMessage());
////                onGetUserInfoListener.onGetUserInfoFailure("");
////            }
////
////            @Override
////            public void onResponse(ApiRequest request, ApiResponse response) {
////                LogUtils.i("MainImpl", "MainImpl onResponse ---> " + response.toString());
////                if (response.getCode() == 200) {
////                    String jsonStr = response.getMessage();
////                    if (!StringUtils.isNullOrBlank(jsonStr)) {
////                        UserInfoBean userInfoBean = new Gson().fromJson(
////                                jsonStr, new TypeToken<UserInfoBean>() {
////                                }.getType());
////                        onGetUserInfoListener.onGetUserInfoSuccess(userInfoBean);
////                    }
////                    onGetUserInfoListener.onGetUserInfoSuccess(new UserInfoBean());
////                } else {
////                    onGetUserInfoListener.onGetUserInfoFailure(response.getMessage());
////                }
////            }
////        });
//
//        HttpUtil.okHttpGet(mContext, ApiConstants.userInfo, map, new CallBackUtil.CallBackString(mContext) {
//            @Override
//            public void onFailure(Call call, Exception e) {
//                LogUtils.i("LoginImpl", "LoginImpl userInfo onFailure --> " + e.getMessage());
//                onCallBackListener.onGetUserInfoFailure("");
//            }
//
//            @Override
//            public void onResponse(String response) {
//                try {
//                    LogUtils.i("LoginImpl", "LoginImpl userInfo onResponse ---> " + response.toString());
//                    if (!StringUtils.isNullOrBlank(response)) {
//                        ResponseBean responseBean = new Gson().fromJson(
//                                response, new TypeToken<ResponseBean>() {
//                                }.getType());
//                        if (responseBean.getCode() == 200) {
//                            String jsonStr = new Gson().toJson(responseBean.getData());
//                            if (!StringUtils.isNullOrBlank(jsonStr)) {
//                                UserInfoBean userInfoBean = new Gson().fromJson(
//                                        jsonStr, new TypeToken<UserInfoBean>() {
//                                        }.getType());
//                                onCallBackListener.onGetUserInfoSuccess(userInfoBean);
//                            }
//                        } else {
//                            onCallBackListener.onGetUserInfoFailure(responseBean.getMessage());
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

    public interface OnCallBackListener {
        // 成功
        void onSendCodeSuccess(String data);
        // 失败
        void onSendCodeFailure(String message);

        // 成功
        void onLoginSuccess(LoginBean loginBean);
        // 失败
        void onLoginFailure(String msg);

        // 登陆逻辑改动2020-07-29
//        // 成功
//        void onGetUserInfoSuccess(UserInfoBean userInfoBean);
//        // 失败
//        void onGetUserInfoFailure(String message);

    }


}
