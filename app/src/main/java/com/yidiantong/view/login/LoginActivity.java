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

package com.yidiantong.view.login;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tencent.bugly.crashreport.CrashReport;
import com.yidiantong.R;
import com.yidiantong.base.BaseActivity;
import com.yidiantong.model.biz.login.ILogin;
import com.yidiantong.presenter.login.LoginPresenter;
import com.yidiantong.util.SharedPreferencesUtil;
import com.yidiantong.util.StringUtils;
import com.yidiantong.util.TimerCallBackUtils;
import com.yidiantong.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements ILogin {

    @BindView(R.id.et_phone_num)
    EditText etPhoneNum;
    @BindView(R.id.tv_send_verification_code)
    TextView tvSendVerificationCode;
    @BindView(R.id.et_verification_code)
    EditText etVerificationCode;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_phone_err)
    TextView tvPhoneErr;
    @BindView(R.id.tv_verification_code_err)
    TextView tvVerificationCodeErr;
    private LoginPresenter presenter;

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x01: // 倒计时
                    Bundle bundle = msg.getData();
                    long time = bundle.getLong("time");
                    tvSendVerificationCode.setText(time + "S 重新发送");
                    break;
                case 0x02: // 倒计时结束
                    tvSendVerificationCode.setEnabled(true);
                    tvSendVerificationCode.setTextColor(getResources().getColor(R.color.blue_3f74fd));
                    tvSendVerificationCode.setText("发送验证码");
                    break;
            }
        }
    };
    private Button mBt;

    @Override
    public void getIntentData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        presenter = new LoginPresenter(this, this);

        etPhoneNum.setOnFocusChangeListener(onPhoneNumFocusChangeListener);
        etPhoneNum.addTextChangedListener(phoneNumTextWatcher);
        etVerificationCode.setOnFocusChangeListener(onVerificationFocusChangeListener);
        etVerificationCode.addTextChangedListener(verificationCodeTextWatcher);

//        // 注册广播接收器 (登陆逻辑改动（2020-07-29）)
//        EventBus.getDefault().register(this);
    }

    @OnClick({R.id.tv_send_verification_code, R.id.btn_login})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.tv_send_verification_code:
                presenter.phoneNumErrShow(etPhoneNum, tvPhoneErr);
                presenter.sendVerificationCode(tvSendVerificationCode, etPhoneNum, timerCallBack);
                break;
            case R.id.btn_login:

                // SharedPreferencesUtil.getSharedPreferences(this).clearAll();
                presenter.phoneNumErrShow(etPhoneNum, tvPhoneErr);
                presenter.verificationCodeErrShow(etVerificationCode, tvVerificationCodeErr);
                presenter.login(etPhoneNum, etVerificationCode);

                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        presenter.onDestroy();

        // 登陆逻辑改动（2020-07-29）
//        EventBus.getDefault().unregister(this);
    }

    @Override
    public void loginSuccess() {
        SharedPreferencesUtil.getSharedPreferences(mContext).putBoolean("isLogin", true);
        ToastUtils.showToast(this, "登录成功");
        presenter.goToMainActivity();
        setResult(RESULT_OK);
        this.finish();
    }

    @Override
    public void loginFailure(String data) {
        String phone = etPhoneNum.getText().toString().trim();
        if (StringUtils.isNullOrBlank(phone) || !StringUtils.isPhoneType(phone)) {
            tvPhoneErr.setVisibility(View.VISIBLE);
        }
        String vCode = etVerificationCode.getText().toString().trim();
        if (StringUtils.isNullOrBlank(vCode)) {
            tvVerificationCodeErr.setVisibility(View.VISIBLE);
        }
        ToastUtils.showToast(this, "登录失败");
    }

    @Override
    public void sendCodeSuccess(String data) {
        ToastUtils.showToast(this, "发送成功");
    }

    @Override
    public void sendCodeFailure(String data) {
        mHandler.sendEmptyMessage(0x02);
        ToastUtils.showToast(this, "发送失败");
    }

//     登陆逻辑改动（2020-07-29）
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEvent(String message) {
//        switch (message) {
//            case Constants.LOGIN_SUCCESS:
//                String token = SharedPreferencesUtil.getSharedPreferences(mContext).getString("token", "");
//                if (!StringUtils.isNullOrBlank(token)) {
//                    SharedPreferencesUtil.getSharedPreferences(mContext).putBoolean("isLogin", true);
//                    loginSuccess();
//                }
//                break;
//        }
//    }

    /**
     * 倒计时回调
     */
    TimerCallBackUtils.TimerCallBack timerCallBack = new TimerCallBackUtils.TimerCallBack() {
        @Override
        public void onTickCallBack(long millisUntilFinished) {
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putLong("time", millisUntilFinished / 1000);
            message.setData(bundle);
            message.what = 0x01;
            mHandler.sendMessage(message);
        }

        @Override
        public void onFinishCallBack() {
            mHandler.sendEmptyMessage(0x02);
        }
    };

    // 拨号焦点监听
    View.OnFocusChangeListener onPhoneNumFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                tvPhoneErr.setVisibility(View.GONE);
            } else {
                presenter.phoneNumErrShow(etPhoneNum, tvPhoneErr);
            }
        }
    };

    // 验证码焦点监听
    View.OnFocusChangeListener onVerificationFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                tvVerificationCodeErr.setVisibility(View.GONE);
            } else {
                presenter.verificationCodeErrShow(etVerificationCode, tvVerificationCodeErr);
            }
        }
    };

    // 监听
    TextWatcher phoneNumTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!StringUtils.isNullOrBlank(s.toString()) && StringUtils.isPhoneType(s.toString())) {
                tvPhoneErr.setVisibility(View.GONE);
            }
        }
    };

    // 监听
    TextWatcher verificationCodeTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            tvVerificationCodeErr.setVisibility(View.GONE);
        }
    };
}
