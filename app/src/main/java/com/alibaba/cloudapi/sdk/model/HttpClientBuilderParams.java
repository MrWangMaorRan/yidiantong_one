package com.alibaba.cloudapi.sdk.model;

import com.alibaba.cloudapi.sdk.enums.Scheme;
import com.alibaba.cloudapi.sdk.exception.SdkException;

import java.util.EventListener;

import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;

/**
 * Created by fred on 2017/7/14.
 */
public class HttpClientBuilderParams extends BaseClientInitialParam {

    SSLSocketFactory sslSocketFactory = null;
    X509TrustManager x509TrustManager = null;
    HostnameVerifier hostnameVerifier = null;
    okhttp3.EventListener.Factory eventListenerFactory = null;
    SocketFactory socketFactory = null;
    boolean isHttpConnectionRetry = true;
    Interceptor interceptor = null;



    public void check() {
        super.check();
        if (Scheme.HTTPS == scheme) {
            if (null == sslSocketFactory || null == x509TrustManager || null == hostnameVerifier) {
                throw new SdkException("https channel need sslSocketFactory amd x509TrustManager and hostnameVerifier for communication");
            }
        }

    }


    public SSLSocketFactory getSslSocketFactory() {
        return sslSocketFactory;
    }

    public void setSslSocketFactory(SSLSocketFactory sslSocketFactory) {
        this.sslSocketFactory = sslSocketFactory;
    }

    public X509TrustManager getX509TrustManager() {
        return x509TrustManager;
    }

    public void setX509TrustManager(X509TrustManager x509TrustManager) {
        this.x509TrustManager = x509TrustManager;
    }

    public HostnameVerifier getHostnameVerifier() {
        return hostnameVerifier;
    }

    public void setHostnameVerifier(HostnameVerifier hostnameVerifier) {
        this.hostnameVerifier = hostnameVerifier;
    }

    public okhttp3.EventListener.Factory getEventListenerFactory() {
        return eventListenerFactory;
    }

    public void setEventListenerFactory(okhttp3.EventListener.Factory eventListenerFactory) {
        this.eventListenerFactory = eventListenerFactory;
    }

    public SocketFactory getSocketFactory() {
        return socketFactory;
    }

    public void setSocketFactory(SocketFactory socketFactory) {
        this.socketFactory = socketFactory;
    }

    public boolean isHttpConnectionRetry() {
        return isHttpConnectionRetry;
    }

    public void setHttpConnectionRetry(boolean httpConnectionRetry) {
        isHttpConnectionRetry = httpConnectionRetry;
    }

    public Interceptor getInterceptor() {
        return interceptor;
    }

    public void setInterceptor(Interceptor interceptor) {
        this.interceptor = interceptor;
    }
}

