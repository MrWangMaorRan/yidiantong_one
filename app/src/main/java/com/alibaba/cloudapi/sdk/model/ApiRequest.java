package com.alibaba.cloudapi.sdk.model;

import com.alibaba.cloudapi.sdk.enums.HttpConnectionModel;
import com.alibaba.cloudapi.sdk.enums.HttpMethod;
import com.alibaba.cloudapi.sdk.enums.ParamPosition;
import com.alibaba.cloudapi.sdk.enums.Scheme;
import com.alibaba.cloudapi.sdk.enums.WebSocketApiType;
import com.alibaba.cloudapi.sdk.exception.SdkException;
import com.alibaba.fastjson.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fred on 2017/7/14.
 */
public class ApiRequest extends ApiHttpMessage {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ApiRequest(HttpMethod method, String path) {
        this.method = method;
        this.path = path;
    }

    public ApiRequest(HttpMethod method, String path, byte[] body) {
        this.method = method;
        this.path = path;
        this.body = body;
    }

    public ApiRequest(JSONObject message) {
        parse(message);
    }

    private Scheme scheme;

    private HttpMethod method;

    private String host;

    private String path;

    private String url;

    private String signatureMethod;

    private Date currentDate;

    private HttpConnectionModel httpConnectionMode = HttpConnectionModel.SINGER_CONNECTION;

    private WebSocketApiType webSocketApiType = WebSocketApiType.COMMON;

    private Map<String, String> pathParams = new HashMap<String, String>();

    private Map<String, String> querys = new HashMap<String, String>();

    private Map<String, String> formParams = new HashMap<String, String>();

    private boolean isBase64BodyViaWebsocket = false;

    public Scheme getScheme() {
        return scheme;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getHost() {
        return host;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getPathParams() {
        return pathParams;
    }

    public Map<String, String> getQuerys() {
        return querys;
    }

    public Map<String, String> getFormParams() {
        return formParams;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void addParam(String name, String value, ParamPosition position, boolean isRequired) {
        if (value == null) {
            if (isRequired) {
                throw new SdkException(String.format("param %s is not nullable, please check your codes", name));
            } else {
                return;
            }
        }
        Map<String, String> targetParamMap = null;
        switch (position) {
            case HEAD: {
                addHeader(name, (String) value);
                return;
            }
            case PATH: {
                targetParamMap = this.pathParams;
                break;
            }
            case QUERY: {
                targetParamMap = this.querys;
                break;
            }
            case BODY: {
                targetParamMap = this.formParams;
                break;
            }
            default: {
                throw new SdkException("unknown param position: " + position);
            }
        }
        if (value instanceof String) {
            targetParamMap.put(name, (String) value);
        } else {
            targetParamMap.put(name, value.toString());
        }
    }

    public void setScheme(Scheme scheme) {
        this.scheme = scheme;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPathParams(Map<String, String> pathParams) {
        this.pathParams = pathParams;
    }


    public void setQuerys(Map<String, String> querys) {
        this.querys = querys;
    }

    public void setFormParams(Map<String, String> formParams) {
        this.formParams = formParams;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isBase64BodyViaWebsocket() {
        return isBase64BodyViaWebsocket;
    }

    public void setBase64BodyViaWebsocket(boolean base64BodyViaWebsocket) {
        isBase64BodyViaWebsocket = base64BodyViaWebsocket;
    }

    public String getSignatureMethod() {
        return signatureMethod;
    }

    public void setSignatureMethod(String signatureMethod) {
        this.signatureMethod = signatureMethod;
    }

    public HttpConnectionModel getHttpConnectionMode() {
        return httpConnectionMode;
    }

    public void setHttpConnectionMode(HttpConnectionModel httpConnectionMode) {
        this.httpConnectionMode = httpConnectionMode;
    }

    public void parse(JSONObject message) {
        super.parse(message);
    }

    public WebSocketApiType getWebSocketApiType() {
        return webSocketApiType;
    }

    public void setWebSocketApiType(WebSocketApiType webSocketApiType) {
        this.webSocketApiType = webSocketApiType;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public ApiRequest duplicate() {
        ApiRequest apiRequest = new ApiRequest(method, path, body);
        apiRequest.scheme = scheme;
        if (null != host) {
            apiRequest.host = new String(host);
        }
        if (null != url) {
            apiRequest.url = new String(url);
        }
        apiRequest.pathParams = new HashMap<String, String>();
        apiRequest.pathParams.putAll(pathParams);

        apiRequest.headers = new HashMap<String, List<String>>();
        apiRequest.headers.putAll(headers);

        apiRequest.querys = new HashMap<String, String>();
        apiRequest.querys.putAll(querys);

        apiRequest.formParams = new HashMap<String, String>();
        apiRequest.formParams.putAll(formParams);

        if (null != signatureMethod) {
            apiRequest.signatureMethod = new String(signatureMethod);
        }
        apiRequest.webSocketApiType = webSocketApiType;
        apiRequest.httpConnectionMode = httpConnectionMode;
        apiRequest.isBase64BodyViaWebsocket = isBase64BodyViaWebsocket;
        return apiRequest;

    }


}
