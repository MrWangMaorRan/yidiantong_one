package com.yidiantong.model.impl.company;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yidiantong.api.ApiConstants;
import com.yidiantong.bean.EnterprisesInfoBean;
import com.yidiantong.bean.ResponseBean;
import com.yidiantong.httpUtils.CallBackUtil;
import com.yidiantong.httpUtils.HttpUtil;
import com.yidiantong.util.log.LogUtils;
import com.yidiantong.util.StringUtils;

import java.util.Map;

import okhttp3.Call;

public class CompanyInfoImpl {

    /**
     * 获取企业基本信息
     * @param mContext
     * @param map
     * @param onCallBackListener
     */
    public void getEnterprisesInfo(Context mContext, Map<String, String> map,
                                   OnCallBackListener onCallBackListener) {

//        ApiHttpClientUtil.requestPost(mContext, map, ApiConstants.enterprisesInfo, new ApiCallback() {
//            @Override
//            public void onFailure(ApiRequest request, Exception e) {
//                LogUtils.i("CompanyInfoImpl", "CompanyInfoImpl onFailure --> " + e.getMessage());
//                onGetEnterprisesInfoListener.onGetEnterprisesInfoFailure("");
//            }
//
//            @Override
//            public void onResponse(ApiRequest request, ApiResponse response) {
//                LogUtils.i("CompanyInfoImpl", "CompanyInfoImpl onResponse ---> " + response.toString());
//                if (response.getCode() == 200) {
//                    String jsonStr = response.getMessage();
//                    if (!StringUtils.isNullOrBlank(jsonStr)) {
//                        EnterprisesInfoBean enterprisesInfoBean = new Gson().fromJson(
//                                jsonStr, new TypeToken<EnterprisesInfoBean>() {
//                                }.getType());
//                        onGetEnterprisesInfoListener.onGetEnterprisesInfoSuccess(enterprisesInfoBean);
//                    }
//                    onGetEnterprisesInfoListener.onGetEnterprisesInfoSuccess(new EnterprisesInfoBean());
//                } else {
//                    onGetEnterprisesInfoListener.onGetEnterprisesInfoFailure(response.getMessage());
//                }
//            }
//        });


        HttpUtil.okHttpGet(mContext, ApiConstants.enterprisesInfo, map, new CallBackUtil.CallBackString(mContext) {
            @Override
            public void onFailure(Call call, Exception e) {
                LogUtils.i("CompanyInfoImpl", "CompanyInfoImpl enterprisesInfo onFailure --> " + e.getMessage());
                onCallBackListener.onGetEnterprisesInfoFailure("");
            }

            @Override
            public void onResponse(String response) {
                try {
                    LogUtils.i("CompanyInfoImpl", "CompanyInfoImpl enterprisesInfo onResponse ---> " + response.toString());
                    if (!StringUtils.isNullOrBlank(response)) {
                        ResponseBean responseBean = new Gson().fromJson(
                                response, new TypeToken<ResponseBean>() {
                                }.getType());
                        if (responseBean != null && responseBean.getCode() == 200) {
                            String jsonStr = new Gson().toJson(responseBean.getData());
                            if (!StringUtils.isNullOrBlank(jsonStr)) {
                                EnterprisesInfoBean enterprisesInfoBean = new Gson().fromJson(
                                        jsonStr, new TypeToken<EnterprisesInfoBean>() {
                                        }.getType());
                                onCallBackListener.onGetEnterprisesInfoSuccess(enterprisesInfoBean);
                            }
                        } else{
                            onCallBackListener.onGetEnterprisesInfoFailure("获取失败");
                        }
                    }else {
                        onCallBackListener.onGetEnterprisesInfoFailure("获取失败");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

    }

    public interface OnCallBackListener {
        // 成功
        void onGetEnterprisesInfoSuccess(EnterprisesInfoBean enterprisesInfoBean);

        // 失败
        void onGetEnterprisesInfoFailure(String msg);
    }


}
