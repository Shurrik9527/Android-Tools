package com.hz.maiku.maikumodule.util.RxBus;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe rxbus 帮助工具
 * @date 2018/9/10
 * @email 252774645@qq.com
 */
public class RxBusHelper {

    /**
     * 发布消息
     *
     * @param o
     */
    public static void post(Object o) {
        RxBus.getDefault().post(o);
    }

    /**
     * 接收消息,并在主线程处理
     *
     * @param aClass
     * @param disposables 用于存放消息
     * @param listener
     * @param <T>
     */
    public static <T> void doOnMainThread(Class<T> aClass, CompositeDisposable disposables, final OnEventListener<T> listener) {
        disposables.add(RxBus.getDefault().toObservable(aClass).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<T>() {
            @Override
            public void accept(T t) throws Exception {
                listener.onEvent(t);
            }
        }));
    }

    public static <T> void doOnMainThread(Class<T> aClass, final OnEventListener<T> listener) {
        RxBus.getDefault().toObservable(aClass).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<T>() {
            @Override
            public void accept(T t) throws Exception {
                listener.onEvent(t);
            }
        });

    }

    /**
     * 接收消息,并在子线程处理
     *
     * @param aClass
     * @param disposables
     * @param listener
     * @param <T>
     */
    public static <T> void doOnChildThread(Class<T> aClass, CompositeDisposable disposables, final OnEventListener<T> listener) {

        disposables.add(RxBus.getDefault().toObservable(aClass).subscribeOn(Schedulers.newThread()).subscribe(new Consumer<T>() {
            @Override
            public void accept(T t) throws Exception {
                listener.onEvent(t);
            }
        }));
    }

    public static <T> void doOnChildThread(Class<T> aClass, final OnEventListener<T> listener) {

        RxBus.getDefault().toObservable(aClass).subscribeOn(Schedulers.newThread()).subscribe(new Consumer<T>() {
            @Override
            public void accept(T t) throws Exception {
                listener.onEvent(t);
            }
        });
    }

    public interface OnEventListener<T> {
        void onEvent(T t);
    }


}
