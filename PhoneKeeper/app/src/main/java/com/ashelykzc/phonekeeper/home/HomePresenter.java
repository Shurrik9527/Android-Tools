package com.ashelykzc.phonekeeper.home;

public class HomePresenter implements HomeContract.Presenter {
    private static final String TAG = HomePresenter.class.getName();
    private HomeContract.View mView;

    public HomePresenter(HomeContract.View view) {
        this.mView = view;
        this.mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }


//    @Override
//    public void deviceInform(Context context) {
//        Observable.create(new ObservableOnSubscribe<DeviceInformBean>() {
//            @Override
//            public void subscribe(ObservableEmitter<DeviceInformBean> e) throws Exception {
//                e.onNext(DeviceUtil.getmInstance().getDeviceInform(context, Constant.APP_NAME));
//            }
//        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<DeviceInformBean>() {
//            @Override
//            public void accept(DeviceInformBean deviceInformBean) throws Exception {
//                if (mView != null) {
//                    mView.uploadDeviceInform(deviceInformBean);
//                }
//            }
//        });
//    }
//
//    @Override
//    public void uploadDeviceInform(DeviceInformBean deviceInformBean) {
//
//        if (deviceInformBean == null) {
//            return;
//        }
//
//        HttpCenter.getService().uploadDeviceInform(deviceInformBean.getADNROID_ID(), deviceInformBean.getAID(), deviceInformBean.getAPP_NAME(), deviceInformBean.getPHONE_NUMBER(), deviceInformBean.getOPERATOR(), deviceInformBean.getNETWORK_TYPE(), deviceInformBean.getCOUNTRY_CODE(), deviceInformBean.getOPERATOR_CODE(),
//                deviceInformBean.getSIM_STATUS(), deviceInformBean.getSIM_SERAIL_NUMBER(), deviceInformBean.getSERAIL_NUMBER(), deviceInformBean.getMAC(),
//                deviceInformBean.getBLOOTH_MAC(), deviceInformBean.getIMEI(), deviceInformBean.getIMSI(), deviceInformBean.getSSID(), deviceInformBean.getBSSID(), deviceInformBean.getUSER_AGEN(),
//                deviceInformBean.getNETWORK_OPERATOR(), deviceInformBean.getNETWORK_OPERATOR_NAME(), deviceInformBean.getNETWORK_OPERATOR_COUNTRY_CODE(), deviceInformBean.getTIME_ZONE_ID(),
//                deviceInformBean.getSCREEN_WIDTH(), deviceInformBean.getSCREEN_HEIGHT(), deviceInformBean.getCPU_INFO(), deviceInformBean.getBOARD(), deviceInformBean.getBOOTLOADER(), deviceInformBean.getBRAND(), deviceInformBean.getCPU_ABI(),
//                deviceInformBean.getCPU_ABI2(), deviceInformBean.getDISPLAY(), deviceInformBean.getFINGERPRINT(), deviceInformBean.getHARDWARE(), deviceInformBean.getHOST(), deviceInformBean.getDEVICE(), deviceInformBean.getMODEL(), deviceInformBean.getMANUFACTURER(), deviceInformBean.getPRODUCT(),
//                deviceInformBean.getRADIO(), deviceInformBean.getTAGS(), deviceInformBean.getTIME(), deviceInformBean.getTYPE(), deviceInformBean.getUSER(), deviceInformBean.getVERSION_RELEASE(),
//                deviceInformBean.getVERSION_CODENAME(), deviceInformBean.getVERSION_INCREMENTAL(), deviceInformBean.getVERSION_SDK(), deviceInformBean.getVERSION_SDK_INT()).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<HttpResult<String>>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//            }
//
//            @Override
//            public void onNext(HttpResult<String> result) {
//                if (result.getResult() == 0) {
//                    SpHelper.getInstance().put(Constant.UPLOAD_DEVICE_INFORM, "1");
//                    Log.i(TAG, "设备信息提交成功!");
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
//            }
//        });
//
//    }
}
