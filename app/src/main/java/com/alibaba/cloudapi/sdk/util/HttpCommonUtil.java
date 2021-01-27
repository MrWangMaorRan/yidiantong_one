package com.alibaba.cloudapi.sdk.util;

import com.alibaba.cloudapi.sdk.constant.SdkConstant;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by fred on 2017/7/17.
 */
public class HttpCommonUtil {
    public static String buildParamString(Map<String , String> params){
        StringBuilder result = new StringBuilder();
        if(null != params && params.size() > 0){
            boolean isFirst = true;
            for(String key : params.keySet()){
                if(isFirst){
                    isFirst = false;
                }
                else{
                    result.append("&");
                }

                try {
                    result.append(key).append("=").append(URLEncoder.encode(params.get(key), SdkConstant.CLOUDAPI_ENCODING.displayName()));
                }
                catch (UnsupportedEncodingException ex){
                    throw new RuntimeException(ex);
                }

            }
        }

        return result.toString();
    }
}
