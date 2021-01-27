package com.yidiantong.bean;

public class ResponseCallAllBean {

    private int code;
    private String message;
    private String callId;
    private Object data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseCallAllBean{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", callId='" + callId + '\'' +
                ", data=" + data +
                '}';
    }
}
