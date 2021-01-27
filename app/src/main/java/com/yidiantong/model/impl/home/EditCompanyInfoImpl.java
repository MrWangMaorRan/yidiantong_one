package com.yidiantong.model.impl.home;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yidiantong.api.ApiConstants;
import com.yidiantong.bean.ResponseBean;
import com.yidiantong.httpUtils.CallBackUtil;
import com.yidiantong.httpUtils.HttpUtil;
import com.yidiantong.util.log.LogUtils;
import com.yidiantong.util.StringUtils;

import okhttp3.Call;

public class EditCompanyInfoImpl {

    /**
     * 线索详情编辑
     * @param mContext
     * @param object
     * @param onUpdateCluesInfoListener
     */
    public void updateCluesInfo(Context mContext, Object object,
                                OnUpdateCluesInfoListener onUpdateCluesInfoListener) {

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


        HttpUtil.okHttpPostObjJson(mContext, ApiConstants.updateCluesInfo, object, new CallBackUtil.CallBackString(mContext) {
            @Override
            public void onFailure(Call call, Exception e) {
                LogUtils.i("EditCompanyInfoImpl", "EditCompanyInfoImpl updateCluesInfo onFailure --> " + e.getMessage());
                onUpdateCluesInfoListener.onUpdateCluesInfoFailure("");
            }

            @Override
            public void onResponse(String response) {
                try {
                    LogUtils.i("EditCompanyInfoImpl", "EditCompanyInfoImpl updateCluesInfo onResponse ---> " + response.toString());
                    if (!StringUtils.isNullOrBlank(response)) {
                        ResponseBean responseBean = new Gson().fromJson(
                                response, new TypeToken<ResponseBean>() {
                                }.getType());
                        if (responseBean.getCode() == 200) {
                            onUpdateCluesInfoListener.onUpdateCluesInfoSuccess("");
                        } else {
                            onUpdateCluesInfoListener.onUpdateCluesInfoFailure(responseBean.getMessage());
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    public interface OnUpdateCluesInfoListener {
        // 成功
        void onUpdateCluesInfoSuccess(String msg);
        // 失败
        void onUpdateCluesInfoFailure(String msg);
    }

}
