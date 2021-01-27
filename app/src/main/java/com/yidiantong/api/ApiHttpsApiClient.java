package com.yidiantong.api;

import com.alibaba.cloudapi.sdk.client.HttpApiClient;
import com.alibaba.cloudapi.sdk.enums.Scheme;
import com.alibaba.cloudapi.sdk.model.HttpClientBuilderParams;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class ApiHttpsApiClient {
    private static HttpApiClient httpApiClient;

    public static HttpApiClient getInstance() {
        if (httpApiClient == null) {
            synchronized (ApiHttpsApiClient.class) {
                if (httpApiClient == null) {
                    httpApiClient = new HttpApiClient();
                }
            }
        }
        return httpApiClient;
    }

    /**
     * 初始化
     */
    public static void init() {
        HttpClientBuilderParams param = new HttpClientBuilderParams();
        param.setAppKey(ApiConstants.appid);
        param.setAppSecret(ApiConstants.secret);
        param.setHost(ApiConstants.host);
        param.setScheme(Scheme.HTTP);

        /**
         * 以HTTPS方式提交请求
         * 本DEMO采取忽略证书的模式,目的是方便大家的调试
         * 为了安全起见,建议采取证书校验方式
         */
        X509TrustManager xtm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                X509Certificate[] x509Certificates = new X509Certificate[0];
                return x509Certificates;
            }
        };

        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{xtm}, new SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        }
        HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        param.setSslSocketFactory(sslContext.getSocketFactory());
        param.setX509TrustManager(xtm);
        param.setHostnameVerifier(DO_NOT_VERIFY);

        ApiHttpsApiClient.getInstance().init(param);
    }
}
