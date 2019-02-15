package com.hz.maiku.maikumodule.base;


/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/9/6
 * @email 252774645@qq.com
 */
public class Constant {

    public static final int TYPE_TITLE = 0;
    public static final String SAVE_JUNK_CLEANER_TIME = "SAVE_JUNK_CLEANER_TIME";//上次垃圾扫描时间
    public static final String SAVE_JUNK_CLEANER_ISALL = "SAVE_JUNK_CLEANER_ISALL";//清理垃圾是否所有
    public static final String SAVE_CPU_COOLER_TIME = "SAVE_CPU_COOLER_TIME";//上次CPU降温时间

    public static final String APP_PACKAGE_NAME = MaiKuApp.getmContext().getPackageName(); //包名
    public static final String LOCK_IS_INIT_FAVITER = "lock_is_init_faviter"; //是否初始化了推荐数据库表
    public static final String LOCK_IS_INIT_DB = "lock_is_init_db"; //是否初始化了数据库表
    public static final String LOCK_IS_FIRST_LOCK = "is_lock"; //是否加过锁
    public static final String LOCK_AUTO_SCREEN = "lock_auto_screen"; //是否在手机屏幕关闭后再次锁定
    public static final String LOCK_AUTO_SCREEN_TIME = "lock_auto_screen_time"; //是否在手机屏幕关闭后一段时间再次锁定
    public static final String LOCK_STATE = "app_lock_state"; //应用锁开关(状态，true开，false关)
    public static final String LOCK_LAST_LOAD_PKG_NAME = "last_load_package_name";//最后保存的应用
    public static final String LOCK_CURR_MILLISENCONS = "lock_curr_milliseconds"; //记录当前的时间（毫秒）
    public static final String LOCK_APART_MILLISENCONS = "lock_apart_milliseconds"; //记录相隔的时间（毫秒）
    public static final String LOCK_PACKAGE_NAME = "lock_package_name"; //点开的锁屏应用的包名
    public static final String LOCK_FROM = "lock_from"; //解锁后转跳的action
    public static final String LOCK_FROM_FINISH = "lock_from_finish"; //解锁后转跳的action是finish
    public static final String LOCK_FROM_LOCK_MAIN_ACITVITY = "lock_from_lock_main_activity";
    public static final String LOCK_PERMISSION = "LOCK_PERMISSION";
    public static final String UPDATE_SQLITE = "UPDATE_SQLITE";
    public static final String UPDATE_SMS_ISFIRST_SQLITE = "UPDATE_SMS_ISFIRST_SQLITE";
    public static final String UPDATE_SMS_SQLITE = "UPDATE_SMS_SQLITE";
    public static final String UPDATE_PHONE_SQLITE = "UPDATE_PHONE_SQLITE";
    public static final String UPDATE_CONTRACT_SQLITE = "UPDATE_CONTRACT_SQLITE";
    public static final String WIFI_OPEN = "WIFI_OPEN";
    public static final String NOTIFICATION_SELECT_APP = "NOTIFICATION_SELECT_APP";
    public static final String NOTIFICATION_INIT_APP = "NOTIFICATION_INIT_APP";
    public static final String NOTIFICATION_OPEN_STATE = "NOTIFICATION_OPEN_STATE";
    public static final String ABOUT_SHOW_BTN = "ABOUT_SHOW_BTN";
    public static final String UPLOAD_DEVICE_INFORM = "UPLOAD_DEVICE_INFORM";
    public static final String REGIST_TIME="REGIST_TIME";
    public static final String SCAN_CLEAN_NUM="SCAN_CLEAN_NUM";
    /**
     * 域名
     */
    public static String BASE_URL;
    /**
     * 域名
     */
    public static String APP_NAME;
    /**
     * Admob
     */
    public static String UNIT_ID;
    /**
     * FacebookAd
     */
    public static String PLACEMENT_ID;
    /**
     * FacebookAd
     */
    public static int PID;
    /**
     * 百度Crab
     */
    public static String BAIDU_KEY;
    /**
     * AppsFlyer
     */
    public static String AF_DEV_KEY;
    /**
     * 宿主PackageName
     */
    public static String PACKAGE_NAME;
    /**
     * 宿主Gmail
     */
    public static String GMAIL;
}
