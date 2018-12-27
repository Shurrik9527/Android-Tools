package com.hz.maiku.maikumodule.util.RxBus;


import io.reactivex.functions.Consumer;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/9/7
 * @email 252774645@qq.com
 */
public abstract class Callback<T> implements Consumer<T>{

    /**
     * 监听到事件时回调接口
     * @param t 返回结果
     */
    public abstract void onEvent(T t);


    @Override
    public void accept(T t) {
        onEvent(t);
    }
}
