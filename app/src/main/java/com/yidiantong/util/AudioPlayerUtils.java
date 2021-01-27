package com.yidiantong.util;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;

import java.io.IOException;

import static android.media.AudioManager.ADJUST_RAISE;
import static android.media.AudioManager.ADJUST_SAME;
import static android.media.AudioManager.FLAG_PLAY_SOUND;
import static android.media.AudioManager.FLAG_SHOW_UI;
import static android.media.AudioManager.STREAM_MUSIC;

public class AudioPlayerUtils {
    private MediaPlayer mediaPlayer;
    private Context mContext;

    public AudioPlayerUtils(Context mContext) {
        this.mContext = mContext;
        setSpeakPhoneOn(false);
    }

    /**
     * 播放来电和呼出铃声
     *
     * @param audioId
     */
    public void playFromRawFile(int audioId) {
//        AudioManager am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
//        final int ringerMode = am.getRingerMode();
//        //2.普通模式可以呼叫普通模式： AudioManager.RINGER_MODE_NORMAL 静音模式：AudioManager.RINGER_MODE_VIBRATE 震动模式：AudioManager.RINGER_MODE_SILENT
//        if (ringerMode == AudioManager.RINGER_MODE_NORMAL) {
        try {
            mediaPlayer = new MediaPlayer();
            AssetFileDescriptor file = mContext.getResources().openRawResourceFd(audioId);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                if (!mediaPlayer.isPlaying()) {
//                        mediaPlayer.setVolume(0.5f, 0.5f);
//                        mediaPlayer.setLooping(true);//循环播放
                    mediaPlayer.prepareAsync();
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mediaPlayer.start();
                        }
                    });
                }
            } catch (IOException e) {
                mediaPlayer = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        }
    }

    public void setSpeakPhoneOn(boolean isSpeakerphoneOn){
        //1.获取模式
        AudioManager am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        am.setSpeakerphoneOn(isSpeakerphoneOn);
        if(isSpeakerphoneOn) {
            am.adjustStreamVolume(STREAM_MUSIC, ADJUST_SAME, FLAG_PLAY_SOUND);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                am.setMode(AudioManager.MODE_IN_COMMUNICATION);
            } else {
                am.setMode(AudioManager.MODE_IN_CALL);
            }
        }
    }

    /**
     * 结束播放来电和呼出铃声
     */
    public void stopPlayFromRawFile() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer = null;
    }

}
