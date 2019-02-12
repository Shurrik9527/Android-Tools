package com.hz.maiku.maikumodule.util;

public class DeviceUtil {
//    private Context context;
//    private TelephonyManager phone;
//    private WifiManager wifi;
//    private Display display;
//    private DisplayMetrics metrics;
//
//    public DeviceUtil(Activity activity) {
//        context = activity.getApplicationContext();
//        phone = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
//        wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//        display = activity.getWindowManager().getDefaultDisplay();
//        metrics = activity.getResources().getDisplayMetrics();
//
//        DisplayMetrics book = new DisplayMetrics();
//        activity.getWindowManager().getDefaultDisplay().getMetrics(book);
//
//
//        //asking something
//        RxPermissions rxPermission = new RxPermissions(requireNonNull(activity));
//        rxPermission.request(Manifest.permission.READ_PHONE_STATE
//        ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Boolean>() {
//
//            @Override
//            public void onSubscribe(Disposable d) {
//
//            }
//
//            @Override
//            public void onNext(Boolean aBoolean) {
//                if (aBoolean) {
//                    showLog("imei", phone.getDeviceId());
//                    showLog("deviceversion", phone.getDeviceSoftwareVersion());
//                    //                            showLog("imsi", phone.getSubscriberId());
////                            showLog("number", phone.getLine1Number());
////                            showLog("simserial", phone.getSimSerialNumber());
//                }
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });
//
//        try {
//            Class localClass = Class.forName("android.os.SystemProperties");
//            Object localObject1 = localClass.newInstance();
//            Object localObject2 = localClass.getMethod("get", new Class[]{String.class, String.class}).invoke(localObject1, new Object[]{"gsm.version.baseband", "no message"});
//            Object localObject3 = localClass.getMethod("get", new Class[]{String.class, String.class}).invoke(localObject1, new Object[]{"ro.build.display.id", ""});
//
//
//            showLog("get", localObject2 + "");
//
//            showLog("osVersion", localObject3 + "");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//        //获取网络连接管理者
//        ConnectivityManager connectionManager = (ConnectivityManager)
//                activity.getSystemService(CONNECTIVITY_SERVICE);
//        //获取网络的状态信息，有下面三种方式
//        NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
//
//        showLog("lianwang", networkInfo.getType() + "");
//        showLog("lianwangname", networkInfo.getTypeName());
////        showLog("imei", phone.getDeviceId());
////        showLog("deviceversion", phone.getDeviceSoftwareVersion());
////        showLog("imsi", phone.getSubscriberId());
////        showLog("number", phone.getLine1Number());
////        showLog("simserial", phone.getSimSerialNumber());
//        showLog("simoperator", phone.getSimOperator());
//        showLog("simoperatorname", phone.getSimOperatorName());
//        showLog("simcountryiso", phone.getSimCountryIso());
//        showLog("workType", phone.getNetworkType() + "");
//        showLog("netcountryiso", phone.getNetworkCountryIso());
//        showLog("netoperator", phone.getNetworkOperator());
//        showLog("netoperatorname", phone.getNetworkOperatorName());
//
//
//        showLog("radiovis", android.os.Build.getRadioVersion());
//        showLog("wifimac", wifi.getConnectionInfo().getMacAddress());
//        showLog("getssid", wifi.getConnectionInfo().getSSID());
//        showLog("getbssid", wifi.getConnectionInfo().getBSSID());
//        showLog("ip", wifi.getConnectionInfo().getIpAddress() + "");
//        showLog("bluemac", BluetoothAdapter.getDefaultAdapter().getAddress());
//        showLog("bluname", BluetoothAdapter.getDefaultAdapter().getName());
//
//        showLog("cpu", getCpuName());
//
//
//        showLog("andrlid_id", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));
//        showLog("serial", android.os.Build.SERIAL);
//        showLog("brand", android.os.Build.BRAND);
//        showLog("tags", android.os.Build.TAGS);
//        showLog("device", android.os.Build.DEVICE);
//        showLog("fingerprint", android.os.Build.FINGERPRINT);
//        showLog("bootloader", Build.BOOTLOADER);
//        showLog("release", Build.VERSION.RELEASE);
//        showLog("sdk", Build.VERSION.SDK);
//        showLog("sdk_INT", Build.VERSION.SDK_INT + "");
//        showLog("codename", Build.VERSION.CODENAME);
//        showLog("incremental", Build.VERSION.INCREMENTAL);
//        showLog("cpuabi", android.os.Build.CPU_ABI);
//        showLog("cpuabi2", android.os.Build.CPU_ABI2);
//        showLog("board", android.os.Build.BOARD);
//        showLog("model", android.os.Build.MODEL);
//        showLog("product", android.os.Build.PRODUCT);
//        showLog("type", android.os.Build.TYPE);
//        showLog("user", android.os.Build.USER);
//        showLog("disply", android.os.Build.DISPLAY);
//        showLog("hardware", android.os.Build.HARDWARE);
//        showLog("host", android.os.Build.HOST);
//        showLog("changshang", android.os.Build.MANUFACTURER);
//        showLog("phonetype", phone.getPhoneType() + "");
//        showLog("simstate", phone.getSimState() + "");
//        showLog("b_id", Build.ID);
//        showLog("gjtime", android.os.Build.TIME + "");
//        showLog("width", display.getWidth() + "");
//        showLog("height", display.getHeight() + "");
//        showLog("dpi", book.densityDpi + "");
//        showLog("density", book.density + "");
//        showLog("xdpi", book.xdpi + "");
//        showLog("ydpi", book.ydpi + "");
//        showLog("scaledDensity", book.scaledDensity + "");
//
//
//        //showLog("wl,getNetworkState(this)+"");
//        // 方法2
//        DisplayMetrics dm = new DisplayMetrics();
//        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int width = dm.widthPixels;
//        int height = dm.heightPixels;
//
//        showLog("xwidth", width + "");
//        showLog("xheight", height + "");
//
//
//    }
//
//    private void showLog(String tag, String value) {
//        LogUtil.d(tag, value);
//    }
//
//    /**
//     * 获取CPU型号
//     *
//     * @return
//     */
//    public String getCpuName() {
//
//        String str1 = "/proc/cpuinfo";
//        String str2 = "";
//
//        try {
//            FileReader fr = new FileReader(str1);
//            BufferedReader localBufferedReader = new BufferedReader(fr);
//            while ((str2 = localBufferedReader.readLine()) != null) {
//                if (str2.contains("Hardware")) {
//                    return str2.split(":")[1];
//                }
//            }
//            localBufferedReader.close();
//        } catch (IOException e) {
//        }
//        return null;
//
//    }
}
