package com.hz.maiku.maikumodule.http;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/2/12
 * @email 252774645@qq.com
 */
public class HtpRequest implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request()
                .newBuilder()
                .addHeader("Content-Type", "text/html; charset=UTF-8")
                .build();

        return chain.proceed(request);
    }
}
