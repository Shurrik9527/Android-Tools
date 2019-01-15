package com.hz.maiku.maikumodule.manager;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.text.TextUtils;

import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.base.MaiKuApp;
import com.hz.maiku.maikumodule.util.ToastUtil;

import java.io.IOException;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/1/14
 * @email 252774645@qq.com
 */
public class AudioPlayManager {

    private MediaPlayer mediaPlayer;

    private AudioPlayManager() {
    }

    public static AudioPlayManager getInstance() {
        return SingletonHolder.instance;
    }

    public void startPlay(String url, MediaPlayer.OnCompletionListener completionListener) {
        startPlay(url, completionListener, null);
    }

    /**
     * 播放在线音频, 带播放完成回调
     *
     * @param url
     */
    public void startPlay(String url, MediaPlayer.OnCompletionListener completionListener, MediaPlayer.OnErrorListener errorListener) {
        if (TextUtils.isEmpty(url)) {
            ToastUtil.showToast(MaiKuApp.getmContext(),MaiKuApp.getmContext().getString(R.string.deep_clean_audio_noplay));
            return;
        }

        stopPlay();

        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }

        try {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);//MediaPlayerNative: error (1, -2147483648)
            mediaPlayer.setDataSource(url);
            if (completionListener != null) {
                mediaPlayer.setOnCompletionListener(completionListener); // 设置播放完成监听
            }
            if (errorListener != null) {
                mediaPlayer.setOnErrorListener(errorListener);
            }

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });
            // 异步播放在线音频流，防止阻塞UI线程
            mediaPlayer.prepareAsync();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 播放在线音频,不带播放完成回调
     *
     * @param url
     */
    public void startPlay(String url) {
        startPlay(url, null);
    }


    /**
     * 停止播放音频
     */
    public void stopPlay() {
        if (mediaPlayer != null&&mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }

    }

    /**
     * 当前是否正在播放
     *
     * @return
     */
    public boolean isPlaying() {
        if (mediaPlayer == null) {
            return false;
        }
        return mediaPlayer.isPlaying();
    }


    static class SingletonHolder {
        static AudioPlayManager instance = new AudioPlayManager();

        SingletonHolder() {

        }
    }
}
