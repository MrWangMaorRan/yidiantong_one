package com.yidiantong;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.yidiantong.base.BaseActivity;
import com.yidiantong.model.biz.home.IStart;
import com.yidiantong.presenter.home.StartPresenter;
import com.yidiantong.util.SharedPreferencesUtil;
import com.yidiantong.util.ToastUtils;
import com.yidiantong.util.Utils;
import com.yzx.yzxsip.YzxSipSdk;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StartActivity extends BaseActivity implements IStart {

    @BindView(R.id.iv_checked_agree)
    ImageView ivCheckedAgree;
    @BindView(R.id.ll_bottom_layout)
    LinearLayout llBottomLayout;
    @BindView(R.id.btn_login)
    Button btnLogin;

    private StartPresenter startPresenter;
    private boolean isAgree = true;

    @Override
    public void getIntentData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_start;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        SharedPreferencesUtil.getSharedPreferences(mContext).clearAll();
        startPresenter = new StartPresenter(this, this);
        startPresenter.checkedAgreeAgreement(ivCheckedAgree, isAgree);
        Utils.getPermission(this);

//        // 计算缓存大小
//        long fileSize = 0;
//        String cacheSize = "0KB";
//        File filesDir = getFilesDir();// /data/data/package_name/files
//        File cacheDir = getCacheDir();// /data/data/package_name/cache
//
//        fileSize += getDirSize(filesDir);
//        fileSize += getDirSize(cacheDir);
//
//// 2.2版本才有将应用缓存转移到sd卡的功能
//
//        if(isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)){
//
//            File externalCacheDir = getExternalCacheDir();//"<sdcard>/Android/data/<package_name>/cache/"
//            fileSize += getDirSize(externalCacheDir);
//
//        }
//
//        if (fileSize > 0)
//            cacheSize = formatFileSize(fileSize);
//    }
    }
    @OnClick({R.id.ll_bottom_layout, R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_bottom_layout:
                isAgree = !isAgree;
                startPresenter.checkedAgreeAgreement(ivCheckedAgree, isAgree);
                break;
            case R.id.btn_login:
                if (isAgree) {
                    startPresenter.goToLogin();
                } else {
                    ToastUtils.showToast(this, "请阅读并同意一点通用户协议");
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_OK) {
            switch (requestCode) {
                case StartPresenter.LOGIN_REQUEST_CODE:
                    this.finish();
                    break;
            }
        }
    }



//    /**
//     * 获取目录文件大小
//     *
//     * @param dir
//     * @return
//     */
//    public static long getDirSize(File dir) {
//        if (dir == null) {
//            return 0;
//        }
//        if (!dir.isDirectory()) {
//            return 0;
//        }
//        long dirSize = 0;
//        File[] files = dir.listFiles();
//        for (File file : files) {
//            if (file.isFile()) {
//                dirSize += file.length();
//            } else if (file.isDirectory()) {
//                dirSize += file.length();
//                dirSize += getDirSize(file); // 递归调用继续统计
//            }
//        }
//        return dirSize;
//    }
//
//    /**
//     * 判断当前版本是否兼容目标版本的方法
//     * @param VersionCode
//     * @return
//     */
//    public static boolean isMethodsCompat(int VersionCode) {
//        int currentVersion = android.os.Build.VERSION.SDK_INT;
//        return currentVersion >= VersionCode;
//    }
//
//    @TargetApi(8)
//    public static File getExternalCacheDir(Context context) {
//
//        // return context.getExternalCacheDir(); API level 8
//
//        // e.g. "<sdcard>/Android/data/<package_name>/cache/"
//
//        return context.getExternalCacheDir();
//    }
//
//    /**
//     * 转换文件大小
//     *
//     * @param fileS
//     * @return B/KB/MB/GB
//     */
//    public static String formatFileSize(long fileS) {
//        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
//        String fileSizeString = "";
//        if (fileS < 1024) {
//            fileSizeString = df.format((double) fileS) + "B";
//        } else if (fileS < 1048576) {
//            fileSizeString = df.format((double) fileS / 1024) + "KB";
//        } else if (fileS < 1073741824) {
//            fileSizeString = df.format((double) fileS / 1048576) + "MB";
//        } else {
//            fileSizeString = df.format((double) fileS / 1073741824) + "G";
//        }
//        return fileSizeString;
//    }
//
//    /**
//     * 清除app缓存
//     *
//     * @param activity
//     */
//    public static void clearAppCache(Activity activity) {
//          Application ac = activity.getApplication();
//        final Handler handler = new Handler() {
//            public void handleMessage(Message msg) {
//                if (msg.what == 1) {
//                    Log.i("缓存清除成功","缓存清除成功");
//                   // ToastMessage(ac, "缓存清除成功");
//                } else {
//                    Log.i("缓存清除失败","缓存清除失败");
//                    //ToastMessage(ac, "缓存清除失败");
//                }
//            }
//        };
//        new Thread() {
//            public void run() {
//                Message msg = new Message();
//
//                try {
//                    ac.clearAppCache();
//                    msg.what = 1;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    msg.what = -1;
//                }
//                handler.sendMessage(msg);
//            }
//        }.start();
//    }
//
//
//
////    /**
////     * 清除app缓存
////     */
////    public void clearAppCache()
////    {
////        //清除webview缓存
////        @SuppressWarnings("deprecation")
////        File file = CacheManager.getCacheFileBaseDir();
////
////        //先删除WebViewCache目录下的文件
////
////        if (file != null && file.exists() && file.isDirectory()) {
////            for (File item : file.listFiles()) {
////                item.delete();
////            }
////            file.delete();
////        }
////        deleteDatabase("webview.db");
////        deleteDatabase("webview.db-shm");
////        deleteDatabase("webview.db-wal");
////        deleteDatabase("webviewCache.db");
////        deleteDatabase("webviewCache.db-shm");
////        deleteDatabase("webviewCache.db-wal");
////        //清除数据缓存
////        clearCacheFolder(getFilesDir(),System.currentTimeMillis());
////        clearCacheFolder(getCacheDir(),System.currentTimeMillis());
////        //2.2版本才有将应用缓存转移到sd卡的功能
////        if(isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)){
////            clearCacheFolder(getExternalCacheDir(this),System.currentTimeMillis());
////        }
////
////    }
//
//    /**
//     * 清除缓存目录
//     * @param dir 目录
//     *
//     * @return
//     */
//    private int clearCacheFolder(File dir, long curTime) {
//        int deletedFiles = 0;
//        if (dir!= null && dir.isDirectory()) {
//            try {
//                for (File child:dir.listFiles()) {
//                    if (child.isDirectory()) {
//                        deletedFiles += clearCacheFolder(child, curTime);
//                    }
//                    if (child.lastModified() < curTime) {
//                        if (child.delete()) {
//                            deletedFiles++;
//                        }
//                    }
//                }
//            } catch(Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return deletedFiles;
//    }


}