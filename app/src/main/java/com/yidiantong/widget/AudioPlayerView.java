package com.yidiantong.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;


import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.appcompat.widget.AppCompatTextView;

import com.yidiantong.R;
import com.yidiantong.adapter.CommunicateRecordListAdapter;
import com.yidiantong.util.StringUtils;
import com.yidiantong.util.log.LogUtils;

import java.io.IOException;


/**
 * Incremental change is better than ambitious failure.
 *
 * @author : <a href="http://mysticcoder.coding.me">MysticCoder</a>
 * @date : 2018/3/21
 * @desc : 不用时一定要调用{{@link #release()}}
 */


public class AudioPlayerView extends AppCompatTextView {

    String mUrl;

    public boolean isHasprepared() {
        return hasprepared;
    }

    /**
     * 在非初始化状态下调用setDataSource  会抛出IllegalStateException异常
     */
    boolean hasprepared = false;

    private MediaPlayer mediaPlayer;

    private Handler handler;
    private Runnable runnable;
    private int i;
    private Context mContext;
    private CommunicateRecordListAdapter.UrlCallBack urlCallBack;

    private int[] drawLefts = new int[]{R.drawable.ic_recording_gray, R.drawable.ic_recording_blue, R.drawable.ic_recording_gray};
    private int[] bgDrawable = new int[]{R.drawable.bg_corner_4dp_white_with_gray_line, R.drawable.bg_corner_4dp_white_with_blue_line, R.drawable.bg_corner_4dp_white_with_gray_line};
    private int[] txtColor = new int[]{R.color.gray_5b5e64, R.color.blue_3f74fd, R.color.gray_5b5e64};

    public AudioPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initMediaPlayer();
    }

    private void initMediaPlayer() {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        } catch (Exception e) {
            LogUtils.e("mediaPlayer", " init error" + e);
        }

        if (mediaPlayer != null) {
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    hasprepared = true;
                    setText(getDuration());
                }
            });
        }
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mediaPlayer.reset();
                return false;
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopAnim();
            }
        });
        setClick();

    }

    public void setUrlCallBack(CommunicateRecordListAdapter.UrlCallBack urlCallBack) {
        this.urlCallBack = urlCallBack;
    }

    public void setUrl(String url) {
        mUrl = url;
        new Runnable(){
            @Override
            public void run() {
                try {
                    mediaPlayer.setDataSource(url);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                    LogUtils.e("mediaPlayer", " set dataSource error" + e);
                } catch (IllegalStateException e) {
                    LogUtils.e("mediaPlayer", " set dataSource error" + e);
                }
            }
        }.run();
    }

    /**
     * 用于需要设置不同的dataSource
     * 二次setdataSource的时候需要reset 将MediaPlayer恢复到Initialized状态
     *
     * @param url
     */
    public void resetUrl(String url) {
        if (TextUtils.isEmpty(mUrl) || hasprepared) {
            mediaPlayer.reset();
        }
        setUrl(url);
    }

    public String getDuration() {
        int duration = mediaPlayer.getDuration();
        if (duration == -1) {
            return "";
        } else {
            int sec = duration / 1000;
            int m = sec / 60;
            int s = sec % 60;
//            String min = "";
//            String ss = "";
//            if (m < 10) {
//                min = "0" + m;
//            } else {
//                min = m + "";
//            }
//            if (s < 10) {
//                ss = "0" + s;
//            } else {
//                ss = s + "";
//            }
            return m + "分" + s + "秒";
        }
    }

    private void startAnim() {
        i = 0;
        if (handler == null) {
            handler = new Handler();
        }
        if (runnable == null) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    handler.postDelayed(this, 200);
                    setDrawableRight(drawLefts[i % 3]);
                    setDrawableBg(bgDrawable[i % 3]);
                    setTxtColor(txtColor[i % 3]);
                    i++;
                }
            };
        }
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 200);
    }

    private void stopAnim() {
        setDrawableRight(drawLefts[2]);
        setDrawableBg(bgDrawable[2]);
        setTxtColor(txtColor[2]);
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }

    /**
     * 设置 drawableLeft
     *
     * @param
     * @param id
     */
    private void setDrawableRight(@DrawableRes int id) {
        Drawable rightDrawable = getResources().getDrawable(id);
        rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
        setCompoundDrawables(null, null, rightDrawable, null);
    }

    /**
     * 设置 setDrawableBg
     *
     * @param
     * @param id
     */
    private void setDrawableBg(@DrawableRes int id) {
        Drawable bgDrawable = getResources().getDrawable(id);
        setBackground(bgDrawable);
    }

    /**
     * 设置 setTxtColor
     *
     * @param
     * @param id
     */
    @SuppressLint("ResourceType")
    private void setTxtColor(@ColorInt int id) {
        setTextColor(getResources().getColor(id));
    }

    private void setClick() {
        // 点击事件
        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                第一次调用start播放正常. 然后调用stop().停止播放.然后再调start 放不了
//
                //方案一： 先stop，再Reset ，stop换成pause也行
//                if (mediaPlayer.isPlaying()){
//                    mediaPlayer.stop();
//                    stopAnim();
//                }else {
//                    mediaPlayer.reset();
//                    setUrl(mUrl);
//                    startAnim();
//                    mediaPlayer.start();
//
//                }

                if(urlCallBack != null){
                    if(StringUtils.isNullOrBlank(mUrl)) {
                        urlCallBack.setUrlCallBack();
                    }
                    urlCallBack.setOnClicked();
                }
                // 采取方案二  pause  代替 stop
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    stopAnim();
                } else {
                    mediaPlayer.seekTo(0);
                    startAnim();
                    mediaPlayer.start();
                }
            }

        });
    }

    public void pause(){
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }

        if (handler != null) {
            setDrawableRight(drawLefts[2]);
            setDrawableBg(bgDrawable[2]);
            setTxtColor(txtColor[2]);
            handler.removeCallbacks(runnable);
        }
    }

    /**
     * 释放资源
     */
    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }

}
