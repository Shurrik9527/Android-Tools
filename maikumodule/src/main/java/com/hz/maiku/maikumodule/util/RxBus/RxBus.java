package com.hz.maiku.maikumodule.util.RxBus;

import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;

import io.reactivex.Observable;


/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 事件 组件间通信
 * @date 2018/9/7
 * @email 252774645@qq.com
 */
public class RxBus {

    private final Relay<Object> mBus;

    private RxBus() {
        // toSerialized method made bus thread safe
        mBus = PublishRelay.create().toSerialized();
    }

    public static RxBus getDefault() {
        return Holder.BUS;
    }

    public void post(Object obj) {
        mBus.accept(obj);
    }

    public <T> Observable<T> toObservable(Class<T> tClass) {
        return mBus.ofType(tClass);
    }

    public Observable<Object> toObservable() {
        return mBus;
    }

    public boolean hasObservers() {
        return mBus.hasObservers();
    }

    private static class Holder {
        private static final RxBus BUS = new RxBus();
    }


}
