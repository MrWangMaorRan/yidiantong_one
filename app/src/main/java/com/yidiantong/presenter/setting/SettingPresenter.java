package com.yidiantong.presenter.setting;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.yidiantong.R;
import com.yidiantong.StartActivity;
import com.yidiantong.bean.CheckVersionBean;
import com.yidiantong.model.biz.setting.ISetting;
import com.yidiantong.model.impl.setting.SettingImpl;
import com.yidiantong.util.AppUpdateUtils;
import com.yidiantong.util.SharedPreferencesUtil;
import com.yidiantong.util.ToastUtils;
import com.yidiantong.view.GuideActivity;

public class SettingPresenter implements SettingImpl.OnLoginOutListener, SettingImpl.OnCallBackListener {

    private Context mContext;
    private SettingImpl settingImpl;
    private ISetting iSetting;

    public SettingPresenter(Context mContext, ISetting iSetting) {
        this.mContext = mContext;
        this.iSetting = iSetting;
        settingImpl = new SettingImpl();
    }

    /**
     * 退出登录
     */
    public void goToLoginStart() {
        Intent intent = new Intent(mContext, StartActivity.class);
        mContext.startActivity(intent);
    }

    // 显示当前版本号
    public void showNowVersion(TextView tvVersionUpdate) {
        String versionName = AppUpdateUtils.getVersionName(mContext);
        tvVersionUpdate.setText("V" + versionName);
    }

    // sip登录状态
    public void setSipLoginState(ImageView ivSipLoginState, TextView tvSipLoginState, boolean isLogin) {
        if (isLogin) {
            ivSipLoginState.setImageDrawable(mContext.getResources().getDrawable(R.drawable.bg_green_point));
            tvSipLoginState.setText("在线");
        } else {
            ivSipLoginState.setImageDrawable(mContext.getResources().getDrawable(R.drawable.bg_red_point));
            tvSipLoginState.setText("不在线");
        }
    }

    /**
     * 退出登录
     */
    public void loginOut() {
        settingImpl.loginOut(mContext, this);

    }

    /**
     * 版本检测
     */
    public void checkVersion() {
        settingImpl.checkVersion(mContext, this);
    }

    @Override
    public void onLoginOutSuccess(String msg) {
        iSetting.logout();
//        ToastUtils.showToast(mContext, "退出成功");
    }

    @Override
    public void onLoginOutFailure(String msg) {
//        ToastUtils.showToast(mContext, "退出失败");
        iSetting.logout();
    }

    @Override
    public void onCheckVersionSuccess(CheckVersionBean checkVersionBean) {
        if (AppUpdateUtils.getVersionName(mContext).equals(checkVersionBean.getVersion())) {
            iSetting.latestVersion();
        } else {
            iSetting.oldVersion();
        }
    }

    @Override
    public void onCheckVersionFailure(String msg) {
        iSetting.oldVersion();
    }
}
