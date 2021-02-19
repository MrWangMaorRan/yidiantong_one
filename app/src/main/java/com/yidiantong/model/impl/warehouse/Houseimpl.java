package com.yidiantong.model.impl.warehouse;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yidiantong.api.ApiConstants;
import com.yidiantong.bean.ResponseBean;
import com.yidiantong.bean.UpLoadFileBean;
import com.yidiantong.bean.UserInfoBean;
import com.yidiantong.bean.WeiXinBean;
import com.yidiantong.httpUtils.CallBackUtil;
import com.yidiantong.httpUtils.HttpUtil;
import com.yidiantong.model.impl.mine.MineImpl;
import com.yidiantong.util.StringUtils;
import com.yidiantong.util.log.LogUtils;

import java.io.File;
import java.util.Map;

import okhttp3.Call;

public class Houseimpl {

    public void getWeixin(Context mContext, File file, String fileKey, String fileType,
                             Houseimpl.OnCallBackListener onCallBackListener) {
        HttpUtil.okHttpPostFile_2(mContext, ApiConstants.uploadAvatar, file, fileKey, fileType, new CallBackUtil.CallBackString(mContext) {
            @Override
            public void onFailure(Call call, Exception e) {
                LogUtils.i("MineImpl", "MineImpl uploadAvatar onFailure --> " + e.getMessage());
                onCallBackListener.onWeiXinFileFaulure("");
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
                                WeiXinBean weiXinBean = new Gson().fromJson(
                                        jsonStr, new TypeToken<WeiXinBean>() {
                                        }.getType());
                                onCallBackListener.onWeiXinFileSuccess(weiXinBean);
                            }
                        } else {
                            onCallBackListener.onWeiXinFileFaulure(responseBean.getMessage());
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
    void onWeiXinFileSuccess(WeiXinBean upLoadFileBean);
    // 失败
    void onWeiXinFileFaulure(String msg);
}
}
