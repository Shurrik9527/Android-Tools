package com.hz.maiku.maikumodule.util;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebSettings;

import com.google.gson.Gson;
import com.hz.maiku.maikumodule.bean.DeviceInformBean;
import com.hz.maiku.maikumodule.manager.NotificationsManager;

import net.vidageek.mirror.dsl.Mirror;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.TimeZone;

public class DeviceUtil {


    private static final String TAG = NotificationsManager.class.getName();
    public static DeviceUtil mInstance;
    private Context mContext;
    private Context context;
    private TelephonyManager phone;
    private WifiManager wifi;

    //私有构造方法
    private DeviceUtil(Context context) {
        this.mContext = context;
        this.phone = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        this.wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    /**
     * 获取当前内存管理
     *
     * @return
     */
    public static DeviceUtil getmInstance() {
        if (mInstance == null) {
            throw new IllegalStateException("NotificationManager is not init...");
        }
        return mInstance;
    }

    /**
     * 初始化内存管理器
     *
     * @param context
     */
    public static void init(Context context) {
        if (mInstance == null) {
            synchronized (DeviceUtil.class) {
                if (mInstance == null) {
                    mInstance = new DeviceUtil(context);
                }
            }
        }
    }

    public DeviceInformBean getDeviceInform(Context context, String appName) {
        try {
            DeviceInformBean bean = new DeviceInformBean();
            bean.setADNROID_ID(Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
            bean.setAID(AppUtil.getAid(context));
            bean.setAPP_NAME(appName);
            if (phone != null) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return null;
                }
//                bean.setPHONE_NUMBER(phone.getLine1Number());
                bean.setOPERATOR(phone.getSimOperator());
                bean.setNETWORK_TYPE(String.valueOf(phone.getNetworkType()));
                bean.setCOUNTRY_CODE(phone.getSimCountryIso());
                bean.setOPERATOR_CODE(phone.getSimOperatorName());
                bean.setSIM_STATUS(String.valueOf(phone.getSimState()));
                bean.setSIM_SERAIL_NUMBER(phone.getSimSerialNumber());
                bean.setSERAIL_NUMBER(android.os.Build.SERIAL);
                bean.setMAC(getMac(mContext));
                bean.setBLOOTH_MAC(getBluetoothMac());
                bean.setIMEI(phone.getDeviceId());
                bean.setIMSI(phone.getSubscriberId());
                bean.setSSID(wifi.getConnectionInfo().getSSID());
                bean.setBSSID(wifi.getConnectionInfo().getBSSID());
                bean.setUSER_AGEN(getUserAgent(context));
                bean.setNETWORK_OPERATOR(phone.getNetworkOperator());
                bean.setNETWORK_OPERATOR_NAME(phone.getNetworkOperatorName());
                bean.setNETWORK_OPERATOR_COUNTRY_CODE(phone.getNetworkCountryIso());
                bean.setTIME_ZONE_ID(getTimeZoneId());
                bean.setSCREEN_WIDTH(String.valueOf(ScreenUtils.getScreenWidth()));
                bean.setSCREEN_HEIGHT(String.valueOf(ScreenUtils.getScreenHeight()));
                bean.setCPU_INFO(getCpuInform());
                bean.setBOARD(android.os.Build.BOARD);
                bean.setBOOTLOADER(Build.BOOTLOADER);
                bean.setBRAND(android.os.Build.BRAND);
                bean.setCPU_ABI(android.os.Build.CPU_ABI);
                bean.setCPU_ABI2(android.os.Build.CPU_ABI2);
                bean.setDISPLAY(android.os.Build.DISPLAY);
                bean.setFINGERPRINT(Build.FINGERPRINT);
                bean.setHARDWARE(Build.HARDWARE);
                bean.setHOST(Build.HOST);
                bean.setBUILD_ID(Build.ID);
                bean.setDEVICE(Build.DEVICE);
                bean.setMODEL(Build.MODEL);
                bean.setMANUFACTURER(Build.MANUFACTURER);
                bean.setPRODUCT(Build.PRODUCT);
                bean.setRADIO(Build.RADIO);
                bean.setTAGS(Build.TAGS);
                bean.setTIME(String.valueOf(Build.TIME));
                bean.setTYPE(Build.TYPE);
                bean.setUSER(Build.USER);
                bean.setVERSION_RELEASE(Build.VERSION.RELEASE);
                bean.setVERSION_CODENAME(Build.VERSION.CODENAME);
                bean.setVERSION_INCREMENTAL(Build.VERSION.INCREMENTAL);
                bean.setVERSION_SDK(Build.VERSION.SDK);
                bean.setVERSION_SDK_INT(String.valueOf(Build.VERSION.SDK_INT));
                bean.setPLATFORM(getPlatform());
                bean.setBASEBAND(getBaseband());
                Log.i(TAG, "DeviceInformBean=" + bean.toString());
                return bean;
            }

        } catch (Exception e) {

        }
        return new DeviceInformBean();
    }

    private static String getSerialNumber() {
        String serial = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            serial = (String) get.invoke(c, "ro.serialnocustom");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serial;
    }


    public static String getMac(Context context) {

        String strMac = null;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            strMac = getLocalMacAddressFromWifiInfo(context);
            return strMac;
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            strMac = getMacAddress(context);
            return strMac;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (!TextUtils.isEmpty(getMacAddress())) {
                strMac = getMacAddress();
                return strMac;
            } else if (!TextUtils.isEmpty(getMachineHardwareAddress())) {
                strMac = getMachineHardwareAddress();
                return strMac;
            } else {
                strMac = getLocalMacAddressFromBusybox();
                return strMac;
            }
        }
        return "02:00:00:00:00:00";
    }


    /**
     * android 6.0及以上、7.0以下 获取mac地址
     *
     * @param context
     * @return
     */
    public static String getMacAddress(Context context) {

        // 如果是6.0以下，直接通过wifimanager获取
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            String macAddress0 = getMacAddress0(context);
            if (!TextUtils.isEmpty(macAddress0)) {
                return macAddress0;
            }
        }
        String str = "";
        String macSerial = "";
        try {
            Process pp = Runtime.getRuntime().exec(
                    "cat /sys/class/net/wlan0/address");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (Exception ex) {
        }
        if (macSerial == null || "".equals(macSerial)) {
            try {
                return loadFileAsString("/sys/class/net/eth0/address")
                        .toUpperCase().substring(0, 17);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return macSerial;
    }

    private static String getMacAddress0(Context context) {
        if (isAccessWifiStateAuthorized(context)) {
            WifiManager wifiMgr = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = null;
            try {
                wifiInfo = wifiMgr.getConnectionInfo();
                return wifiInfo.getMacAddress();
            } catch (Exception e) {
            }

        }
        return "";

    }

    /**
     * Check whether accessing wifi state is permitted
     *
     * @param context
     * @return
     */
    private static boolean isAccessWifiStateAuthorized(Context context) {
        if (PackageManager.PERMISSION_GRANTED == context
                .checkCallingOrSelfPermission("android.permission.ACCESS_WIFI_STATE")) {
            return true;
        } else
            return false;
    }

    private static String loadFileAsString(String fileName) throws Exception {
        FileReader reader = new FileReader(fileName);
        String text = loadReaderAsString(reader);
        reader.close();
        return text;
    }

    private static String loadReaderAsString(Reader reader) throws Exception {
        StringBuilder builder = new StringBuilder();
        char[] buffer = new char[4096];
        int readLength = reader.read(buffer);
        while (readLength >= 0) {
            builder.append(buffer, 0, readLength);
            readLength = reader.read(buffer);
        }
        return builder.toString();
    }


    /**
     * 根据IP地址获取MAC地址
     *
     * @return
     */
    public static String getMacAddress() {
        String strMacAddr = null;
        try {
            // 获得IpD地址
            InetAddress ip = getLocalInetAddress();
            byte[] b = NetworkInterface.getByInetAddress(ip)
                    .getHardwareAddress();
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < b.length; i++) {
                if (i != 0) {
                    buffer.append(':');
                }
                String str = Integer.toHexString(b[i] & 0xFF);
                buffer.append(str.length() == 1 ? 0 + str : str);
            }
            strMacAddr = buffer.toString().toUpperCase();
        } catch (Exception e) {
        }
        return strMacAddr;
    }

    /**
     * 获取移动设备本地IP
     *
     * @return
     */
    private static InetAddress getLocalInetAddress() {
        InetAddress ip = null;
        try {
            // 列举
            Enumeration<NetworkInterface> en_netInterface = NetworkInterface
                    .getNetworkInterfaces();
            while (en_netInterface.hasMoreElements()) {// 是否还有元素
                NetworkInterface ni = (NetworkInterface) en_netInterface
                        .nextElement();// 得到下一个元素
                Enumeration<InetAddress> en_ip = ni.getInetAddresses();// 得到一个ip地址的列举
                while (en_ip.hasMoreElements()) {
                    ip = en_ip.nextElement();
                    if (!ip.isLoopbackAddress()
                            && ip.getHostAddress().indexOf(":") == -1)
                        break;
                    else
                        ip = null;
                }

                if (ip != null) {
                    break;
                }
            }
        } catch (SocketException e) {

            e.printStackTrace();
        }
        return ip;
    }

    /**
     * 获取本地IP
     *
     * @return
     */
    private static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * android 7.0及以上 （2）扫描各个网络接口获取mac地址
     *
     */
    /**
     * 获取设备HardwareAddress地址
     *
     * @return
     */
    public static String getMachineHardwareAddress() {
        Enumeration<NetworkInterface> interfaces = null;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        String hardWareAddress = null;
        NetworkInterface iF = null;
        if (interfaces == null) {
            return null;
        }
        while (interfaces.hasMoreElements()) {
            iF = interfaces.nextElement();
            try {
                hardWareAddress = bytesToString(iF.getHardwareAddress());
                if (hardWareAddress != null)
                    break;
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        return hardWareAddress;
    }

    /***
     * byte转为String
     *
     * @param bytes
     * @return
     */
    private static String bytesToString(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        StringBuilder buf = new StringBuilder();
        for (byte b : bytes) {
            buf.append(String.format("%02X:", b));
        }
        if (buf.length() > 0) {
            buf.deleteCharAt(buf.length() - 1);
        }
        return buf.toString();
    }


    /**
     * android 7.0及以上 （3）通过busybox获取本地存储的mac地址
     *
     */

    /**
     * 根据busybox获取本地Mac
     *
     * @return
     */
    public static String getLocalMacAddressFromBusybox() {
        String result = "";
        String Mac = "";
        result = callCmd("busybox ifconfig", "HWaddr");
        // 如果返回的result == null，则说明网络不可取
        if (result == null) {
            return "网络异常";
        }
        // 对该行数据进行解析
        // 例如：eth0 Link encap:Ethernet HWaddr 00:16:E8:3E:DF:67
        if (result.length() > 0 && result.contains("HWaddr") == true) {
            Mac = result.substring(result.indexOf("HWaddr") + 6,
                    result.length() - 1);
            result = Mac;
        }
        return result;
    }

    private static String callCmd(String cmd, String filter) {
        String result = "";
        String line = "";
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            InputStreamReader is = new InputStreamReader(proc.getInputStream());
            BufferedReader br = new BufferedReader(is);

            while ((line = br.readLine()) != null
                    && line.contains(filter) == false) {
                result += line;
            }

            result = line;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 根据wifi信息获取本地mac
     *
     * @param context
     * @return
     */
    public static String getLocalMacAddressFromWifiInfo(Context context) {
        try {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo winfo = wifi.getConnectionInfo();
            String mac = winfo.getMacAddress();
            return mac;
        }catch (Exception e){
            return "";
        }
    }

    private static String getUserAgent(Context context) {
//        return new WebView(context).getSettings().getUserAgentString();
        try {
            String userAgent = "";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                try {
                    userAgent = WebSettings.getDefaultUserAgent(context);
                } catch (Exception e) {
                    userAgent = System.getProperty("http.agent");
                }
            } else {
                userAgent = System.getProperty("http.agent");
            }
            StringBuffer sb = new StringBuffer();
            for (int i = 0, length = userAgent.length(); i < length; i++) {
                char c = userAgent.charAt(i);
                if (c <= '\u001f' || c >= '\u007f') {
                    sb.append(String.format("\\u%04x", (int) c));
                } else {
                    sb.append(c);
                }
            }
            return sb.toString();
        }catch (Exception e){
            return "";
        }

    }

    /**
     * 获取当前时区
     *
     * @return
     */
    public static String getTimeZoneId() {
        TimeZone tz = TimeZone.getDefault();
        String strTz = tz.getDisplayName(false, TimeZone.SHORT);
        return strTz;
    }

    /**
     *获得基带版本
     * @return String
     */
    public static String getBaseband(){
        String Version = "";
        try {
            Class cl = Class.forName("android.os.SystemProperties");
            Object invoker = cl.newInstance();
            Method m = cl.getMethod("get", new Class[] { String.class,String.class });
            Object result = m.invoke(invoker, new Object[]{"gsm.version.baseband", "no message"});
            Version = (String)result;
        } catch (Exception e) {
        }
        return Version;
    }

    public static String getPlatform(){
        return  getProp("ro.board.platform");
    }



    public static String getCpuInform() {
        try {
            Map localHashMap = new IdentityHashMap();
            ProcessBuilder localBuilder = new ProcessBuilder(new String[]{"/system/bin/cat", "/proc/cpuinfo"});
            Process localProcess = localBuilder.start();
            InputStreamReader localObject2 = new InputStreamReader(localProcess.getInputStream());
            BufferedReader a = new BufferedReader(localObject2);
            StringBuffer buffer = new StringBuffer();
            for (; ; ) {
                String line = a.readLine();
                if (line == null) {
                    break;
                }
                Log.i(TAG,"line.trim()"+line);
                if (!line.trim().equals("")) {
                    String[] sp = line.split(":");
                    if(sp.length>1){
                        buffer.append(sp[0].trim()).append(":");
                        buffer.append(sp[1].trim()).append(",");
                    }

                }
            }
            Gson gson2=new Gson();
            String str=gson2.toJson(buffer.toString().substring(0,buffer.toString().length()-1));
            return str;
        } catch (IOException localIOException) {
        }
        return "";
    }





    public static  String getProp(String key){
        try{
            //命令窗口输入命令
            Process p=Runtime.getRuntime().exec("getprop");
            //从命令中提取的输入流
            InputStream in=p.getInputStream();
            InputStreamReader reader=new InputStreamReader(in);
            BufferedReader buff=new BufferedReader(reader);
            //逐行读取并输出
            String line="";
            while((line=buff.readLine())!=null){
                if(line.contains("["+key+"]")){
                    String[] s=line.split("\\[");
                    //返回值
                    return s[2].replaceAll("\\].*", "");
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        //如果没取到就返回这个
        return "";
    }

    /**
     * 获取蓝牙MAC
     * @return
     */
    public static String getBluetoothMac() {
        try {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            Object bluetoothManagerService = new Mirror().on(bluetoothAdapter).get().field("mService");
            if (bluetoothManagerService == null) {
                Log.w(TAG, "couldn't find bluetoothManagerService");
                return BluetoothAdapter.getDefaultAdapter().getAddress();
            }
            Object address = new Mirror().on(bluetoothManagerService).invoke().method("getAddress").withoutArgs();
            if (address != null && address instanceof String) {
                Log.w(TAG, "using reflection to get the BT MAC address: " + address);
                return (String) address;
            } else {
                return BluetoothAdapter.getDefaultAdapter().getAddress();
            }
        }catch (Exception e){
            return "";
        }

    }



}
