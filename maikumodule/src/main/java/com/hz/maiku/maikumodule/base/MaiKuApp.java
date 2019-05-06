package com.hz.maiku.maikumodule.base;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.aiming.mdt.sdk.AdtAds;
import com.aiming.mdt.sdk.Callback;
import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.baidu.crabsdk.CrabSDK;
import com.hz.maiku.maikumodule.bean.AppProcessInfornBean;
import com.hz.maiku.maikumodule.exception.CustomCrashHandler;
import com.hz.maiku.maikumodule.manager.AddressListManager;
import com.hz.maiku.maikumodule.manager.CallLogManager;
import com.hz.maiku.maikumodule.manager.CleanManager;
import com.hz.maiku.maikumodule.manager.JunkCleanerManager;
import com.hz.maiku.maikumodule.manager.MemoryManager;
import com.hz.maiku.maikumodule.manager.NotificationsManager;
import com.hz.maiku.maikumodule.manager.ProcessManager;
import com.hz.maiku.maikumodule.manager.SMSManager;
import com.hz.maiku.maikumodule.modules.screenlocker.ScreenLockerService;
import com.hz.maiku.maikumodule.service.HideAppService;
import com.hz.maiku.maikumodule.service.LoadAppListService;
import com.hz.maiku.maikumodule.service.LockService;
import com.hz.maiku.maikumodule.util.DeviceUtil;
import com.hz.maiku.maikumodule.util.SpHelper;
import com.hz.maiku.maikumodule.util.ToastUtil;

import org.litepal.LitePalApplication;

import java.util.List;
import java.util.Map;


/**
 * Created by Shurrik on 2017/11/29.
 */

public class MaiKuApp extends LitePalApplication {

    private static final String TAG = MaiKuApp.class.getName();
    public static Context mContext;
    public static List<AppProcessInfornBean> cpuLists;

    @Override
    public void onCreate() {
        super.onCreate();
        this.mContext = this;

        initConfig();
        initManagers();
        initServices();
        initAppsFlyer();
        initBaiduCrab();

        //初始化Adtiming聚合平台
        String appKey = "8YC4dCDr8za3BGScWIaLVYaGqrrQ8K0L";
        AdtAds.init(this, appKey, new Callback() {
            @Override
            public void onSuccess() {
                //Adtiming SDK init Success
                Log.d(TAG, "Adtiming SDK init Success");
            }
            @Override
            public void onError(String msg) {
                //Adtiming SDK init Error
                Log.d(TAG, "Adtiming SDK init Error");
            }
        });
    }

    /**
     * 初始化全局配置
     */
    private void initConfig() {
        try {
            String BASE_URL = Config.getPropertiesURL(getApplicationContext(), "BASE_URL");
            String APP_NAME = Config.getPropertiesURL(getApplicationContext(), "APP_NAME");
            String UNIT_ID = Config.getPropertiesURL(getApplicationContext(), "UNIT_ID");
            String PLACEMENT_ID = Config.getPropertiesURL(getApplicationContext(), "PLACEMENT_ID");
            String PID = Config.getPropertiesURL(getApplicationContext(), "PID");
            String BAIDU_KEY = Config.getPropertiesURL(getApplicationContext(), "BAIDU_KEY");
            String AF_DEV_KEY = Config.getPropertiesURL(getApplicationContext(), "AF_DEV_KEY");
            String PACKAGE_NAME = Config.getPropertiesURL(getApplicationContext(), "PACKAGE_NAME");
            String GMAIL = Config.getPropertiesURL(getApplicationContext(), "GMAIL");

            if (!TextUtils.isEmpty(UNIT_ID)) {
                Constant.UNIT_ID = UNIT_ID;
            }
            if (!TextUtils.isEmpty(APP_NAME)) {
                Constant.APP_NAME = APP_NAME;
            }
            if (!TextUtils.isEmpty(BASE_URL)) {
                Constant.BASE_URL = BASE_URL;
            }
            if (!TextUtils.isEmpty(PLACEMENT_ID)) {
                Constant.PLACEMENT_ID = PLACEMENT_ID;
            }
            if (!TextUtils.isEmpty(PID)) {
                Constant.PID = Integer.parseInt(PID);
            }
            if (!TextUtils.isEmpty(BAIDU_KEY)) {
                Constant.BAIDU_KEY = BAIDU_KEY;
            }
            if (!TextUtils.isEmpty(AF_DEV_KEY)) {
                Constant.AF_DEV_KEY = AF_DEV_KEY;
            }
            if (!TextUtils.isEmpty(GMAIL)) {
                Constant.GMAIL = GMAIL;
            }
            if (!TextUtils.isEmpty(PACKAGE_NAME)) {
                Constant.PACKAGE_NAME = PACKAGE_NAME;
            }
        } catch (Exception e) {
            ToastUtil.showToast(this, "You must finish config.properties first.");
        }
    }


    private void initManagers() {
        MemoryManager.init(this);
        ProcessManager.init(getApplicationContext());
        CleanManager.init(this);
        JunkCleanerManager.init(this);
        SpHelper.getInstance().init(this);
        AddressListManager.init(getApplicationContext());
        CallLogManager.init(getApplicationContext());
        SMSManager.init(getApplicationContext());
        NotificationsManager.init(getApplicationContext());
        boolean isInit = (boolean) SpHelper.getInstance().get(Constant.NOTIFICATION_INIT_APP, false);
        if (!isInit) {
            NotificationsManager.getmInstance().initAppSelectState();
            SpHelper.getInstance().put(Constant.NOTIFICATION_INIT_APP, true);
        }

        DeviceUtil.init(getApplicationContext());
    }

    /**
     * 初始化服务
     */
    private void initServices() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(this, LoadAppListService.class));
        } else {
            startService(new Intent(this, LoadAppListService.class));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(this, HideAppService.class));
        } else {
            startService(new Intent(this, HideAppService.class));
        }


        boolean lockState = (boolean) SpHelper.getInstance().get(Constant.LOCK_STATE, false);
        if (lockState) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(new Intent(this, LockService.class));
            } else {
                startService(new Intent(this, LockService.class));
            }
        }


        //开启服务，开启锁屏界面
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(this, ScreenLockerService.class));
        } else {
            startService(new Intent(this, ScreenLockerService.class));
        }

    }

    /**
     * 初始化AppsFlyer
     */
    private void initAppsFlyer() {
        AppsFlyerConversionListener conversionDataListener =
                new AppsFlyerConversionListener() {
                    @Override
                    public void onInstallConversionDataLoaded(Map<String, String> conversionData) {
                        for (String attrName : conversionData.keySet()) {
                            Log.d(AppsFlyerLib.LOG_TAG, "attribute: " + attrName + " = " + conversionData.get(attrName));
                        }
                    }

                    @Override
                    public void onInstallConversionFailure(String errorMessage) {
                        Log.d(AppsFlyerLib.LOG_TAG, "error getting conversion data: " + errorMessage);
                    }

                    @Override
                    public void onAppOpenAttribution(Map<String, String> conversionData) {

                    }

                    @Override
                    public void onAttributionFailure(String errorMessage) {
                        Log.d(AppsFlyerLib.LOG_TAG, "error onAttributionFailure : " + errorMessage);
                    }
                };
        //Your dev key is accessible from the AppsFlyer Dashboard under the Configuration section inside App Settings
        AppsFlyerLib.getInstance().init(Constant.AF_DEV_KEY, conversionDataListener, getApplicationContext());
        AppsFlyerLib.getInstance().startTracking(this);
    }

    /**
     * 初始化百度Crab
     */
    private void initBaiduCrab() {
        CrabSDK.init(this, Constant.BAIDU_KEY);
        // 开启卡顿捕获功能, 传入每天上传卡顿信息个数，-1代表不限制, 已自动打开
        CrabSDK.enableBlockCatch(-1);
        CrabSDK.setUploadLimitOfSameCrashInOneday(-1);
        CrabSDK.setUploadLimitOfCrashInOneday(-1);
        CrabSDK.setBlockThreshold(2000);
        /* 初始化全局异常捕获信息 */
        CustomCrashHandler customCrashHandler = CustomCrashHandler.getInstance();
        customCrashHandler.init(this);
    }

    public static Context getmContext() {
        return mContext;
    }
}
