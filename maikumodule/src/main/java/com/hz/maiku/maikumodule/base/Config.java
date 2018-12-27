package com.hz.maiku.maikumodule.base;

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 配置信息
 * @date 2018/12/27
 * @email 252774645@qq.com
 */
public class Config {
    private static  final  String  TAG =MaiKuApp.class.getName();
    public static String getPropertiesURL(Context c, String s) { String url = null;
        Properties properties = new Properties();
        try { properties.load(c.getAssets().open("config.properties"));
            url = properties.getProperty(s);
        } catch (Exception e) { e.printStackTrace();
        } return url;
    }

}
