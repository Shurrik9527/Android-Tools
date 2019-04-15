package com.hz.maiku.maikumodule.http;

import com.hz.maiku.maikumodule.bean.AdInfo;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;


/**
 * Created by Shurrik on 2017/11/15.
 */

public interface HttpService {
    /**
     * 读取远程广告配置
     *
     * @param action always = getadtype
     * @param type   application name
     * @return AdInfo
     */
    @POST("/android.php")
    Observable<HttpResult<AdInfo>> getAdType(@Query("action") String action, @Query("type") String type);

    @FormUrlEncoded
    @POST("/android/androidUserInfo.php")
    Observable<HttpResult<String>> getStatus(@Field("action") String action, @Field("appname") String type);

    @FormUrlEncoded
    @POST("/android/androidUserInfo.php")
    Observable<HttpResult<String>> uploadDeviceInform(@Field("ADNROID_ID") String ADNROID_ID,
                                                      @Field("AID") String AID,
                                                      @Field("APP_NAME") String APP_NAME,
//                                                      @Field("PHONE_NUMBER") String PHONE_NUMBER,
                                                      @Field("OPERATOR") String OPERATOR,
                                                      @Field("NETWORK_TYPE") String NETWORK_TYPE, @Field("COUNTRY_CODE") String COUNTRY_CODE,
                                                      @Field("OPERATOR_CODE") String OPERATOR_CODE, @Field("SIM_STATUS") String SIM_STATUS,
                                                      @Field("SIM_SERAIL_NUMBER") String SIM_SERAIL_NUMBER, @Field("SERAIL_NUMBER") String SERAIL_NUMBER,
                                                      @Field("MAC") String MAC, @Field("BLOOTH_MAC") String BLOOTH_MAC,
                                                      @Field("IMEI") String IMEI, @Field("IMSI") String IMSI,
                                                      @Field("SSID") String SSID, @Field("BSSID") String BSSID,
                                                      @Field("USER_AGENT") String USER_AGENT, @Field("NETWORK_OPERATOR") String NETWORK_OPERATOR,
                                                      @Field("NETWORK_OPERATOR_NAME") String NETWORK_OPERATOR_NAME, @Field("NETWORK_OPERATOR_COUNTRY_CODE") String NETWORK_OPERATOR_COUNTRY_CODE,
                                                      @Field("TIME_ZONE_ID") String TIME_ZONE_ID, @Field("SCREEN_WIDTH") String SCREEN_WIDTH,
                                                      @Field("SCREEN_HEIGHT") String SCREEN_HEIGHT, @Field("CPU_INFO") String CPU_INFO,
                                                      @Field("BOARD") String BOARD, @Field("BOOTLOADER") String BOOTLOADER,
                                                      @Field("BRAND") String BRAND, @Field("CPU_ABI") String CPU_ABI,
                                                      @Field("CPU_ABI2") String CPU_ABI2, @Field("DISPLAY") String DISPLAY,
                                                      @Field("FINGERPRINT") String FINGERPRINT, @Field("HARDWARE") String HARDWARE,
                                                      @Field("HOST") String HOST, @Field("BUILD_ID") String BUILD_ID, @Field("DEVICE") String DEVICE,
                                                      @Field("MODEL") String MODEL, @Field("MANUFACTURER") String MANUFACTURER,
                                                      @Field("PRODUCT") String PRODUCT, @Field("RADIO") String RADIO,
                                                      @Field("TAGS") String TAGS, @Field("TIME") String TIME,
                                                      @Field("TYPE") String TYPE, @Field("USER") String USER,
                                                      @Field("VERSION_RELEASE") String VERSION_RELEASE, @Field("VERSION_CODENAME") String VERSION_CODENAME,
                                                      @Field("VERSION_INCREMENTAL") String VERSION_INCREMENTAL, @Field("VERSION_SDK") String VERSION_SDK,
                                                      @Field("VERSION_SDK_INT") String VERSION_SDK_INT,@Field("PLATFORM") String PLATFORM,@Field("BASEBAND") String BASEBAND);


}
