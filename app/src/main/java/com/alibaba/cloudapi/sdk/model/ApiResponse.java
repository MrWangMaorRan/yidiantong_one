package com.alibaba.cloudapi.sdk.model;

import com.alibaba.cloudapi.sdk.constant.HttpConstant;
import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by fred on 2017/7/14.
 */
public class ApiResponse extends ApiHttpMessage {
    int code;
    String message;
    String contentType;
    Exception ex;

    public ApiResponse(int code){
        this.code = code;
    }

    public ApiResponse(int errorCode ,String message , Exception ex){
        this.code = errorCode;
        this.message = message;
        this.ex = ex;
    }

    public ApiResponse(JSONObject jsonObject){
        this.parse(jsonObject);
    }

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


    public Exception getEx() {
        return ex;
    }

    public void setEx(Exception ex) {
        this.ex = ex;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void parse(JSONObject message){
        super.parse(message);
        this.code = Integer.parseInt(message.get("status").toString());
        this.contentType = getFirstHeaderValue(HttpConstant.CLOUDAPI_HTTP_HEADER_CONTENT_TYPE);
        if(null != this.getFirstHeaderValue(SdkConstant.CLOUDAPI_X_CA_ERROR_MESSAGE)){
            this.message = this.getFirstHeaderValue(SdkConstant.CLOUDAPI_X_CA_ERROR_MESSAGE);
        }
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", contentType='" + contentType + '\'' +
                ", ex=" + ex +
                '}';
    }
}
