package com.hz.maiku.maikumodule.http;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Shurrik on 2017/11/22.
 */

public class HttpCenter {
    private static final String BASE_URL = "http://blackteach.com/";

    protected static Retrofit getRetrofit(Retrofit.Builder builder) {
        OkHttpClient.Builder client = new OkHttpClient().newBuilder();
        client.retryOnConnectionFailure(true);
        client.connectTimeout(1, TimeUnit.MINUTES);
        client.readTimeout(1, TimeUnit.MINUTES);
        client.writeTimeout(1, TimeUnit.MINUTES);
        return builder.addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client.build())
                .build();
    }

    private static class BuilderHolder {
        private static Retrofit.Builder BUILDER = new Retrofit.Builder().baseUrl(BASE_URL);
    }

    private static class ServiceHolder {
        private static HttpService TEST_SERVICE = getRetrofit(BuilderHolder.BUILDER).create(HttpService.class);
    }

    public static HttpService getService() {
        return ServiceHolder.TEST_SERVICE;
    }
}
