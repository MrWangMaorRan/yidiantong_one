/*
 *
 *  * Copyright (C) 2018 Antonio Leiva Gordillo.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.yidiantong.presenter.login;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yidiantong.MainActivity;
import com.yidiantong.R;
import com.yidiantong.app.MyLinPhoneManager;
import com.yidiantong.bean.LoginBean;
import com.yidiantong.bean.UserInfoBean;
import com.yidiantong.model.biz.login.ILogin;
import com.yidiantong.model.impl.login.LoginImpl;
import com.yidiantong.util.SharedPreferencesUtil;
import com.yidiantong.util.SpUtils;
import com.yidiantong.util.StringUtils;
import com.yidiantong.util.TimerCallBackUtils;
import com.yidiantong.util.ToastUtils;

import java.util.HashMap;
import java.util.Map;

public class LoginPresenter implements LoginImpl.OnCallBackListener {

    private ILogin iLogin;
    private LoginImpl loginImpl;
    private Context mContext;
    private long millisInFuture = 60000;  // 倒计时60s
    private long countDownInterval = 1000; // 倒计时间隔1s
    private TimerCallBackUtils timerCallBackUtils;

    public LoginPresenter(Context mContext, ILogin iLogin) {
        this.mContext = mContext;
        this.iLogin = iLogin;
        loginImpl = new LoginImpl();
    }

    // 输入错误
    public void phoneNumErrShow(EditText etPhoneNum, TextView tvPhoneNumErr) {
        String phoneNum = etPhoneNum.getText().toString().trim();
        if (StringUtils.isNullOrBlank(phoneNum) || !StringUtils.isPhoneType(phoneNum)) {
            tvPhoneNumErr.setVisibility(View.VISIBLE);
        }
    }

    // 输入错误
    public void verificationCodeErrShow(EditText etVerificationCode, TextView tvVerificationCodeErr) {
        String verificationCode = etVerificationCode.getText().toString().trim();
        if (StringUtils.isNullOrBlank(verificationCode)) {
            tvVerificationCodeErr.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 发送验证码
     */
    public void sendVerificationCode(TextView tvSendVerificationCode, EditText etPhoneNum, TimerCallBackUtils.TimerCallBack timerCallBack) {
        tvSendVerificationCode.setEnabled(false);
        tvSendVerificationCode.setTextColor(mContext.getResources().getColor(R.color.gray_c3c5cb));
        String phoneNumCode = etPhoneNum.getText().toString().trim();
        if (StringUtils.isNullOrBlank(phoneNumCode) || !StringUtils.isPhoneType(phoneNumCode)) {
            ToastUtils.showToast(mContext, "请输入正确的手机号");
            tvSendVerificationCode.setEnabled(true);
            tvSendVerificationCode.setTextColor(mContext.getResources().getColor(R.color.blue_3f74fd));
            tvSendVerificationCode.setText("发送验证码");
            return;
        }
        Map<String, String> mapSendCode = new HashMap<>();
        mapSendCode.put("phoneNumber", phoneNumCode);
        loginImpl.sendCode(mContext, mapSendCode, this);
        // 倒计时
        if (timerCallBackUtils == null) {
            timerCallBackUtils = new TimerCallBackUtils(millisInFuture, countDownInterval, timerCallBack);
        }
        timerCallBackUtils.start();
    }

    /**
     * 登录
     */
    public void login(EditText etPhoneNum, EditText etVerificationCode) {
        String phoneNum = etPhoneNum.getText().toString().trim();
        String verificationCode = etVerificationCode.getText().toString().trim();
        if (StringUtils.isNullOrBlank(phoneNum) || !StringUtils.isPhoneType(phoneNum)) {
            ToastUtils.showToast(mContext, "请输入正确的手机号");
            return;
        }
        if (StringUtils.isNullOrBlank(verificationCode)) {
            ToastUtils.showToast(mContext, "请输入验证码");
            return;
        }
        Map<String, String> mapLogin = new HashMap<>();
        mapLogin.put("phoneNumber", phoneNum);
        mapLogin.put("captcha", verificationCode);
        loginImpl.login(mContext, mapLogin, this);
    }

//    /**
//     * 获取个人信息（登陆逻辑改动2020-07-29）
//     */
//    public void getUserInfo() {
//        loginImpl.getUserInfo(mContext, new HashMap<>(), this);
//    }

    /**
     * 跳转到首页
     */
    public void goToMainActivity() {
        Intent intent = new Intent(mContext, MainActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * 销毁
     */
    public void onDestroy() {
        loginImpl = null;
    }

    @Override
    public void onLoginFailure(String data) {
        iLogin.loginFailure("");
    }

//     登陆逻辑改动2020-07-29
//    @Override
//    public void onGetUserInfoSuccess(UserInfoBean userInfoBean) {
//        if (userInfoBean != null && userInfoBean.getSip() != null) {
//            MyLinPhoneManager.getInstance().login(userInfoBean.getSip().getUsername(),
//                    userInfoBean.getSip().getPassword());
//        } else {
//            iLogin.loginFailure("");
//        }
//    }
//
//    @Override
//    public void onGetUserInfoFailure(String message) {
//        iLogin.loginFailure("");
//    }

    @Override
    public void onLoginSuccess(LoginBean loginBean) {
        if (loginBean != null) {
            SharedPreferencesUtil.getSharedPreferences(mContext).putString("token", loginBean.getToken());
            SpUtils.getInstance().setValue("token",loginBean.getToken());
            SharedPreferencesUtil.getSharedPreferences(mContext).putString("callType", loginBean.getCallType());
//            getUserInfo(); // 登陆逻辑改动（2020-07-29）
            SharedPreferencesUtil.getSharedPreferences(mContext).putBoolean("isLogin", true);
            iLogin.loginSuccess();
        }
    }

    @Override
    public void onSendCodeSuccess(String data) {
        iLogin.sendCodeSuccess(data);
    }

    @Override
    public void onSendCodeFailure(String data) {
        if (timerCallBackUtils != null) {
            timerCallBackUtils.cancel();
        }
        iLogin.sendCodeFailure(data);
    }
}
