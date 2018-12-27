package com.hz.maiku.maikumodule.util;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import com.hz.maiku.maikumodule.bean.WifiBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @author heguogui
 * @version v 1.0.0
 * @describe wifi
 * @date 2018/11/20
 * @email 252774645@qq.com
 */
public class WifiHelper {

    private static final String TAG = WifiHelper.class.getName();

    public static final String WIFI_STATE_CONNECT = "connected";//已连接
    public static final String WIFI_STATE_ON_CONNECTING = "connecting";//正常连接
    public static final String WIFI_STATE_UNCONNECT = "unconnected";//未连接



    public enum WifiCipherType {
        WIFICIPHER_WEP, WIFICIPHER_WPA, WIFICIPHER_NOPASS, WIFICIPHER_INVALID
    }

    public WifiHelper() {
    }

    public static List<ScanResult> getWifiScanResult(Context context) {
        boolean b = context == null;
        return ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).getScanResults();
    }

    public static boolean isWifiEnable(Context context) {
        return ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).isWifiEnabled();
    }

    public static WifiInfo getConnectedWifiInfo(Context context) {
        return ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo();
    }

    public static List getConfigurations(Context context) {
        return ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).getConfiguredNetworks();
    }


    public static WifiConfiguration createWifiConfig(String SSID, String password, WifiCipherType type) {

        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";

        if (type == WifiCipherType.WIFICIPHER_NOPASS) {
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        }

        if (type == WifiCipherType.WIFICIPHER_WEP) {
            config.preSharedKey = "\"" + password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }

        if (type == WifiCipherType.WIFICIPHER_WPA) {
            config.preSharedKey = "\"" + password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;

        }

        return config;

    }

    /**
     * 接入某个wifi热点
     */
    public static boolean addNetWork(WifiConfiguration config, Context context) {

        WifiManager wifimanager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);

        WifiInfo wifiinfo = wifimanager.getConnectionInfo();

        if (null != wifiinfo) {
            wifimanager.disableNetwork(wifiinfo.getNetworkId());
        }

        boolean result = false;

        if (config.networkId > 0) {
            result = wifimanager.enableNetwork(config.networkId, true);
            wifimanager.updateNetwork(config);
        } else {

            int i = wifimanager.addNetwork(config);
            result = false;

            if (i > 0) {

                wifimanager.saveConfiguration();
                return wifimanager.enableNetwork(i, true);
            }
        }

        return result;

    }

    /**
     * 判断wifi热点支持的加密方式
     */
    public static WifiCipherType getWifiCipher(String s) {

        if (s.isEmpty()) {
            return WifiCipherType.WIFICIPHER_INVALID;
        } else if (s.contains("WEP")) {
            return WifiCipherType.WIFICIPHER_WEP;
        } else if (s.contains("WPA") || s.contains("WPA2") || s.contains("WPS")) {
            return WifiCipherType.WIFICIPHER_WPA;
        } else {
            return WifiCipherType.WIFICIPHER_NOPASS;
        }
    }

    //查看以前是否也配置过这个网络
    public static WifiConfiguration isExsits(String SSID, Context context) {
        WifiManager wifimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        List<WifiConfiguration> existingConfigs = wifimanager.getConfiguredNetworks();
        for (WifiConfiguration existingConfig : existingConfigs) {
            if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                return existingConfig;
            }
        }
        return null;
    }

    // 打开WIFI
    public static void openWifi(Context context) {
        WifiManager wifimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (!wifimanager.isWifiEnabled()) {
            wifimanager.setWifiEnabled(true);
        }
    }

    // 关闭WIFI
    public static void closeWifi(Context context) {
        WifiManager wifimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifimanager.isWifiEnabled()) {
            wifimanager.setWifiEnabled(false);
        }
    }

    public static boolean isOpenWifi(Context context){
        WifiManager wifimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        boolean b = wifimanager.isWifiEnabled();
        return b;
    }

    /**
     * 将idAddress转化成string类型的Id字符串
     *
     * @param idString
     * @return
     */
    public static String getStringId(int idString) {
        StringBuffer sb = new StringBuffer();
        int b = (idString >> 0) & 0xff;
        sb.append(b + ".");
        b = (idString >> 8) & 0xff;
        sb.append(b + ".");
        b = (idString >> 16) & 0xff;
        sb.append(b + ".");
        b = (idString >> 24) & 0xff;
        sb.append(b);
        return sb.toString();
    }

    /**
     * 设置安全性
     *
     * @param capabilities
     * @return
     */
    public static String getCapabilitiesString(String capabilities) {
        if (capabilities.contains("WEP")) {
            return "WEP";
        } else if (capabilities.contains("WPA") || capabilities.contains("WPA2") || capabilities.contains("WPS")) {
            return "WPA/WPA2";
        } else {
            return "OPEN";
        }
    }

    public static boolean getIsWifiEnabled(Context context) {
        WifiManager wifimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wifimanager.isWifiEnabled();
    }

    public static void getReplace(Context context, List<WifiBean> list) {
        WifiInfo wifi = getConnectedWifiInfo(context);
        List<WifiBean> listCopy = new ArrayList<>();
        listCopy.addAll(list);
        for (int i = 0; i < list.size(); i++) {
            if (("\"" + list.get(i).getWifiName() + "\"").equals(wifi.getSSID())) {
                listCopy.add(0, list.get(i));
                listCopy.remove(i + 1);
                listCopy.get(0).setState(WifiHelper.WIFI_STATE_CONNECT);
            }
        }
        list.clear();
        list.addAll(listCopy);
    }
    /**
     * 去除同名WIFI
     *
     * @param oldSr 需要去除同名的列表
     * @return 返回不包含同命的列表
     */
    public static List<ScanResult> noSameName(List<ScanResult> oldSr)
    {
        List<ScanResult> newSr = new ArrayList<ScanResult>();
        for (ScanResult result : oldSr)
        {
            if (!TextUtils.isEmpty(result.SSID) && !containName(newSr, result.SSID))
                newSr.add(result);
        }
        return newSr;
    }
    /**
     * 判断一个扫描结果中，是否包含了某个名称的WIFI
     * @param sr 扫描结果
     * @param name 要查询的名称
     * @return 返回true表示包含了该名称的WIFI，返回false表示不包含
     */
    public static boolean containName(List<ScanResult> sr, String name)
    {
        for (ScanResult result : sr)
        {
            if (!TextUtils.isEmpty(result.SSID) && result.SSID.equals(name))
                return true;
        }
        return false;
    }

    /**
     * 返回level 等级
     */
    public static int getLevel(int level){
        if (Math.abs(level) < 50) {
            return 1;
        } else if (Math.abs(level) < 75) {
            return 2;
        } else if (Math.abs(level) < 90) {
            return 3;
        } else {
            return 4;
        }
    }



    public static List<WifiBean> wifiScanResult(Context context) {
        List<ScanResult> scanResults = noSameName(getWifiScanResult(context));
        List<WifiBean> realWifiList = new ArrayList<>();
        if(!StringUtil.isNullOrEmpty(scanResults)){
            for(int i = 0;i < scanResults.size();i++){
                WifiBean wifiBean = new WifiBean();
                wifiBean.setWifiName(scanResults.get(i).SSID);
                wifiBean.setState(WIFI_STATE_UNCONNECT);   //只要获取都假设设置成未连接，真正的状态都通过广播来确定
                wifiBean.setCapabilities(scanResults.get(i).capabilities);
                wifiBean.setLevel(getLevel(scanResults.get(i).level)+"");
                realWifiList.add(wifiBean);
                //排序
                Collections.sort(realWifiList);
            }
        }
        return realWifiList;
    }








}
