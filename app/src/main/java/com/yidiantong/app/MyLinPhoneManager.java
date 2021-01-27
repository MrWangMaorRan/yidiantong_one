package com.yidiantong.app;

import android.content.Context;

import com.yidiantong.base.Constants;
import com.yidiantong.util.SharedPreferencesUtil;
import com.yidiantong.util.StringUtils;
import com.yidiantong.util.Utils;
import com.yidiantong.util.log.LogUtils;
import com.yzx.yzxsip.YzxSipSdk;
import com.yzx.yzxsip.callback.PhoneCallback;
import com.yzx.yzxsip.callback.RegistrationCallback;
import com.yzx.yzxsip.callback.SdkCallBack;

import org.greenrobot.eventbus.EventBus;
import org.linphone.core.Call;

public class MyLinPhoneManager {
    private String TAG = "LinPhoneManager";
    private static MyLinPhoneManager linPhoneManager;
    private int failureCount = 0;
    private int maxFailureCount = 5;
    private Context mContext;

    public MyLinPhoneManager(Context mContext) {
        this.mContext = mContext;
    }

    public static MyLinPhoneManager getInstance(Context mContext) {
        if (linPhoneManager == null) {
            synchronized (MyLinPhoneManager.class) {
                if (linPhoneManager == null) {
                    linPhoneManager = new MyLinPhoneManager(mContext);
                }
            }
        }
        return linPhoneManager;
    }

    /**
     * 初始化LinPhone
     */
    public void init() {
        if (Utils.isAXB(mContext) && !Constants.sipIsLogin) {
            Constants.sipIsInit = true;
            YzxSipSdk.init(mContext, SipNotification.createForegroundNotification(mContext));
            YzxSipSdk.addCallback(new RegistrationCallback() {
                @Override
                public void registrationOk() {
                    super.registrationOk();
                    LogUtils.e(TAG, "登录成功");
                    Constants.sipIsLogin = true;
                    // 重置登录失败次数
                    failureCount = 0;
                    LogUtils.e(TAG, "登录成功，登录状态：" + Constants.sipIsLogin + "，登录失败次数：" + failureCount);
                    if (!SharedPreferencesUtil.getSharedPreferences(mContext).getBoolean("isLogin", false)) {
                        //发送广播
                        EventBus.getDefault().post(Constants.LOGIN_SUCCESS);
                    }
                }

                @Override
                public void registrationFailed() {
                    super.registrationFailed();
                    LogUtils.e(TAG, "登录失败");
                    Constants.sipIsLogin = false;
                    // 失败次数叠加
                    failureCount++;
                    if (SharedPreferencesUtil.getSharedPreferences(mContext).getBoolean("isLogin", false)) {
                        //发送广播
                        EventBus.getDefault().post(Constants.LOGIN_FAILURE);
                    }
                    LogUtils.e(TAG, "登录失败，登录状态：" + Constants.sipIsLogin + "，登录失败次数：" + failureCount);
                    // 连续登录失败n次后，关闭sdk，重新打开sdk
                    if (failureCount > maxFailureCount) {
                        LogUtils.e(TAG, "登录失败次数大于" + maxFailureCount + "次，关闭sdk服务，重新初始化");
                        failureCount = 0;
                        unInit();
                    }
                }
            }, new PhoneCallback() {
                @Override
                public void incomingCall(Call call) {
                    super.incomingCall(call);
                    LogUtils.e(TAG, "incomingCall state = " + call.getState());
                    // 视频通话默认免提，语音通话默认非免提
                    speakerphoneOn(true);
                }

                @Override
                public void outgoingInit() {
                    super.outgoingInit();
                    //发送广播
                    EventBus.getDefault().post(Constants.CALL_OUTGOING);
                    LogUtils.e(TAG, "outgoingInit");
                }

                @Override
                public void callConnected() {
                    super.callConnected();
                    LogUtils.e(TAG, "callConnected");
                    //发送广播
                    EventBus.getDefault().post(Constants.CALL_CONNECT);
                    // 视频通话默认免提，语音通话默认非免提
                    speakerphoneOn(false);
                }

                @Override
                public void callEnd() {
                    super.callEnd();
                    LogUtils.e(TAG, "callEnd");
                    //发送广播
                    EventBus.getDefault().post(Constants.CALL_END);
                }

                @Override
                public void callReleased() {
                    super.callReleased();
                }

                @Override
                public void error() {
                    super.error();
                    LogUtils.e(TAG, "error");
                    //发送广播
                    EventBus.getDefault().post(Constants.CALL_ERROR);
                }

                @Override
                public void outgoingEarlyMedia() {
                    super.outgoingEarlyMedia();
                    //发送广播
                    EventBus.getDefault().post(Constants.CALL_EARLY_MEDIA);
                }
            }, new SdkCallBack() {
                @Override
                public void onInitSuccess() {
                    super.onInitSuccess();
                    if (!Constants.sipIsLogin) {
                        login();
                    }
                }

                @Override
                public void onDestroySuccess() {
                    super.onDestroySuccess();
                    removeSdkCallBack();
                    init();
                }
            });
        }
    }


    /**
     * sip注册测试账号
     * 注册地址 siptest.szi5g.com:5060
     * 用户名/密码   038010000000/crm2020.com
     * 用户名/密码   038010000001/crm2020.com
     */
    /**
     * 登录LinPhone
     */
    public void login() {
        if (Utils.isAXB(mContext) && !Constants.sipIsLogin) {
            String account = SharedPreferencesUtil.getSharedPreferences(mContext).getString("sipAccount", "");
            String password = SharedPreferencesUtil.getSharedPreferences(mContext).getString("sipPassword", "");
            String serverIP = "siptest.szi5g.com:2060";
//          String serverIP = "sipproxy.ucpaas.com:25060";

            LogUtils.e(TAG, "sip: account = " + account + ", password = " + password);
            if (StringUtils.isNullOrBlank(account) || StringUtils.isNullOrBlank(password)) {
                return;
            }
            YzxSipSdk.clearData();
            YzxSipSdk.setDomain(serverIP);
            YzxSipSdk.sipLogin(account, password);
        }
    }


    /**
     * 拨号
     *
     * @param phoneNum
     */
    public void call(String phoneNum) {
        if (Utils.isAXB(mContext) && Constants.sipIsLogin) {
//          YzxSipSdk.toggleMicro(false);
            YzxSipSdk.speakerphoneOn(mContext, false);
            YzxSipSdk.callTo(phoneNum, false);
        }
    }


    /**
     * 挂断
     */
    public void hangUp() {
        if (Utils.isAXB(mContext) && Constants.sipIsLogin) {
            //发送广播
            EventBus.getDefault().post(Constants.CALL_HANGUP);
            YzxSipSdk.hangUp();
        }
    }

    /**
     * 销毁
     */
    public void unInit() {
        if (Utils.isAXB(mContext) && Constants.sipIsInit) {
            Constants.sipIsInit = false;
            YzxSipSdk.unInit();
        }
    }

    /**
     * 移除sdk的回调
     */
    public void removeSdkCallBack() {
        if (Utils.isAXB(mContext) && Constants.sipIsInit) {
            YzxSipSdk.removeSdkCallBack();
        }
    }

    /**
     * 是否为扬声器
     *
     * @param isSpeakerOn
     */
    public void speakerphoneOn(boolean isSpeakerOn) {
        if (Utils.isAXB(mContext) && Constants.sipIsInit && Constants.sipIsLogin) {
            YzxSipSdk.speakerphoneOn(mContext, isSpeakerOn);
        }
    }

}
