package com.yidiantong.bean;


/**
 * 登录结果
 */
public class LoginBean extends ResponseBean {

    private String token ; // 鉴权token
    private String callType; // AXB 或者 DualCall  DualCall = 双呼

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }
}
