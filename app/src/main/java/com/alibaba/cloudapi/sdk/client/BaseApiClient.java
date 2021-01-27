package com.alibaba.cloudapi.sdk.client;

import com.alibaba.cloudapi.sdk.enums.Scheme;
import com.alibaba.cloudapi.sdk.exception.SdkException;
import com.alibaba.cloudapi.sdk.model.ApiCallback;
import com.alibaba.cloudapi.sdk.model.ApiRequest;
import com.alibaba.cloudapi.sdk.model.ApiResponse;

/**
 * Created by fred on 2017/7/19.
 */
public abstract class BaseApiClient {
    String appKey= "";
    String appSecret = "";
    Scheme scheme = null;
    String host = "";
    boolean isInit = false;

    public void checkIsInit(){
        if(!isInit){
            throw new SdkException("MUST initial client before using");
        }
    }

    public abstract ApiResponse sendSyncRequest(ApiRequest apiRequest);
    public abstract void sendAsyncRequest(final ApiRequest apiRequest , final ApiCallback apiCallback);
}
