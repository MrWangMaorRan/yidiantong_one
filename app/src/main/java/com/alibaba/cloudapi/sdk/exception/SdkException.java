package com.alibaba.cloudapi.sdk.exception;

/**
 * Created by fred on 2017/5/3.
 */
public class SdkException extends RuntimeException {

    public SdkException(String message) {
        super(message);
    }

    public SdkException(String message, Throwable cause) {
        super(message, cause);
    }

    public SdkException(Throwable cause) {
        super(cause);
    }

}