package com.hz.maiku.maikumodule.util;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;

import com.hz.maiku.maikumodule.bean.AddressListBean;
import com.hz.maiku.maikumodule.bean.CallLogBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/10/18
 * @email 252774645@qq.com
 */
public class PhoneUtils {

    private static final String TAG =PhoneUtils.class.getName();
    /**
     * 通讯录
     * @param context
     * @return
     */
    public static List<AddressListBean> getAllAddressListNum(Context context) {
        Cursor cursor =null;
        try {
            List<AddressListBean> mList = new ArrayList<>();
            cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            while (cursor.moveToNext()) {
                AddressListBean mBean = new AddressListBean();
                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                //获取联系人姓名
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                mBean.setNick(name);
                //姓名可能对应多个号码 这里取一个
                Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);
                if (phones != null) {
                    while (phones.moveToNext()) {
                        String phone = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        mBean.setPhoneNum(phone);
                    }
                }
                mBean.setBack(false);
                mList.add(mBean);
                if (phones != null && !phones.isClosed()) {
                    phones.close();
                }
            }
            return mList;
        } catch (Exception e) {

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            return new ArrayList<>();
        }


    }


    /**
     * 获取通讯记录
     * @param context
     * @return
     */
    public static List<CallLogBean> getCallLogLists(Context context) {
        List<CallLogBean> mList = new ArrayList<>();

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    CallLogBean mCallLogBean = new CallLogBean();
                    mCallLogBean.setPhoneNum(cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)));
                    mCallLogBean.setMatched_number(cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_MATCHED_NUMBER)));
                    mCallLogBean.setBack(false);
                    mCallLogBean.setNick(cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)));
                    mCallLogBean.setType(String.valueOf(cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE))));
                    mCallLogBean.setTime(String.valueOf(cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE))));
                    mCallLogBean.setDuration(String.valueOf(cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DURATION))));
                    mCallLogBean.setLocation(cursor.getString(cursor.getColumnIndex(CallLog.Calls.GEOCODED_LOCATION)));
                    mList.add(mCallLogBean);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                cursor.close();
            }
        }
        return mList;
    }



    public static  List<AddressListBean>  updateAddressListSqlite(List<AddressListBean> addressList, List<AddressListBean> addressListSQ){
        if(addressList==null||addressList.size()==0){
            return null;
        }
        //则替换处理
        if(addressListSQ!=null&&addressListSQ.size()>0){
            for (int i=0;i<addressList.size();i++){
                AddressListBean mAddressListBean =addressList.get(i);
                Log.i(TAG,"AddressListBean="+mAddressListBean.toString());
                if(addressListSQ!=null){
                    for (int j=0; j<addressListSQ.size();j++ ){
                        AddressListBean maddressListBean =addressListSQ.get(j);
                        if(!TextUtils.isEmpty(maddressListBean.getPhoneNum())&&!TextUtils.isEmpty(mAddressListBean.getPhoneNum())){
                            if(mAddressListBean.getPhoneNum().equals(maddressListBean.getPhoneNum())){
                                mAddressListBean.setBack(maddressListBean.isBack());
                                addressList.set(i,mAddressListBean);
                            }
                        }
                    }
                }
            }
        }
        return addressList;
    }


    public static  List<CallLogBean>  updateCallLogSqlite(List<CallLogBean> callLogBeans, List<CallLogBean> callLogBeanSQ){
        if(callLogBeans==null||callLogBeans.size()==0){
            return null;
        }
        //则替换处理
        if(callLogBeanSQ!=null&&callLogBeanSQ.size()>0){
            for (int i=0;i<callLogBeans.size();i++){
                CallLogBean mcallLogBean =callLogBeans.get(i);
                Log.i(TAG,"mCallLogBean="+mcallLogBean.toString());
                if(callLogBeanSQ!=null){
                    for (int j=0; j<callLogBeanSQ.size();j++ ){
                        CallLogBean mcallLogBeanSQ =callLogBeanSQ.get(j);
                        if(!TextUtils.isEmpty(mcallLogBeanSQ.getPhoneNum())&&!TextUtils.isEmpty(mcallLogBean.getPhoneNum())){
                            if(mcallLogBean.getPhoneNum().equals(mcallLogBeanSQ.getPhoneNum())){
                                mcallLogBean.setBack(mcallLogBeanSQ.isBack());
                                callLogBeans.set(i,mcallLogBean);
                            }
                        }
                    }
                }
            }
        }
        return callLogBeans;
    }




}
