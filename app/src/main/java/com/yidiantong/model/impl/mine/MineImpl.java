package com.yidiantong.model.impl.mine;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yidiantong.api.ApiConstants;
import com.yidiantong.bean.ResponseBean;
import com.yidiantong.bean.UpLoadFileBean;
import com.yidiantong.bean.UserInfoBean;
import com.yidiantong.httpUtils.CallBackUtil;
import com.yidiantong.httpUtils.HttpUtil;
import com.yidiantong.util.log.LogUtils;
import com.yidiantong.util.StringUtils;

import java.io.File;
import java.util.Map;

import okhttp3.Call;

public class MineImpl {

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
//                Log.i("MineImpl", "MineImpl onFailure --> " + e.getMessage());
//                onGetUserInfoListener.onGetUserInfoFailure("");
//            }
//
//            @Override
//            public void onResponse(ApiRequest request, ApiResponse response) {
//                Log.i("MineImpl", "MineImpl onResponse ---> " + response.toString());
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
                LogUtils.i("MineImpl", "MineImpl userInfo onFailure --> " + e.getMessage());
                onCallBackListener.onGetUserInfoFailure("");
            }

            @Override
            public void onResponse(String response) {
                try {
                    LogUtils.i("MineImpl", "MineImpl userInfo onResponse ---> " + response.toString());
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
     * 修改信息
     * @param mContext
     * @param onCallBackListener
     */
    public void uploadAvatar(Context mContext, File file, String fileKey, String fileType,
                               OnCallBackListener onCallBackListener) {

//        ApiHttpClientUtil.requestPost(mContext, map, ApiConstants.userInfoUpdate, new ApiCallback() {
//            @Override
//            public void onFailure(ApiRequest request, Exception e) {
//                Log.i("MineImpl", "MineImpl onFailure --> " + e.getMessage());
//                onUserInfoUpdateListener.onUserInfoUpdateFailure("");
//            }
//
//            @Override
//            public void onResponse(ApiRequest request, ApiResponse response) {
//                Log.i("MineImpl", "MineImpl onResponse ---> " + response.toString());
//                if (response.getCode() == 200) {
//                    onUserInfoUpdateListener.onUserInfoUpdateSuccess("保存成功");
//                } else {
//                    onUserInfoUpdateListener.onUserInfoUpdateFailure(response.getMessage());
//                }
//            }
//        });

        HttpUtil.okHttpPostFile_2(mContext, ApiConstants.uploadAvatar, file, fileKey, fileType, new CallBackUtil.CallBackString(mContext) {
            @Override
            public void onFailure(Call call, Exception e) {
                LogUtils.i("MineImpl", "MineImpl uploadAvatar onFailure --> " + e.getMessage());
                onCallBackListener.onUpLoadFileFailure("");
            }

            @Override
            public void onResponse(String response) {
                try {
                    LogUtils.i("MineImpl", "MineImpl uploadAvatar onResponse ---> " + response.toString());
                    if (!StringUtils.isNullOrBlank(response)) {
                        ResponseBean responseBean = new Gson().fromJson(
                                response, new TypeToken<ResponseBean>() {
                                }.getType());
                        if (responseBean.getCode() == 0) {
                            String jsonStr = new Gson().toJson(responseBean.getData());
                            if (!StringUtils.isNullOrBlank(jsonStr)) {
                                UpLoadFileBean upLoadFileBean = new Gson().fromJson(
                                        jsonStr, new TypeToken<UpLoadFileBean>() {
                                        }.getType());
                                onCallBackListener.onUpLoadFileSuccess(upLoadFileBean);
                            }
                        } else {
                            onCallBackListener.onUpLoadFileFailure(responseBean.getMessage());
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }


    // 回调
    public interface OnCallBackListener {

        // 成功
        void onGetUserInfoSuccess(UserInfoBean userInfoBean);
        // 失败
        void onGetUserInfoFailure(String msg);

        // 成功
        void onUpLoadFileSuccess(UpLoadFileBean upLoadFileBean);
        // 失败
        void onUpLoadFileFailure(String msg);
    }
}
