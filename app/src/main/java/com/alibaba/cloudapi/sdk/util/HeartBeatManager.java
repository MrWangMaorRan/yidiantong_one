package com.alibaba.cloudapi.sdk.util;

import com.alibaba.cloudapi.sdk.client.WebSocketApiClient;
import com.yidiantong.util.log.LogUtils;

/**
 * Created by fred on 2017/8/8.
 */
public class HeartBeatManager implements Runnable{
    WebSocketApiClient webSocketApiClient;
    int heartbeatInterval = 25000;
    boolean isStop = false;

    public HeartBeatManager(WebSocketApiClient webSocketApiClientp , int interval){
        this.webSocketApiClient = webSocketApiClientp;
        this.heartbeatInterval = interval;
        isStop = false;
    }

    public void stop(){
        isStop = true;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(heartbeatInterval);
                if(isStop == true){
                    break;
                }
                webSocketApiClient.sendHeatbeart();
            }
            catch (Exception ex){
                LogUtils.e("SDK" , "SEND HEARTBEAT FAILED " + ex);
            }
        }
    }
}
