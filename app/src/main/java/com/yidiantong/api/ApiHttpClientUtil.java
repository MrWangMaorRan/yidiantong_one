package com.yidiantong.api;

import android.content.Context;

import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import com.alibaba.cloudapi.sdk.enums.HttpMethod;
import com.alibaba.cloudapi.sdk.model.ApiCallback;
import com.alibaba.cloudapi.sdk.model.ApiRequest;
import com.alibaba.cloudapi.sdk.model.ApiResponse;
import com.alibaba.fastjson.JSONObject;
import com.yidiantong.util.log.LogUtils;
import com.yidiantong.util.SharedPreferencesUtil;

import java.io.IOException;
import java.util.Map;

public class ApiHttpClientUtil {

    /**
     * 请求接口
     *
     * @param map
     * @param requestUrl
     * @param apiCallback
     */
    public static void requestPost(Context mContext, Map<String, String> map, String requestUrl, ApiCallback apiCallback) {
        String jsonStr = JSONObject.toJSONString(map);
        LogUtils.i("ApiHttpClientUtil", "ApiHttpClientUtil str --> " + jsonStr);
        ApiRequest apiRequest = new ApiRequest(HttpMethod.POST_BODY_JSON, requestUrl, jsonStr.getBytes(SdkConstant.CLOUDAPI_ENCODING));
        String token = SharedPreferencesUtil.getSharedPreferences(mContext).getString("tkoen", "");
        apiRequest.setToken(token);
        LogUtils.i("ApiHttpClientUtil", "ApiHttpClientUtil token = " + token);
        ApiHttpsApiClient.getInstance().sendAsyncRequest(apiRequest, apiCallback);
    }


    /**
     * 打印回调的headler
     *
     * @param response
     * @return
     * @throws IOException
     */
    public static String getResultString(ApiResponse response) throws IOException {
        StringBuilder result = new StringBuilder();
        result.append("Response from backend server").append(SdkConstant.CLOUDAPI_LF).append(SdkConstant.CLOUDAPI_LF);
        result.append("ResultCode:").append(SdkConstant.CLOUDAPI_LF).append(response.getCode()).append(SdkConstant.CLOUDAPI_LF).append(SdkConstant.CLOUDAPI_LF);
        if (response.getCode() != 200) {
            result.append("Error description:").append(response.getHeaders().get("X-Ca-Error-Message")).append(SdkConstant.CLOUDAPI_LF).append(SdkConstant.CLOUDAPI_LF);
        }

        result.append("ResultBody:").append(SdkConstant.CLOUDAPI_LF).append(new String(response.getBody(), SdkConstant.CLOUDAPI_ENCODING));

        return result.toString();
    }
}
