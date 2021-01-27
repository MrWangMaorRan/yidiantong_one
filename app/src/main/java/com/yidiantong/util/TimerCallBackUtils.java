package com.yidiantong.util;

import android.os.CountDownTimer;

/**
 * Created by Administrator on 2018/5/23.
 */

public class TimerCallBackUtils extends CountDownTimer {
    private TimerCallBack timerCallBack;

    public TimerCallBackUtils(long millisInFuture, long countDownInterval, TimerCallBack timerCallBack) {
        super(millisInFuture + 50, countDownInterval);  // +50 是因为倒计时不准确，需要把时间差补回
        this.timerCallBack = timerCallBack;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        timerCallBack.onTickCallBack(millisUntilFinished);
    }

    @Override
    public void onFinish() {
        timerCallBack.onFinishCallBack();
    }

    /**
     * 回调
     */
    public interface TimerCallBack {

        void onTickCallBack(long millisUntilFinished);

        void onFinishCallBack();
    }
}
