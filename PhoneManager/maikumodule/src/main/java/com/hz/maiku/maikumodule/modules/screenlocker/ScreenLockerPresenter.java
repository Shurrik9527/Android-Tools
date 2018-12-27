package com.hz.maiku.maikumodule.modules.screenlocker;

import android.os.Handler;
import android.os.Message;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Shurrik on 2017/11/22.
 */

public class ScreenLockerPresenter implements ScreenLockerContract.Presenter {
    private ScreenLockerContract.View view;


    Handler handler = new Handler() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd    E", Locale.ENGLISH);
        @Override
        public void handleMessage(Message message) {
            String time = sdf.format(System.currentTimeMillis());
            String date = sdf1.format(System.currentTimeMillis());
            view.showTime(time);
            view.showDate(date);
        }
    };


    public ScreenLockerPresenter(ScreenLockerContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void subscribe() {
        startTimer();
    }

    @Override
    public void unsubscribe() {
        stopTimer();
    }

    Timer timer = null;
    @Override
    public void startTimer() {
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        };
        timer.schedule(task, 0, 1000);
    }

    @Override
    public void stopTimer() {
        if (timer != null){
            timer.cancel();
        }
    }

    @Override
    public void checkChargingCompleted() {
        view.showNotification("Charging Completed");
    }

}
