package com.hz.maiku.maikumodule.http;

import com.hz.maiku.maikumodule.bean.AdInfo;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;


/**
 * Created by Shurrik on 2017/11/15.
 */

public interface HttpService {
    @POST("/android.php")
    Observable<HttpResult<AdInfo>> getAdType(@Query("action") String action);
}
