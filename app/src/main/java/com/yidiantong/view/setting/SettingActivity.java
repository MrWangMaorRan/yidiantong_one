package com.yidiantong.view.setting;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yidiantong.R;
import com.yidiantong.app.MyLinPhoneManager;
import com.yidiantong.base.AppManager;
import com.yidiantong.base.BaseActivity;
import com.yidiantong.base.Constants;
import com.yidiantong.model.biz.setting.ISetting;
import com.yidiantong.presenter.setting.SettingPresenter;
import com.yidiantong.util.HandlerUtils;
import com.yidiantong.util.SharedPreferencesUtil;
import com.yzx.yzxsip.YzxSipSdk;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity implements ISetting {

    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_right)
    Button btnRight;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.rl_checked_version)
    RelativeLayout rlCheckedVersion;
    @BindView(R.id.tv_version_update_title)
    TextView tvVersionUpdateTitle;
    @BindView(R.id.tv_version_update)
    TextView tvVersionUpdate;
    @BindView(R.id.btn_login_out)
    Button btnLoginOut;
    @BindView(R.id.tv_sip_login_state)
    TextView tvSipLoginState;
    @BindView(R.id.iv_sip_login_state)
    ImageView ivSipLoginState;
    @BindView(R.id.rl_sip_login_state)
    RelativeLayout rlSipLoginState;


    private SettingPresenter settingPresenter;
    private String callType;

    @Override
    public void getIntentData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        settingPresenter = new SettingPresenter(this, this);
        // 呼叫类型
        callType = SharedPreferencesUtil.getSharedPreferences(mContext).getString("callType", "");
        // 初始化UI
        tvTitle.setText("设置");
        settingPresenter.showNowVersion(tvVersionUpdate);
        settingPresenter.setSipLoginState(ivSipLoginState, tvSipLoginState, Constants.sipIsLogin);
        // 注册广播接收器
        EventBus.getDefault().register(this);
    }


    @OnClick({R.id.iv_left, R.id.rl_checked_version, R.id.btn_login_out, R.id.rl_sip_login_state})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.rl_checked_version:
                settingPresenter.checkVersion();
                break;
            case R.id.btn_login_out:
                YzxSipSdk.clearData();
                settingPresenter.loginOut();
                break;
            case R.id.rl_sip_login_state:
                if (!Constants.sipIsLogin) {
                    rlSipLoginState.setEnabled(false);
                    MyLinPhoneManager.getInstance(mContext).login();
                }
                break;
        }
    }

    @Override
    public void latestVersion() {
        // 已经是最新版本
        HandlerUtils.setText(tvVersionUpdate, "已是最新版本");
    }

    @Override
    public void oldVersion() {
        // 有新版本可以更新
        HandlerUtils.setText(tvVersionUpdate, "");
    }

    @Override
    public void logout() {
        YzxSipSdk.clearData();
        Constants.sipIsLogin=false;
        SharedPreferencesUtil.getSharedPreferences(mContext).clearAll();
        AppManager.finishAllActivity();

        settingPresenter.goToLoginStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String message) {
        if (message != null) {
            switch (message) {
                case Constants.LOGIN_FAILURE: // 登陆广播
                case Constants.LOGIN_SUCCESS:
                    rlSipLoginState.setEnabled(true);
                    settingPresenter.setSipLoginState(ivSipLoginState, tvSipLoginState, Constants.sipIsLogin);
                    break;
            }
        }
    }


}