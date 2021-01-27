package com.yidiantong.api;

import com.alibaba.cloudapi.sdk.client.WebSocketApiClient;
import com.alibaba.cloudapi.sdk.enums.Scheme;
import com.alibaba.cloudapi.sdk.model.ApiResponse;
import com.alibaba.cloudapi.sdk.model.ApiWebSocketListner;
import com.alibaba.cloudapi.sdk.model.WebSocketClientBuilderParams;

public class ApiWebSocketApiClient {

    private static WebSocketApiClient webSocketApiClient;

    public static WebSocketApiClient getInstance() {
        if (webSocketApiClient == null) {
            synchronized (ApiWebSocketApiClient.class) {
                if (webSocketApiClient == null) {
                    webSocketApiClient = new WebSocketApiClient();
                }
            }
        }
        return webSocketApiClient;
    }

    /**
     * 初始化
     */
    public static void init() {
        WebSocketClientBuilderParams param = new WebSocketClientBuilderParams();
        param.setAppKey(ApiConstants.appid);
        param.setAppSecret(ApiConstants.secret);
        param.setHost(ApiConstants.host);
        param.setScheme(Scheme.HTTP);

        param.setApiWebSocketListner(new ApiWebSocketListner() {
            @Override
            public void onNotify(String message) {
                System.out.println(message);
            }

            @Override
            public void onFailure(Throwable t, ApiResponse response) {
                if (null != t) {
                    t.printStackTrace();
                }

                if (null != response) {
                    System.out.println(response.getCode());
                    System.out.println(response.getMessage());
                }

            }
        });

        ApiWebSocketApiClient.getInstance().init(param);
    }

}
