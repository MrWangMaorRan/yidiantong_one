package com.yidiantong.model.impl.mine;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yidiantong.api.ApiConstants;
import com.yidiantong.bean.ResponseBean;
import com.yidiantong.httpUtils.CallBackUtil;
import com.yidiantong.httpUtils.HttpUtil;
import com.yidiantong.util.log.LogUtils;
import com.yidiantong.util.StringUtils;

import java.util.Map;

import okhttp3.Call;

public class EditMineInfoImpl {

    /**
     * 修改信息
     * @param mContext
     * @param map
     * @param onCallBackListener
     */
    public void userInfoUpdate(Context mContext, Map<String, String> map,
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

        HttpUtil.okHttpPostJson(mContext, ApiConstants.userInfoUpdate, map, new CallBackUtil.CallBackString(mContext) {
            @Override
            public void onFailure(Call call, Exception e) {
                LogUtils.i("EditMineInfoImpl", "EditMineInfoImpl userInfoUpdate onFailure --> " + e.getMessage());
                onCallBackListener.onUserInfoUpdateFailure("");
            }

            @Override
            public void onResponse(String response) {
                try {
                    LogUtils.i("EditMineInfoImpl", "EditMineInfoImpl userInfoUpdate onResponse ---> " + response.toString());
                    if (!StringUtils.isNullOrBlank(response)) {
                        ResponseBean responseBean = new Gson().fromJson(
                                response, new TypeToken<ResponseBean>() {
                                }.getType());
                        if (responseBean.getCode() == 200) {
                            onCallBackListener.onUserInfoUpdateSuccess("保存成功");
                        } else {
                            onCallBackListener.onUserInfoUpdateFailure(responseBean.getMessage());
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    public interface OnCallBackListener {
        // 成功
        void onUserInfoUpdateSuccess(String msg);

        // 失败
        void onUserInfoUpdateFailure(String msg);
    }

}
