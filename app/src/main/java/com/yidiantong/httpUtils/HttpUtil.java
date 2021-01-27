package com.yidiantong.httpUtils;

import android.content.Context;

import com.google.gson.Gson;
import com.yidiantong.api.ApiConstants;
import com.yidiantong.util.log.LogUtils;
import com.yidiantong.util.NetWorkUtils;
import com.yidiantong.util.SharedPreferencesUtil;
import com.yidiantong.util.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/9/11.
 */

public class HttpUtil {

    /**
     * @param context
     * @param url
     * @param paramsMap
     * @param callBack
     */
    public static void okHttpPostJson(final Context context, final String url,
                                      final Map<String, String> paramsMap, final CallBackUtil callBack) {
        NetWorkUtils.setNetWorkShow(context, new NetWorkUtils.NetWorkCallBack() {
            @Override
            public void netWorkCallBack() {
                okHttpPostHeader(context, ApiConstants.rootUrl + url, paramsMap, 0, callBack);
            }
        });
    }

    /**
     * @param context
     * @param url
     * @param object
     * @param callBack
     */
    public static void okHttpPostObjJson(final Context context, final String url,
                                         Object object, final CallBackUtil callBack) {
        NetWorkUtils.setNetWorkShow(context, new NetWorkUtils.NetWorkCallBack() {
            @Override
            public void netWorkCallBack() {
                okHttpPostJsonObj(context, ApiConstants.rootUrl + url, object, callBack);
            }
        });
    }

    /**
     * post请求，可以传递参数
     *
     * @param context     上下文对象
     * @param url         url
     * @param paramsMap   map集合，封装键值对参数
     * @param requestTime 发送请求的时间
     * @param callBack    回调接口，onFailure方法在请求失败时调用，onResponse方法在请求成功后调用，这两个方法都执行在UI线程。
     */
    public static void okHttpPostJson(final Context context,
                                      final String url,
                                      final Map<String, String> paramsMap,
                                      final long requestTime,
                                      final CallBackUtil callBack) {
        NetWorkUtils.setNetWorkShow(context, new NetWorkUtils.NetWorkCallBack() {
            @Override
            public void netWorkCallBack() {
                okHttpPostHeader(context, ApiConstants.rootUrl + url + url, paramsMap, requestTime, callBack);
            }
        });
    }

    /**
     * @param context
     * @param url
     * @param paramsMap
     * @param callBack
     */
    public static void okHttpGet(final Context context, final String url,
                                 final Map<String, String> paramsMap, final CallBackUtil callBack) {
        NetWorkUtils.setNetWorkShow(context, new NetWorkUtils.NetWorkCallBack() {
            @Override
            public void netWorkCallBack() {
                okHttpGetHeader(context, ApiConstants.rootUrl + url, paramsMap, callBack);
            }
        });
    }

    /**
     * @param context
     * @param url
     * @param paramsMap
     * @param callBack
     */
    public static void okHttpDelete(final Context context, final String url,
                                    final Map<String, String> paramsMap, final CallBackUtil callBack) {
        NetWorkUtils.setNetWorkShow(context, new NetWorkUtils.NetWorkCallBack() {
            @Override
            public void netWorkCallBack() {
                okHttpDeleteHeader(context, ApiConstants.rootUrl + url, paramsMap, callBack);
            }
        });
    }

    /**
     * post请求，上传单个文件
     *
     * @param url      url
     * @param file     File对象
     * @param fileKey  上传参数时file对应的键
     * @param fileType File类型，是image，video，audio，file
     * @param callBack 回调接口，onFailure方法在请求失败时调用，onResponse方法在请求成功后调用，这两个方法都执行在UI线程。还可以重写onProgress方法，得到上传进度
     */
    public static void okHttpPostFile(final Context context,
                                      final String url,
                                      final File file,
                                      final String fileKey,
                                      final String fileType,
                                      final CallBackUtil callBack) {
        NetWorkUtils.setNetWorkShow(context, new NetWorkUtils.NetWorkCallBack() {
            @Override
            public void netWorkCallBack() {
                okHttpPostHeader(context, ApiConstants.rootUrl + url, file, fileKey, fileType, callBack);
            }
        });
    }

    /**
     * post请求，上传单个文件
     *
     * @param url      url
     * @param fileList File集合
     * @param fileKey  上传参数时file对应的键
     * @param fileType File类型，是image，video，audio，file
     * @param callBack 回调接口，onFailure方法在请求失败时调用，onResponse方法在请求成功后调用，这两个方法都执行在UI线程。还可以重写onProgress方法，得到上传进度
     */
    public static void okHttpPostFileList(final Context context,
                                          final String url,
                                          final Map<String, String> paramsMap,
                                          final List<File> fileList,
                                          final String fileKey,
                                          final String fileType,
                                          final CallBackUtil callBack) {
        NetWorkUtils.setNetWorkShow(context, new NetWorkUtils.NetWorkCallBack() {
            @Override
            public void netWorkCallBack() {
                okHttpPostHeader(context, paramsMap, ApiConstants.rootUrl + url, fileList, fileKey, fileType, callBack);
            }
        });
    }

    /**
     * post请求，可以传递参数
     *
     * @param url         url
     * @param paramsMap   map集合，封装键值对参数
     * @param requestTime 发送请求的时间
     * @param callBack    回调接口，onFailure方法在请求失败时调用，onResponse方法在请求成功后调用，这两个方法都执行在UI线程。
     */
    private static void okHttpPostHeader(Context context, String url, Map<String, String> paramsMap, long requestTime, CallBackUtil callBack) {
        // header
        Map<String, String> headerMap = getHeader(context, requestTime);

        String jsonStr = new Gson().toJson(paramsMap);
        LogUtils.info("HttpUtil", "post url = " + url + "\njsonStr = " + jsonStr);
        OkhttpUtil.okHttpPostJson(context, url, jsonStr, headerMap, callBack);
    }

    /**
     * jsonPost
     *
     * @param context
     * @param url
     * @param data
     * @param callBack
     */
    public static void okHttpPostJsonObj(Context context, String url, Object data, CallBackUtil callBack) {
        // header
        Map<String, String> headerMap = getHeader(context, 0);

        String jsonStr = new Gson().toJson(data);
        LogUtils.info("HttpUtil", "jsonStr (post) url = " + url + "\njsonStr = " + jsonStr);
        OkhttpUtil.okHttpPostJson(context, url, jsonStr, headerMap, callBack);
    }

    /**
     * get，可以传递参数
     *
     * @param url       url
     * @param paramsMap map集合，封装键值对参数
     * @param callBack  回调接口，onFailure方法在请求失败时调用，onResponse方法在请求成功后调用，这两个方法都执行在UI线程。
     */
    private static void okHttpGetHeader(Context context, String url, Map<String, String> paramsMap, CallBackUtil callBack) {
        // header
        Map<String, String> headerMap = getHeader(context, 0);

        LogUtils.info("HttpUtil", "jsonStr (get) url = " + url + "\njsonStr = " + paramsMap.toString());
        OkhttpUtil.okHttpGet(context, url, paramsMap, headerMap, callBack);
    }

    /**
     * delete，可以传递参数
     *
     * @param url       url
     * @param paramsMap map集合，封装键值对参数
     * @param callBack  回调接口，onFailure方法在请求失败时调用，onResponse方法在请求成功后调用，这两个方法都执行在UI线程。
     */
    private static void okHttpDeleteHeader(Context context, String url, Map<String, String> paramsMap, CallBackUtil callBack) {
        // header
        Map<String, String> headerMap = getHeader(context, 0);

        LogUtils.info("HttpUtil", "jsonStr (delete) url = " + url + "\njsonStr = " + paramsMap.toString());
        OkhttpUtil.okHttpDelete(context, url, paramsMap, headerMap, callBack);
    }


    /**
     * post请求，上传单个文件
     *
     * @param url      url
     * @param file     File对象
     * @param fileKey  上传参数时file对应的键
     * @param fileType File类型，是image，video，audio，file
     * @param callBack 回调接口，onFailure方法在请求失败时调用，onResponse方法在请求成功后调用，这两个方法都执行在UI线程。还可以重写onProgress方法，得到上传进度
     */
    private static void okHttpPostHeader(Context context, String url, File file, String fileKey, String fileType, CallBackUtil callBack) {
        // header
        Map<String, String> headerMap = getHeader(context, 0);

        LogUtils.info("HttpUtil", "file = " + file + "\nfileKey = " + fileKey);
        OkhttpUtil.okHttpUploadFile(context, url, file, fileKey, fileType, new HashMap<String, String>(), headerMap, callBack);
    }


    /**
     * post请求，上传多个文件
     *
     * @param url      url
     * @param fileList File对象
     * @param fileKey  上传参数时file对应的键
     * @param fileType File类型，是image，video，audio，file
     * @param callBack 回调接口，onFailure方法在请求失败时调用，onResponse方法在请求成功后调用，这两个方法都执行在UI线程。还可以重写onProgress方法，得到上传进度
     */
    private static void okHttpPostHeader(Context context, Map<String, String> paramsMap, String url, List<File> fileList, String fileKey, String fileType, CallBackUtil callBack) {
        // header
        Map<String, String> headerMap = getHeader(context, 0);

        LogUtils.info("HttpUtil", "file = " + fileList + "\nfileKey = " + fileKey + "\nparamsMap = " + paramsMap.toString());
        OkhttpUtil.okHttpUploadListFile(context, url, paramsMap, fileList, fileKey, fileType, headerMap, callBack);
    }

    /**
     * 设置头函数
     *
     * @param context
     * @return
     */
    private static Map<String, String> getHeader(Context context, long requestTime) {
        // 时间戳，获取当前的时间戳
        if (requestTime <= 0) {
            // 没有传递时间戳，则获取当前的时间戳
            requestTime = System.currentTimeMillis();
        }

        String token = SharedPreferencesUtil.getSharedPreferences(context).getString("token", "");
        // 请求头参数
        Map<String, String> headerMap = new HashMap<String, String>();
//        headerMap.put("requestTime", requestTime + "");
        if (!StringUtils.isNullOrBlank(token)) { // token 不为空
            headerMap.put("Token", token);
        }

        return headerMap;
    }

    /**
     * @param context
     * @param url
     * @param object
     * @param callBack
     */
    public static void okHttpPostObjJson_2(final Context context, final String url,
                                           Object object, final CallBackUtil callBack) {
        NetWorkUtils.setNetWorkShow(context, new NetWorkUtils.NetWorkCallBack() {
            @Override
            public void netWorkCallBack() {
                okHttpPostJsonObj_2(context, ApiConstants.rootUrl_2 + url, object, callBack);
            }
        });
    }

    /**
     * jsonPost
     *
     * @param context
     * @param url
     * @param data
     * @param callBack
     */
    private static void okHttpPostJsonObj_2(Context context, String url, Object data, CallBackUtil callBack) {
        // header
        Map<String, String> headerMap = getHeader_2(context);

        String jsonStr = new Gson().toJson(data);
        LogUtils.info("HttpUtil", "jsonStr (post) url = " + url + "\njsonStr = " + jsonStr);
        OkhttpUtil.okHttpPostJson(context, url, jsonStr, headerMap, callBack);
    }

    /**
     * @param context
     * @param url
     * @param paramsMap
     * @param callBack
     */
    public static void okHttpGet_2(final Context context, final String url,
                                   final Map<String, String> paramsMap, final CallBackUtil callBack) {
        NetWorkUtils.setNetWorkShow(context, new NetWorkUtils.NetWorkCallBack() {
            @Override
            public void netWorkCallBack() {
                okHttpGetHeader_2(context, ApiConstants.rootUrl_2 + url, paramsMap, callBack);
            }
        });
    }

    /**
     * get，可以传递参数
     *
     * @param url       url
     * @param paramsMap map集合，封装键值对参数
     * @param callBack  回调接口，onFailure方法在请求失败时调用，onResponse方法在请求成功后调用，这两个方法都执行在UI线程。
     */
    private static void okHttpGetHeader_2(Context context, String url, Map<String, String> paramsMap, CallBackUtil callBack) {
        LogUtils.info("HttpUtil", "jsonStr (get) url = " + url + "\njsonStr = " + paramsMap.toString());
        OkhttpUtil.okHttpGet(context, url, paramsMap, new HashMap<>(), callBack);
    }

    /**
     * post请求，上传单个文件
     *
     * @param url      url
     * @param file     File对象
     * @param fileKey  上传参数时file对应的键
     * @param fileType File类型，是image，video，audio，file
     * @param callBack 回调接口，onFailure方法在请求失败时调用，onResponse方法在请求成功后调用，这两个方法都执行在UI线程。还可以重写onProgress方法，得到上传进度
     */
    public static void okHttpPostFile_2(final Context context,
                                      final String url,
                                      final File file,
                                      final String fileKey,
                                      final String fileType,
                                      final CallBackUtil callBack) {
        NetWorkUtils.setNetWorkShow(context, new NetWorkUtils.NetWorkCallBack() {
            @Override
            public void netWorkCallBack() {
                okHttpPostHeader_2(context, ApiConstants.rootUrl_2 + url, file, fileKey, fileType, callBack);
            }
        });
    }

    /**
     * post请求，上传单个文件
     *
     * @param url      url
     * @param file     File对象
     * @param fileKey  上传参数时file对应的键
     * @param fileType File类型，是image，video，audio，file
     * @param callBack 回调接口，onFailure方法在请求失败时调用，onResponse方法在请求成功后调用，这两个方法都执行在UI线程。还可以重写onProgress方法，得到上传进度
     */
    private static void okHttpPostHeader_2(Context context, String url, File file, String fileKey, String fileType, CallBackUtil callBack) {
        // header
        Map<String, String> headerMap = getHeader_2(context);

        LogUtils.info("HttpUtil", "file = " + file + "\nfileKey = " + fileKey);
        OkhttpUtil.okHttpUploadFile(context, url, file, fileKey, fileType, new HashMap<String, String>(), headerMap, callBack);
    }


    /**
     * 设置头函数
     *
     * @param context
     * @return
     */
    private static Map<String, String> getHeader_2(Context context) {
        String token = SharedPreferencesUtil.getSharedPreferences(context).getString("token", "");
        // 请求头参数
        Map<String, String> headerMap = new HashMap<String, String>();
        if (!StringUtils.isNullOrBlank(token)) { // token 不为空
            headerMap.put("Authorization", "Bearer " + token);
        }

        return headerMap;
    }


}
