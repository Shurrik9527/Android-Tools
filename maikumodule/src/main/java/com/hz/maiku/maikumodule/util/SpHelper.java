package com.hz.maiku.maikumodule.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Map;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 本地保存信息工具类
 * @date 2018/10/12
 * @email 252774645@qq.com
 */
public class SpHelper {

     private static final String TAG =SpHelper.class.getName();
    private volatile static SpHelper mInstance;

    private Context mContext;
    private SharedPreferences mPref;
    /*
     * 保存手机里面的名字
     */private SharedPreferences.Editor editor;
    private SpHelper() {
    }

    public static SpHelper getInstance() {
        if (null == mInstance) {
            synchronized (SpHelper.class) {
                if (null == mInstance) {
                    mInstance = new SpHelper();
                }
            }
        }
        return mInstance;
    }

    //在App里面初始化
    public void init(Context context) {
        if (mContext == null) {
            mContext = context;
        }
        if (mPref == null) {
            mPref = PreferenceManager.getDefaultSharedPreferences(mContext);
            editor = mPref.edit();
        }
    }

    /**
     * 存储
     */
    public void put(String key, Object object) {
        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }
        editor.apply();
    }

    /**
     * 获取保存的数据
     */
    public Object get(String key, Object defaultObject) {
        if (defaultObject instanceof String) {
            return mPref.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return mPref.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return mPref.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return mPref.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return mPref.getLong(key, (Long) defaultObject);
        } else {
            return mPref.getString(key, null);
        }
    }

    /**
     * 移除某个key值已经对应的值
     */
    public void remove(String key) {
        editor.remove(key);
        editor.commit();
    }

    /**
     * 清除所有数据
     */
    public void clear() {
        editor.clear();
        editor.commit();
    }

    /**
     * 查询某个key是否存在
     */
    public Boolean contain(String key) {
        return mPref.contains(key);
    }

    /**
     * 返回所有的键值对
     */
    public Map<String, ?> getAll() {
        return mPref.getAll();
    }

}
