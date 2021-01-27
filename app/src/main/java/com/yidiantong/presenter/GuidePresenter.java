package com.yidiantong.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.yidiantong.MainActivity;
import com.yidiantong.StartActivity;
import com.yidiantong.model.biz.IGudie;
import com.yidiantong.model.impl.GuideImpl;
import com.yidiantong.util.SharedPreferencesUtil;
import com.yidiantong.util.log.LogUtils;

public class GuidePresenter {

    private Context mContext;
    private GuideImpl guideImpl;
    private IGudie iGudie;

    public GuidePresenter(Context mContext, IGudie iGudie) {
        this.mContext = mContext;
        this.iGudie = iGudie;
    }

    /**
     * 打开APP，根据isLogin，判断是否已经登录状态。
     */
    public void startActivity() {
        boolean isLogin = SharedPreferencesUtil.getSharedPreferences(mContext).getBoolean("isLogin", false);
        Intent intent;
        if (isLogin) {
            LogUtils.i("GuidePresenter", "login token = " + SharedPreferencesUtil.getSharedPreferences(mContext).getString("token", ""));
            intent = new Intent(mContext, MainActivity.class);
        } else {
            intent = new Intent(mContext, StartActivity.class);
        }
        mContext.startActivity(intent);
        ((Activity) mContext).finish();
    }
}
