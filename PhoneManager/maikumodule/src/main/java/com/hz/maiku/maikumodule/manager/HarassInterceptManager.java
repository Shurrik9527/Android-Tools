package com.hz.maiku.maikumodule.manager;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.hz.maiku.maikumodule.bean.AddressListBean;
import com.hz.maiku.maikumodule.bean.CallLogBean;
import com.hz.maiku.maikumodule.bean.SmsBean;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import static org.litepal.crud.DataSupport.where;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 电话拦截数据库管理类
 * @date 2018/10/11
 * @email 252774645@qq.com
 */
public class HarassInterceptManager {

    private static final String TAG = HarassInterceptManager.class.getName();
    private PackageManager mPackageManager;
    private Context mContext;

    public HarassInterceptManager(Context mContext) {
        this.mContext = mContext;
        mPackageManager = mContext.getPackageManager();
    }

    /**
     * 查找所有通讯记录
     */
    public synchronized List<CallLogBean> getAllPhoneInfos() {
        try {
            return DataSupport.findAll(CallLogBean.class);
        }catch (Exception e){

        }
        return new ArrayList<>();
    }



    /**
     * 查找所有通讯录
     */
    public synchronized List<AddressListBean> getAllAddressListInfos() {
        try {
            return DataSupport.findAll(AddressListBean.class);
        }catch (Exception e){

        }
        return new ArrayList<>();
    }

    /**
     * 查找所有通讯记录
     */
    public synchronized List<SmsBean> getAllSMSInfos() {
        try {
            return DataSupport.findAll(SmsBean.class);
        }catch (Exception e){

        }
        return new ArrayList<>();
    }



    /**
     * 删除通讯记录
     */
    public synchronized void deleteCallLogInfoTable(List<CallLogBean> commPhoneInfos) {
        try {
            for (CallLogBean info : commPhoneInfos) {
                DataSupport.deleteAll(CallLogBean.class, "time = ?", info.getTime());
            }
        }catch (Exception e){

        }
    }

    /**
     * 删除通讯记录
     */
    public synchronized void deleteSMSInfo(List<SmsBean> mSmsBean) {
        try {
            for (SmsBean info : mSmsBean) {
                DataSupport.deleteAll(SmsBean.class, "time = ?", info.getDate());
            }
        }catch (Exception e){

        }
    }

    /**
     * 删除所有通讯记录
     */
    public synchronized void deleteAllCallLogInfo() {
        try {
            DataSupport.deleteAll(CallLogBean.class);
        }catch (Exception e){

        }
    }

    /**
     * 删除所有通讯记录
     */
    public synchronized void deleteAllSMSInfo() {
        try {
            DataSupport.deleteAll(SmsBean.class);
        }catch (Exception e){

        }
    }

    /**
     * 删除所有通讯录
     */
    public synchronized void deleteAllAddressListInfo() {
        try {
            DataSupport.deleteAll(AddressListBean.class);
        }catch (Exception e){

        }
    }


    /**
     * 删除通讯录
     */
    public synchronized void deleteAddressListInfoTable(List<AddressListBean> addressLists) {
        try {
            for (AddressListBean info : addressLists) {
                DataSupport.deleteAll(AddressListBean.class, "phoneNum = ?", info.getPhoneNum());
            }
        }catch (Exception e){

        }
    }


    /**
     * 将所有通讯记录插入数据库
     */
    public synchronized void instanceAllSMSInfoTable(List<SmsBean> mlists) throws PackageManager.NameNotFoundException {
        DataSupport.saveAll(mlists);
    }

    /**
     * 将所有通讯记录插入数据库
     */
    public synchronized void instanceAllCallLogInfoTable(List<CallLogBean> mlists) throws PackageManager.NameNotFoundException {
        try {
            if(mlists!=null&&mlists.size()>0){
                DataSupport.saveAll(mlists);
            }
        }catch (Exception e){

        }
    }


    /**
     * 将所欲通讯信息插入数据库
     */
    public synchronized void instanceAddressListInfoTable(List<AddressListBean> mlists) throws PackageManager.NameNotFoundException {
        try {
            if(mlists!=null&&mlists.size()>0){
                DataSupport.saveAll(mlists);
            }
        }catch (Exception e){

        }
    }



    /**
     * 更改数据库拉黑
     */
    public void blackPhone(String phoneNum) {
        updateBlackStatus(phoneNum, true);
    }

    /**
     * 更改数据库去黑
     */
    public void unBlackPhone(String phoneNum) {
        updateBlackStatus(phoneNum, false);
    }

    public void updateBlackStatus(String phoneNum, boolean isSure) {
        ContentValues values = new ContentValues();
        values.put("isBack", isSure);
        int size =DataSupport.updateAll(CallLogBean.class, values, "phoneNum = ?", phoneNum);
        Log.i(TAG,"变化了几个size="+size);
    }



    /**
     * 更改数据库拉黑
     */
    public void blackSMS(String phoneNum) {
        updateSMSBlackStatus(phoneNum, true);
    }
    /**
     * 更改数据库去黑
     */
    public void unBlackSMS(String phoneNum) {
        updateSMSBlackStatus(phoneNum, false);
    }

    public void updateSMSBlackStatus(String phoneNum, boolean isSure) {
        ContentValues values = new ContentValues();
        values.put("isBack", isSure);
        int size =DataSupport.updateAll(SmsBean.class, values, "address = ?", phoneNum);
        Log.i(TAG,"变化了几个size="+size);
    }


    /**
     * 是否是黑户
     * @param phoneNum
     * @return
     */
    public boolean isBlack(String phoneNum) {
        List<CallLogBean> mCallLogBeans = where("phoneNum = ?", phoneNum).find(CallLogBean.class);
        for (CallLogBean mCallLogBean : mCallLogBeans) {
            if (mCallLogBean.isBack()) {
                return true;
            }
        }
        return false;
    }


    /**
     * 是否是黑户
     * @param phoneNum
     * @return
     */
    public boolean isSMSBlack(String phoneNum) {
        List<SmsBean> mSmsBeans = where("address = ?", phoneNum).find(SmsBean.class);
        for (SmsBean mSmsBean : mSmsBeans) {
            if (mSmsBean.isBack()) {
                return true;
            }
        }
        return false;
    }



}
