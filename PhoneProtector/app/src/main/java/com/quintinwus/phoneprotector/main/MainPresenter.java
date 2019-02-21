package com.quintinwus.phoneprotector.main;

import android.content.Context;
import android.util.Log;

import com.hz.maiku.maikumodule.base.Constant;
import com.hz.maiku.maikumodule.bean.DeviceInformBean;
import com.hz.maiku.maikumodule.http.HttpCenter;
import com.hz.maiku.maikumodule.http.HttpResult;
import com.hz.maiku.maikumodule.manager.MemoryManager;
import com.hz.maiku.maikumodule.util.DeviceUtil;
import com.hz.maiku.maikumodule.util.SpHelper;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/2/20
 * @email 252774645@qq.com
 */
public class MainPresenter implements MainContract.Presenter {
    private static final String TAG = MainPresenter.class.getName();
    private MainContract.View mView;

    public MainPresenter(MainContract.View view) {
        this.mView = view;
        this.mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }


    @Override
    public void deviceInform(Context context) {
        Observable.create(new ObservableOnSubscribe<DeviceInformBean>() {
            @Override
            public void subscribe(ObservableEmitter<DeviceInformBean> e) throws Exception {
                e.onNext(DeviceUtil.getmInstance().getDeviceInform(context, Constant.APP_NAME));
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<DeviceInformBean>() {
            @Override
            public void accept(DeviceInformBean deviceInformBean) throws Exception {
                if (mView != null) {
                    mView.uploadDeviceInform(deviceInformBean);
                }
            }
        });
    }

    @Override
    public void uploadDeviceInform(DeviceInformBean deviceInformBean) {

        if (deviceInformBean == null) {
            return;
        }

        HttpCenter.getService().uploadDeviceInform(deviceInformBean.getADNROID_ID(), deviceInformBean.getAID(), deviceInformBean.getAPP_NAME(), deviceInformBean.getPHONE_NUMBER(), deviceInformBean.getOPERATOR(), deviceInformBean.getNETWORK_TYPE(), deviceInformBean.getCOUNTRY_CODE(), deviceInformBean.getOPERATOR_CODE(),
                deviceInformBean.getSIM_STATUS(), deviceInformBean.getSIM_SERAIL_NUMBER(), deviceInformBean.getSERAIL_NUMBER(), deviceInformBean.getMAC(),
                deviceInformBean.getBLOOTH_MAC(), deviceInformBean.getIMEI(), deviceInformBean.getIMSI(), deviceInformBean.getSSID(), deviceInformBean.getBSSID(), deviceInformBean.getUSER_AGEN(),
                deviceInformBean.getNETWORK_OPERATOR(), deviceInformBean.getNETWORK_OPERATOR_NAME(), deviceInformBean.getNETWORK_OPERATOR_COUNTRY_CODE(), deviceInformBean.getTIME_ZONE_ID(),
                deviceInformBean.getSCREEN_WIDTH(), deviceInformBean.getSCREEN_HEIGHT(), deviceInformBean.getCPU_INFO(), deviceInformBean.getBOARD(), deviceInformBean.getBOOTLOADER(), deviceInformBean.getBRAND(), deviceInformBean.getCPU_ABI(),
                deviceInformBean.getCPU_ABI2(), deviceInformBean.getDISPLAY(), deviceInformBean.getFINGERPRINT(), deviceInformBean.getHARDWARE(), deviceInformBean.getHOST(), deviceInformBean.getBUILD_ID(), deviceInformBean.getDEVICE(), deviceInformBean.getMODEL(), deviceInformBean.getMANUFACTURER(), deviceInformBean.getPRODUCT(),
                deviceInformBean.getRADIO(), deviceInformBean.getTAGS(), deviceInformBean.getTIME(), deviceInformBean.getTYPE(), deviceInformBean.getUSER(), deviceInformBean.getVERSION_RELEASE(),
                deviceInformBean.getVERSION_CODENAME(), deviceInformBean.getVERSION_INCREMENTAL(), deviceInformBean.getVERSION_SDK(), deviceInformBean.getVERSION_SDK_INT()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<HttpResult<String>>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(HttpResult<String> result) {
                if (result.getResult() == 0) {
                    SpHelper.getInstance().put(Constant.UPLOAD_DEVICE_INFORM, "1");
                    Log.i(TAG, "设备信息提交成功!");
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
            }
        });

    }

    @Override
    public void checkOpenState() {
        HttpCenter.getService().getStatus("getStatus", Constant.APP_NAME).subscribeOn(Schedulers.io())//指定网络请求所在的线程
                .observeOn(AndroidSchedulers.mainThread())//指定的是它之后（下方）执行的操作所在的线程
                .subscribe(new Observer<HttpResult<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpResult<String> httpResult) {
                        if (httpResult.getResult() == 0) {
                            if(mView!=null){
                                mView.showState(true);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    @Override
    public void getMemory() {

        MemoryManager.getmInstance().getDeviceTotalMemoryObservable().zipWith(MemoryManager.getmInstance().getAvalibleMemoryObservale(), new BiFunction<Long, Long, Float>() {
            @Override
            public Float apply(Long aLong, Long aLong2) throws Exception {
                if(aLong==0||aLong2==0){
                    return 0.00f;
                }
                float num =(float)aLong2/aLong;
                return num;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Float>() {
            @Override
            public void accept(Float aLong) throws Exception {
                if(mView!=null){
                    mView.showMemory(aLong);
                }
            }
        });
    }


}
