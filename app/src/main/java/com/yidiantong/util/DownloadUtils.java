package com.yidiantong.util;

import android.app.ProgressDialog;
import android.util.Log;
import android.widget.ProgressBar;

import com.yidiantong.api.Backpro;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadUtils {
    String TAG = "DownloadUtils";
    String mSavePath;
    /*
    * String mUrl 下载网址
    * String mSavePath  本地保存的路径
    * */
    public void downloadOk(String mUrl,String SavePath, Backpro back) {
        mSavePath = SavePath;

        //client.newCall().enqueue();
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .get()
                .addHeader("Accept-Encoding", "identity")
                .url(mUrl)
                .build();
        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d(TAG, "onFailure: "+e.toString());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.d(TAG, "onResponse: ");
                        //下载文件:将获取到的io流(输入流) 写到本地文件中即可,注意权限
                        save(response.body().byteStream(),response.body().contentLength(),back);
                    }
                });
    }

    private void save(InputStream inputStream, long max, Backpro back) {
        byte[] bytes = new byte[4096];
        int len;
        int progress = 0;
        try {
            FileOutputStream fos = new FileOutputStream(mSavePath);
            while (((len = inputStream.read(bytes)) != -1)) {
                //len != -1 代表流中还有数据
                fos.write(bytes, 0, len);
                //更新进度
                //progress += len;
                progress = progress+len;
                back.getpro(max,progress);
                Log.d(TAG, "max:"+max+",progress:"+progress);
                //updateProgress(progress,max);
            }
            Log.d(TAG, "save: 完成了");
            back.getpro(666,progress);
            //关流
            inputStream.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    private void updateProgress(int progress, long max) {
//        mPb.setMax((int) max);
//        mPb.setProgress(progress);
//    }
}
