package com.hz.maiku.maikumodule.http;

import android.util.Log;

import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/2/12
 * @email 252774645@qq.com
 */
public class HttpLogger implements HttpLoggingInterceptor.Logger{
    @Override
    public void log(String message) {
        Log.d("HttpLogInfo", message);
    }

}
