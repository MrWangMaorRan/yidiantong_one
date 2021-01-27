package com.yidiantong.view.home;

import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yidiantong.R;
import com.yidiantong.app.MyLinPhoneManager;
import com.yidiantong.base.BaseActivity;
import com.yidiantong.base.Constants;
import com.yidiantong.bean.CluesListBean;
import com.yidiantong.model.biz.home.ICalling;
import com.yidiantong.presenter.home.CallingPresenter;
import com.yidiantong.util.AudioPlayerUtils;
import com.yidiantong.util.HandlerUtils;
import com.yidiantong.util.SharedPreferencesUtil;
import com.yidiantong.util.StringUtils;
import com.yidiantong.util.TimeUtils;
import com.yidiantong.util.ToastUtils;
import com.yidiantong.util.Utils;
import com.yidiantong.widget.FlowLayout;
import com.yidiantong.widget.ScrollViewWithGridView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CallingActivity extends BaseActivity implements ICalling {

    @BindView(R.id.tv_industry)
    TextView tvIndustry;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_phone_num)
    TextView tvPhoneNum;
    @BindView(R.id.tv_calling_time)
    TextView tvCallingTime;
    @BindView(R.id.svgv_customer_property)
    ScrollViewWithGridView svgvCustomerProperty;
    @BindView(R.id.svgv_customer_intention)
    ScrollViewWithGridView svgvCustomerIntention;
    @BindView(R.id.svgv_customer_emotion)
    ScrollViewWithGridView svgvCustomerEmotion;
    @BindView(R.id.fll_other_tag)
    FlowLayout fllOtherTag;
    @BindView(R.id.et_we_chat_num)
    EditText etWeChatNum;
    @BindView(R.id.et_phone_num)
    EditText etPhoneNum;
    @BindView(R.id.ll_save_and_hang_up)
    LinearLayout llSaveAndHangUp;
    @BindView(R.id.tv_call_time_title)
    TextView tvCallTimeTitle;
    @BindView(R.id.ll_bot_View)
    LinearLayout llBotView;
    @BindView(R.id.ll_speak)
    LinearLayout llSpeak;
    @BindView(R.id.tv_speak)
    TextView tvSpeak;
    @BindView(R.id.ll_call_next)
    LinearLayout llCallNext;
    @BindView(R.id.iv_continuous_call)
    ImageView ivContinuousCall;
    @BindView(R.id.ll_continuous_call)
    LinearLayout llContinuousCall;
    @BindView(R.id.iv_speak)
    ImageView ivSpeak;
    @BindView(R.id.iv_hang_up)
    ImageView ivHangUp;
    @BindView(R.id.iv_edit_call)
    ImageView ivEditCall;

    private String TAG = "CallingActivity";
    private CallingPresenter callingPresenter;
    private int time = 0;
    private String cluesId;
    private String industry;
    private String area;
    private String phoneNum;
    private AudioPlayerUtils audioPlayerUtils;
    //    private int ringId = R.raw.ring_calling;
//    private int durTime = 15000; //间隔时间
//    private long millisInFuture = 30000;  // 倒计时30s
//    private long countDownInterval = 1000; // 倒计时间隔1s
//    private TimerCallBackUtils timerCallBackUtils;
//    private boolean isStartCallTimeDown;
    private boolean isSpeakPhoneOn; //  是否开启扬声器
    private boolean isSaveSuccess; // 是否保存成功
    private List<CluesListBean> cluesList = new ArrayList<>();
    private int callPosition;
    private boolean isAutoCallNext;
    private boolean isCallNext;
    private boolean isCallAll;
    private String callType;
    private boolean isInputCall;
    private Button mBtBaocun;
    // handler
    private Handler mHandler = new Handler();
    private Button bt_baocun_dualCall;
    private ImageView iv_hang_up_dualCall;
    private ImageView iv_speak_dualCall;
    private ImageView iv_edit_call_dualCall;


    @Override
    public void getIntentData() {
        cluesId = getIntent().getStringExtra("cluesId");
        industry = getIntent().getStringExtra("industry");
        area = getIntent().getStringExtra("area");
        phoneNum = getIntent().getStringExtra("phoneNum");
        cluesList = (List<CluesListBean>) getIntent().getSerializableExtra("cluesList");
        callPosition = getIntent().getIntExtra("callPosition", -1);
        isInputCall = getIntent().getBooleanExtra("isInputCall", false);
       // mBtBaocun = findViewById(R.id.bt_baocun);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_calling;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        // 呼叫类型 AXB 或者 DualCall  DualCall = 双呼
        callType = SharedPreferencesUtil.getSharedPreferences(mContext).getString("callType", "");
        callingPresenter = new CallingPresenter(this, this);
        audioPlayerUtils = new AudioPlayerUtils(mContext);
        audioPlayerUtils.setSpeakPhoneOn(isSpeakPhoneOn);
        iv_hang_up_dualCall = findViewById(R.id.iv_hang_up);
        iv_speak_dualCall = findViewById(R.id.iv_speak);
        iv_edit_call_dualCall = findViewById(R.id.iv_edit_call);
        bt_baocun_dualCall = findViewById(R.id.bt_baocun);
        if (callType=="AXB"){

        }else {
            iv_edit_call_dualCall.setVisibility(View.GONE);
            iv_hang_up_dualCall.setVisibility(View.GONE);
            iv_speak_dualCall.setVisibility(View.GONE);
            bt_baocun_dualCall.setVisibility(View.VISIBLE);
            if (isCallAll) {
                ToastUtils.showToast(this, "已全部拨打结束");
                return;
            }
            if (isCallNext) {
                callingPresenter.callNext();
            } else {
                callNextNoHangUp();
            }
        }
        // 注册广播接收器
        EventBus.getDefault().register(this);
        // 传值
        if (cluesId != null) {
            callingPresenter.setCluesId(cluesId);
        }

        if (isInputCall) {
            callingPresenter.importOnePhone(phoneNum);
            llContinuousCall.setVisibility(View.GONE);
            if (phoneNum != null) {
                etPhoneNum.setText(phoneNum);
            }
        } else {
            callingPresenter.initCall(phoneNum);
            // 获取数据
            callingPresenter.cluesInfo();
            // 拨打电话
            if (cluesList != null) {
                if (callPosition >= 0 && callPosition < cluesList.size()) {
                    cluesList.get(callPosition).setCalled(true);
                    if (!StringUtils.isNullOrBlank(cluesList.get(callPosition).getPhoneNumber())) {
                        etPhoneNum.setText(cluesList.get(callPosition).getPhoneNumber());
                    }
                }
                // 列表只有一个的时候，呼叫一次就呼叫完
                if (cluesList.size() == 1) {
                    callAllResult();
                }
            }
            // 连续拨打按钮初始化
            if (cluesList != null && cluesList.size() > 0) {
                if (cluesList.size() == 1) {
                    callAllResult();
                } else {
                    callNextVisible();
                }
            } else {
                callAllResult();
            }
        }

        if (callType.equals("DualCall")) {
            tvCallTimeTitle.setText(mContext.getResources().getString(R.string.system_calling));
        }
        // 赋值
        if (phoneNum != null) {
            tvPhoneNum.setText(phoneNum);
        }
        if (industry != null) {
            tvIndustry.setText(industry);
        }
        if (area != null) {
            tvAddress.setText(area);
        }

        // 自定义
        callingPresenter.addTagsView(fllOtherTag);
        // 客户属性
        callingPresenter.initAdapterCustomProperty(svgvCustomerProperty);
        // 客户意向
        callingPresenter.initAdapterCustomIntention(svgvCustomerIntention);
        // 客户情绪
        callingPresenter.initAdapterCustomEmotion(svgvCustomerEmotion);
        // 初始化设置呼叫位置和呼叫列表数据
        callingPresenter.setCallPosition(callPosition);
        callingPresenter.setCluesList(cluesList);

    }


    @OnClick({R.id.ll_speak, R.id.ll_save_and_hang_up, R.id.ll_call_next, R.id.ll_continuous_call, R.id.iv_speak, R.id.iv_hang_up, R.id.iv_edit_call})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_speak:
                isSpeakPhoneOn = !isSpeakPhoneOn;
                audioPlayerUtils.setSpeakPhoneOn(isSpeakPhoneOn);
                if (isSpeakPhoneOn) {
                    llSpeak.setBackground(mContext.getResources().getDrawable(R.drawable.bg_corner_left_38dp_blue));
                } else {
                    llSpeak.setBackground(mContext.getResources().getDrawable(R.drawable.bg_corner_left_38dp_empty));
                }
                break;
            case R.id.ll_save_and_hang_up:
                isCallNext = false;
                callNextNoHangUp();
                break;
            case R.id.ll_call_next:
                isCallNext = true;
                callNextVisible();
                callingPresenter.callNext();
                break;
            case R.id.ll_continuous_call:
                isCallNext = !isCallNext;
                if (isCallNext) {
                    ivContinuousCall.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_continuous_call_in));
                } else {
                    ivContinuousCall.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_continuous_call_out));
                }
                break;
            case R.id.iv_speak:
                isSpeakPhoneOn = !isSpeakPhoneOn;
                audioPlayerUtils.setSpeakPhoneOn(isSpeakPhoneOn);
                if (isSpeakPhoneOn) {
                    ivSpeak.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_speak_on));
                } else {
                    ivSpeak.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_speak_out));
                }
                break;
            case R.id.iv_hang_up:
                if (isCallAll) {
                    ToastUtils.showToast(this, "已全部拨打结束");
                    return;
                }
                if (isCallNext) {
                    callingPresenter.callNext();
                } else {
                    callNextNoHangUp();
                }
                break;
            case R.id.iv_edit_call:
                this.finish();
                break;
        }
    }

    @Override
    public void saveAndHangUpCallingSuccessResult() {
        isSaveSuccess = true;
        ToastUtils.showToast(mContext, "保存成功");
        if (isCallNext) {
            callingPresenter.callNext();
        } else {
            this.finish();
        }
    }

    @Override
    public void saveAndHangUpCallingFailureResult() {
        callingPresenter.showCommitFailureDialog(isCallNext);
    }

    @Override
    public void setTagsList() {
        callingPresenter.setWeChat(etWeChatNum);
        callingPresenter.addTagsView(fllOtherTag);
    }

    @Override
    public void callAllResult() {
        llCallNext.setEnabled(false);
        llCallNext.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_corner_right_38dp_gray));
        // 新UI用到
        isCallAll = true;
    }

    @Override
    public void callNextVisible() {
        if (cluesList != null) {
            llCallNext.setVisibility(View.VISIBLE);
            llCallNext.setEnabled(true);
            if (isCallNext) {
                llCallNext.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.btn_corner_right_38dp_blue));
            } else {
                llCallNext.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_corner_right_38dp_empty));
            }
        }
    }

    @Override
    public void callOtherPhoneText(String phoneNum) {
        HandlerUtils.setText(tvPhoneNum, phoneNum);
        HandlerUtils.setText(etPhoneNum, phoneNum);
        HandlerUtils.setText(tvCallingTime, "");
    }

    @Override
    public void callNextNoHangUp() {
        onlyHangUp();
        // 挂断并保存
        callingPresenter.saveAndHangUpCalling(etPhoneNum, etWeChatNum);
    }

    @Override
    public void onlyHangUp() {
        // 停止声音
        audioPlayerUtils.stopPlayFromRawFile();
        // 停止计时器
        mHandler.removeCallbacks(runnable);
//        mHandler.removeCallbacks(callRingRunnable);
    }

    @Override
    public void setCallStateText(String callStateStr) {
        HandlerUtils.setText(tvCallTimeTitle, callStateStr);
        HandlerUtils.setText(tvCallingTime, "");
    }

    @Override
    public void setIsAutoCallNext(boolean isAutoCallNext) {
        this.isAutoCallNext = isAutoCallNext;
    }

    @Override
    public void setShowEmptyText() {
        callingPresenter.setShowEmptyText(tvPhoneNum, tvCallingTime, tvCallTimeTitle, etPhoneNum);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            isCallNext = false;
            callNextNoHangUp();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String message) {
//        if (isStartCallTimeDown) {
//            // 30秒内收到接听，拒接等返回的广播，则取消倒计时
//            timerCallBackUtils.cancel();
//        }
        switch (message) {
            case Constants.CALL_OUTGOING: // 呼出
            case Constants.CALL_EARLY_MEDIA:
//                mHandler.removeCallbacks(callRingRunnable);
                tvCallTimeTitle.setText(mContext.getResources().getString(R.string.calling));
                tvCallingTime.setText("");
                audioPlayerUtils.stopPlayFromRawFile();
//                ringId = R.raw.ring_calling;
//                audioPlayerUtils.playFromRawFile(ringId);
//                durTime = 1000 + 3000;// 音频时间(1s) + 间隔时间 = 延迟时间
//                mHandler.postDelayed(callRingRunnable, durTime); // 每两秒执行一次runnable.
                // 呼叫30秒没收到接听，则挂断。
//                timerCallBackUtils = new TimerCallBackUtils(millisInFuture, countDownInterval, callRingCallBack);
//                timerCallBackUtils.start();
//                isStartCallTimeDown = true;
                break;
//            case Constants.CALL_EARLY_MEDIA:
//                tvCallTimeTitle.setText(mContext.getResources().getString(R.string.early_media));
//                tvCallingTime.setText("");
//                audioPlayerUtils.stopPlayFromRawFile();
//                break;
            case Constants.CALL_CONNECT:
//                mHandler.removeCallbacks(callRingRunnable);
                audioPlayerUtils.stopPlayFromRawFile();
                // 启动计时器
                mHandler.postDelayed(runnable, 1000); // 每秒执行一次runnable.
                tvCallTimeTitle.setText(mContext.getResources().getString(R.string.calling_time));
                tvCallingTime.setText("00:00");
                break;
            case Constants.CALL_END: // 通话结束
                callingPresenter.setIsCalling(false);
                mHandler.removeCallbacks(runnable);
//                mHandler.removeCallbacks(callRingRunnable);
                audioPlayerUtils.stopPlayFromRawFile();
                tvCallTimeTitle.setText(mContext.getResources().getString(R.string.calling_end));
                if (StringUtils.isNullOrBlank(tvCallingTime.getText().toString().trim())) {
                    tvCallingTime.setText("00:00");
                }
                // 点击弹框 忽略 按钮，自动呼叫
                if (isAutoCallNext) {
                    callingPresenter.callNext();
                    isAutoCallNext = false;
                }
                break;
            case Constants.CALL_HANGUP: // 挂断
                mHandler.removeCallbacks(runnable);
//                mHandler.removeCallbacks(callRingRunnable);
                audioPlayerUtils.stopPlayFromRawFile();
                tvCallTimeTitle.setText(mContext.getResources().getString(R.string.calling_end));
                if (StringUtils.isNullOrBlank(tvCallingTime.getText().toString().trim())) {
                    tvCallingTime.setText("00:00");
                }
                // 避免保存成功的提示语被挂断提示语快速替换提醒
                if (!isSaveSuccess) {
                    ToastUtils.showToast(mContext, "已挂断");
                }
                break;
            case Constants.CALL_ERROR:
                callingPresenter.setIsCalling(false);
//                mHandler.removeCallbacks(callRingRunnable);
                audioPlayerUtils.stopPlayFromRawFile();
                tvCallTimeTitle.setText(mContext.getResources().getString(R.string.calling_err));
//                ringId = R.raw.ring_call_end;
//                audioPlayerUtils.playFromRawFile(ringId); x
//                durTime = 3000 + 1000;// 音频时间(3s) + 间隔时间 = 延迟时间
//                mHandler.postDelayed(callRingRunnable, durTime); // 每两秒执行一次runnable.
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(runnable);
//        mHandler.removeCallbacks(callRingRunnable);
        audioPlayerUtils.stopPlayFromRawFile();
        if (callingPresenter.isCalling()) {
            if (Utils.isAXB(mContext) && Constants.sipIsLogin) {
                MyLinPhoneManager.getInstance(mContext).hangUp();
            } else {

            }
        }
        EventBus.getDefault().unregister(this);
    }

    // 通话计时器
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            // 要做的事情
            mHandler.postDelayed(this, 1000);
            time++;
            tvCallingTime.setText(TimeUtils.timeFormat(time));
        }
    };

//    // 计时器
//    Runnable callRingRunnable = new Runnable() {
//        @Override
//        public void run() {
//            // TODO Auto-generated method stub
//            // 要做的事情
//            LogUtils.i(TAG, "callRingRunnable durTime = " + durTime + ", ringId = " + ringId);
//            mHandler.postDelayed(this, durTime);
//            audioPlayerUtils.playFromRawFile(ringId);
//        }
//    };
//
//    /**
//     * 倒计时回调
//     */
//    TimerCallBackUtils.TimerCallBack callRingCallBack = new TimerCallBackUtils.TimerCallBack() {
//        @Override
//        public void onTickCallBack(long millisUntilFinished) {
//        }
//
//        @Override
//        public void onFinishCallBack() {
//            LogUtils.i(TAG, "onFinishCallBack durTime = " + durTime + ", ringId = " + ringId);
//            ringId = R.raw.ring_call_failure; // 音频时间(3s) + 间隔时间 = 延迟时间
//            durTime = 12000 + 2000;// 音频时间(12s) + 间隔时间 = 延迟时间
//            mHandler.postDelayed(callRingRunnable, durTime); // 每两秒执行一次runnable.
//        }
//    };

}