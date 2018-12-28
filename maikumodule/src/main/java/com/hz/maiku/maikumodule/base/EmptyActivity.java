package com.hz.maiku.maikumodule.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.hz.maiku.maikumodule.event.EmptyEvent;
import com.hz.maiku.maikumodule.util.AdUtil;
import com.hz.maiku.maikumodule.util.RxBus.RxBusHelper;

import io.reactivex.disposables.CompositeDisposable;

/**
 * 当应用程序图标被隐藏时使用
 */
public class EmptyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        RxBusHelper.doOnMainThread(EmptyEvent.class, new CompositeDisposable(), new RxBusHelper.OnEventListener<EmptyEvent>() {
            @Override
            public void onEvent(EmptyEvent junkCleanerTotalSizeEvent) {
                finish();
            }
        });
        AdUtil.getAdTypeAndShow(EmptyActivity.this, "EmptyActivity");
        setContentView(new View(this));
    }
}
