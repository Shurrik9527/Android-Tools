package com.hz.maiku.maikumodule.util;

import android.support.v7.widget.SearchView;
import android.text.TextUtils;

import com.hz.maiku.maikumodule.bean.CommLockInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/9/18
 * @email 252774645@qq.com
 */
public class StringUtil {

    private static long lastClickTime;
    /**
     * 防止多次点击事件处理
     * @return
     * @author songdiyuan
     */
    public synchronized static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (timeD < 2000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }


    /**
     * 格式化字符串，每三位用逗号隔开
     *
     * @param str
     * @return
     */
    public static String addComma(String str) {
        str = new StringBuilder(str).reverse().toString();     //先将字符串颠倒顺序
        if (str.equals("0")) {
            return str;
        }
        String str2 = "";
        for (int i = 0; i < str.length(); i++) {
            if (i * 3 + 3 > str.length()) {
                str2 += str.substring(i * 3, str.length());
                break;
            }
            str2 += str.substring(i * 3, i * 3 + 3) + ",";
        }
        if (str2.endsWith(",")) {
            str2 = str2.substring(0, str2.length() - 1);
        }
        //最后再将顺序反转过来
        String temp = new StringBuilder(str2).reverse().toString();
        //将最后的,去掉
        return temp.substring(0, temp.lastIndexOf(",")) + temp.substring(temp.lastIndexOf(",") + 1, temp.length());
    }

    /**
     * 清除重复的数据
     * @param lockInfos
     * @return
     */
    public static List<CommLockInfo> clearRepeatCommLockInfo(List<CommLockInfo> lockInfos) {
        HashMap<String, CommLockInfo> hashMap = new HashMap<>();
        for (CommLockInfo lockInfo : lockInfos) {
            if (!hashMap.containsKey(lockInfo.getPackageName())) {
                hashMap.put(lockInfo.getPackageName(), lockInfo);
            }
        }
        List<CommLockInfo> commLockInfos = new ArrayList<>();
        for (HashMap.Entry<String, CommLockInfo> entry : hashMap.entrySet()) {
            commLockInfos.add(entry.getValue());
        }
        return commLockInfos;
    }


    /**
     * 搜索View
     * @param searchView
     * @return
     */
    public static Observable<String> fromSearchView(final SearchView searchView){

        final BehaviorSubject<String> subject = BehaviorSubject.createDefault("");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                subject.onComplete();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                subject.onNext(s);
                return true;
            }
        });
        return  subject;
    }





    /**
     * 是否是中文
     * @param c
     * @return
     */
    public static boolean isChinese(char c){
        return c>=0x4e00&&c<=0x9FA5;
    }

    /**
     * 是否含有某个中文
     * @param str
     * @return
     */
    public static boolean isContainChina(String str){
        if(TextUtils.isEmpty(str)){
            return false;
        }
        for(char c:str.toCharArray()){
            if(isChinese(c)){
                return true;
            }
        }
        return false;
    }


    public static  boolean isEquals(String org,String des){
        if(TextUtils.isEmpty(org)||TextUtils.isEmpty(des)){
            return false;
        }

        StringBuffer orgsb =new StringBuffer();
        StringBuffer dessb = new StringBuffer();

        return false;
    }

    /**
     * 判断集合是否为null或者0个元素
     * @param c
     * @return
     */
    public static boolean isNullOrEmpty(Collection c) {
        if (null == c || c.isEmpty()) {
            return true;
        }
        return false;
    }


}
