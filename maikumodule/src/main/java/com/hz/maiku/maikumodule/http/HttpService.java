package com.hz.maiku.maikumodule.http;

import com.hz.maiku.maikumodule.bean.AdInfo;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;


/**
 * Created by Shurrik on 2017/11/15.
 */

public interface HttpService {
    /**
     * 读取远程广告配置
     * @param action always = getadtype
     * @param type application name
     * @return AdInfo
     */
    @POST("/android.php")
    Observable<HttpResult<AdInfo>> getAdType(@Query("action") String action, @Query("type") String type);
}
