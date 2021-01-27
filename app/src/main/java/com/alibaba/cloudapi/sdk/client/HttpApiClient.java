/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.alibaba.cloudapi.sdk.client;

import com.alibaba.cloudapi.sdk.constant.HttpConstant;
import com.alibaba.cloudapi.sdk.enums.HttpConnectionModel;
import com.alibaba.cloudapi.sdk.enums.Scheme;
import com.alibaba.cloudapi.sdk.exception.SdkException;
import com.alibaba.cloudapi.sdk.model.ApiCallback;
import com.alibaba.cloudapi.sdk.model.ApiRequest;
import com.alibaba.cloudapi.sdk.model.ApiResponse;
import com.alibaba.cloudapi.sdk.model.HttpClientBuilderParams;
import com.alibaba.cloudapi.sdk.signature.SignerFactoryManager;
import com.alibaba.cloudapi.sdk.util.ApiRequestMaker;
import com.alibaba.cloudapi.sdk.util.HttpCommonUtil;
import com.yidiantong.util.log.LogUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by fred on 16/9/7.
 */
public class HttpApiClient extends BaseApiClient {
    OkHttpClient client;

    public HttpApiClient() {
    }

    public void  init(HttpClientBuilderParams buildParam) {
        if (null == buildParam) {
            throw new SdkException("buildParam must not be null");
        }

        buildParam.check();

        this.appKey = buildParam.getAppKey();
        this.appSecret = buildParam.getAppSecret();
        host = buildParam.getHost();
        scheme = buildParam.getScheme();
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(buildParam.getReadTimeout(), TimeUnit.MILLISECONDS)
                .writeTimeout(buildParam.getWriteTimeout(), TimeUnit.MILLISECONDS)
                .connectTimeout(buildParam.getConnectionTimeout(), TimeUnit.MILLISECONDS);

        if (null != buildParam.getEventListenerFactory()) {
            builder.eventListenerFactory(buildParam.getEventListenerFactory());
        }

        if (null != buildParam.getSocketFactory()) {
            builder.socketFactory(buildParam.getSocketFactory());
        }

        builder.retryOnConnectionFailure(buildParam.isHttpConnectionRetry());

        if (null != buildParam.getInterceptor()) {
            builder.interceptors().add(buildParam.getInterceptor());
        }

        if (scheme == Scheme.HTTPS) {
            builder.sslSocketFactory(buildParam.getSslSocketFactory(), buildParam.getX509TrustManager())
                    .hostnameVerifier(buildParam.getHostnameVerifier());
        }
        client = builder.build();

        SignerFactoryManager.init();

        isInit = true;
    }


    @Override
    public ApiResponse sendSyncRequest(ApiRequest apiRequest) {
        checkIsInit();
        Request request = this.buildRequest(apiRequest);
        Call call = client.newCall(request);

        try {
            return getApiResponse(apiRequest, call.execute());
        } catch (IOException ex) {
            return new ApiResponse(500, "Read response occur error", ex);
        }

    }

    @Override
    public void sendAsyncRequest(final ApiRequest apiRequest, final ApiCallback apiCallback) {
        checkIsInit();
        final Request request = this.buildRequest(apiRequest);
        LogUtils.i("HttpApiClient", "HttpApiClient --> " + " url = " + request.url()
                + " method = " + request.method());
        Call call = client.newCall(request);
        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                apiCallback.onFailure(apiRequest, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                apiCallback.onResponse(apiRequest, getApiResponse(apiRequest, response));
            }
        };
        call.enqueue(callback);

    }


    private Request buildRequest(ApiRequest request) {
        if (request.getHttpConnectionMode() == HttpConnectionModel.SINGER_CONNECTION) {
            request.setHost(host);
            request.setScheme(scheme);
        }

        ApiRequestMaker.make(request, appKey, appSecret);
        RequestBody requestBody = null;
        if (null != request.getFormParams() && request.getFormParams().size() > 0) {
            requestBody = RequestBody.create(MediaType.parse(request.getMethod().getRequestContentType()), HttpCommonUtil.buildParamString(request.getFormParams()));
        }
        /**
         *  如果类型为byte数组的body不为空
         *  将body中的内容MD5算法加密后再采用BASE64方法Encode成字符串，放入HTTP头中
         *  做内容校验，避免内容在网络中被篡改
         */
        else if (null != request.getBody() && request.getBody().length > 0) {
            requestBody = RequestBody.create(MediaType.parse(request.getMethod().getRequestContentType()), request.getBody());
        }
        return new Request.Builder()
                .method(request.getMethod().getValue(), requestBody)
                .url(request.getUrl())
                .headers(getHeadersFromMap(request.getHeaders()))
                .build();
    }

    private Headers getHeadersFromMap(Map<String, List<String>> map) {
        List<String> nameAndValues = new ArrayList<String>();
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            for (String value : entry.getValue()) {
                nameAndValues.add(entry.getKey());
                nameAndValues.add(value);
            }
        }

        return Headers.of(nameAndValues.toArray(new String[nameAndValues.size()]));

    }

    private ApiResponse getApiResponse(ApiRequest request, Response response) throws IOException {
        ApiResponse apiResponse = new ApiResponse(response.code());
        apiResponse.setHeaders(response.headers().toMultimap());
        apiResponse.setBody(response.body().bytes());
        apiResponse.setContentType(response.header(HttpConstant.CLOUDAPI_HTTP_HEADER_CONTENT_TYPE, ""));

        return apiResponse;
    }

    /**
     * 暂时不支持同名header
     * @param headers
     * @return
     */
    private Map<String, String> getSimpleMapFromRequest(Headers headers) {
        Map<String, List<String>> complexMap = headers.toMultimap();
        Map<String, String> simpleMap = new HashMap<String, String>();
        for (Map.Entry<String, List<String>> entry : complexMap.entrySet()) {
            simpleMap.put(entry.getKey(), entry.getValue().get(0));
        }

        return simpleMap;
    }


}
