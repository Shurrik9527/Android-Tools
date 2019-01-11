package com.sharkwang8.phoneassistant;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.duapps.ad.base.DuAdNetwork;
import com.hz.maiku.maikumodule.base.MaiKuApp;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;

/**
 * Created by Shurrik on 2018/12/26.
 */

public class App extends MaiKuApp {


    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        DuAdNetwork.setConsentStatus(this, true);
        DuAdNetwork.getConsentStatus(this);
        /**
         * the sdk initialization 初始化SDK
         */
        DuAdNetwork.init(this, getConfigJSON(getApplicationContext()));
    }

    /**
     * 从assets中读取txt
     */
    private String getConfigJSON(Context context) {
        BufferedInputStream bis = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            bis = new BufferedInputStream(context.getAssets().open("json.txt"));
            byte[] buffer = new byte[4096];
            int readLen = -1;
            while ((readLen = bis.read(buffer)) > 0) {
                bos.write(buffer, 0, readLen);
            }
        } catch (IOException e) {
            Log.e("", "IOException :" + e.getMessage());
        } finally {
            closeQuietly(bis);
        }

        return bos.toString();
    }
    private void closeQuietly(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (IOException e) {
            // empty
        }
    }
}
