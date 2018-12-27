package com.hz.maiku.maikumodule.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import com.hz.maiku.maikumodule.manager.HarassInterceptManager;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/10/19
 * @email 252774645@qq.com
 */
public class HarassInterceptService extends IntentService{
    public static final String TAG = HarassInterceptService.class.getName();
    private HarassInterceptManager manager;


    public HarassInterceptService() {
        super("HarassInterceptService");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        manager = new HarassInterceptManager(this);
    }


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public HarassInterceptService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {


//
//        //手机本身获取数据
//        List<AddressListBean> addressList = PhoneUtils.getAllAddressListNum(this);
//        List<CallLogBean> callLog =PhoneUtils.getCallLogLists(this);
//        //数据库获取内容
//        List<AddressListBean> addressListSQ=manager.getAllAddressListInfos();
//        List<CallLogBean> callLogSQ=manager.getAllPhoneInfos();
//
//        //更新通讯记录
//        if(callLog!=null&&callLog.size()>0){
//            for (int i=0;i<callLog.size();i++){
//                CallLogBean mCallLogBean =callLog.get(i);
//                Log.i(TAG,"mCallLogBean="+mCallLogBean.toString());
//                if(callLogSQ!=null){
//                    for (int j=0; j<callLogSQ.size();j++ ){
//                        CallLogBean mcallLogBean =callLogSQ.get(j);
//                        if(!TextUtils.isEmpty(mCallLogBean.getPhoneNum())&&!TextUtils.isEmpty(mcallLogBean.getPhoneNum())){
//                            if(mCallLogBean.getPhoneNum().equals(mcallLogBean.getPhoneNum())){
//                                mCallLogBean.setBack(mcallLogBean.isBack());
//                            }
//                        }
//                    }
//                }
//            }
//            if(callLogSQ!=null&&callLogSQ.size()>0){
//                manager.deleteAllCallLogInfo();
//            }
//            try {
//                manager.instanceAllCallLogInfoTable(callLog);
//                Log.e(TAG,"更新通讯记录成功...");
//            } catch (PackageManager.NameNotFoundException e) {
//                e.printStackTrace();
//            }
//
//        }
//
//        //更新通讯录
//        if(addressList!=null&&addressList.size()>0){
//            for (int i=0;i<addressList.size();i++){
//                AddressListBean mAddressListBean =addressList.get(i);
//                if(addressListSQ!=null){
//                    for (int j=0; j<addressListSQ.size();j++ ){
//                        AddressListBean maddressListBean =addressListSQ.get(j);
//                        if(!TextUtils.isEmpty(maddressListBean.getPhoneNum())&&!TextUtils.isEmpty(mAddressListBean.getPhoneNum())){
//                            if(mAddressListBean.getPhoneNum().equals(maddressListBean.getPhoneNum())){
//                                mAddressListBean.setBack(maddressListBean.isBack());
//                            }
//                        }
//                    }
//                }
//            }
//            if(addressListSQ!=null&&addressListSQ.size()>0){
//                manager.deleteAllAddressListInfo();
//                Log.e(TAG,"更新通讯录成功...");
//            }
//            try {
//                manager.instanceAddressListInfoTable(addressList);
//            } catch (PackageManager.NameNotFoundException e) {
//                e.printStackTrace();
//            }
//        }


    }
}
