package com.alibaba.cloudapi.sdk.signature;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by guikong on 17/10/28.
 */
public class SignerFactoryManager {

    private static Map<String, ISignerFactory> factoryMap = new HashMap<>(2);

    /**
     * add the default SignerFactory
     */
    public static void init() {
        registerSignerFactory(HMacSHA256SignerFactory.METHOD, new HMacSHA256SignerFactory());
        registerSignerFactory(HMacSHA1SignerFactory.METHOD, new HMacSHA1SignerFactory());
    }

    /**
     * @param method,  method name, i.e. "HmacSHA256", can not be null or ""
     * @param factory, can not be null
     * @return old one if exist
     */
    public static ISignerFactory registerSignerFactory(String method, ISignerFactory factory) {

        if (TextUtils.isEmpty(method)) {
            throw new IllegalArgumentException("method can not be empty");
        }

        if (null == factory) {
            throw new IllegalArgumentException("factory can not be null");
        }

        return factoryMap.put(method, factory);
    }

    /**
     * @param method method name, i.e. "HmacSHA256", can be null, default value is "HmacSHA256"
     * @return maybe null
     */
    public static ISignerFactory findSignerFactory(String method) {

        if (TextUtils.isEmpty(method)) {
            method = HMacSHA256SignerFactory.METHOD;
        }

        return factoryMap.get(method);
    }
}
