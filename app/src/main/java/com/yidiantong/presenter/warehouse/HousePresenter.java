package com.yidiantong.presenter.warehouse;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.yidiantong.bean.UpLoadFileBean;
import com.yidiantong.bean.UserInfoBean;

import com.yidiantong.bean.WeiXinBean;
import com.yidiantong.httpUtils.OkhttpUtil;
import com.yidiantong.model.biz.warehouse.House;
import com.yidiantong.model.impl.warehouse.Houseimpl;
import com.yidiantong.util.FileUtils;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HousePresenter implements Houseimpl.OnCallBackListener {


    private  House house;
    private  Context mContext;
    private final Houseimpl houseimpl;
    private WeiXinBean upLoadFileBean1;
    private WeiXinBean upLoadFileBean2;

    public HousePresenter(Context mContext, House house){
        this.mContext=mContext;
        this.house=house;
        houseimpl = new Houseimpl();

        upWeinxin();
    }
    public  void upWeinxin(){
        houseimpl.getWeixin(mContext,null,"file", OkhttpUtil.FILE_TYPE_IMAGE,this);
    }
    @Override
    public void onWeiXinFileSuccess(WeiXinBean upLoadFileBean) {
//        upLoadFileBean1 = upLoadFileBean;
//        house.onWeinxin(upLoadFileBean);
        this.upLoadFileBean2 = upLoadFileBean;
            house.onWeinxin();

    }

    @Override
    public void onWeiXinFileFaulure(String msg) {

    }
    public void uploadFile(String path_one) {
//        File file1 = new File(path_one);
//        Log.i("真实文件",path_one);
//        OkHttpClient client = new OkHttpClient();
//        MediaType contentType = MediaType.parse("multipart/form-data"); // 上传文件的Content-Type
//        RequestBody body = RequestBody.create( contentType,path_one); // 上传文件的请求体
//        Request request = new Request.Builder()
//                .url("http://139.196.56.167:10090/api/phone/uploadAvatar") // 上传地址
//                .post(body)
//                .build();
//        Call call = client.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                // 文件上传成功
//                if (response.isSuccessful()) {
//                    Log.i("成功one", "onResponse: " + response.body().string());
//
//                } else {
//                    Log.i("成功", "onResponse: " + response.message());
//                }
//            }
//
//            @Override
//            public void onFailure(Call call, IOException e) {
//                // 文件上传失败
//                Log.i("失败上传", "onFailure: " + e.getMessage());
//            }
//        });
    }

}
