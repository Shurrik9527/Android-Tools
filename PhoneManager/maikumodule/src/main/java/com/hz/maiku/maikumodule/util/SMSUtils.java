package com.hz.maiku.maikumodule.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;

import com.hz.maiku.maikumodule.bean.SmsBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 短信工具类
 * @date 2018/10/18
 * @email 252774645@qq.com
 */
public class SMSUtils {

    private static final String TAG =SMSUtils.class.getName();

    private static Uri SMS_INBOX = Uri.parse("content://sms/");

    public static List<SmsBean> getSmsInfo(Context context) {
        ContentResolver cr = context.getContentResolver();
        String[] projection = new String[] {"address","person","body","date","type","read","status"};
        Cursor cur = cr.query(SMS_INBOX, projection, null, null, "date desc");
        if (null == cur) {
            Log.e(TAG,"sms cursor is null");
            return null;
        }
        List<SmsBean> mlist = new ArrayList<>();
        while(cur.moveToNext()) {
            String number = cur.getString(cur.getColumnIndex("address"));//手机号
            String name = cur.getString(cur.getColumnIndex("person"));//联系人姓名列表
            String body = cur.getString(cur.getColumnIndex("body"));//短信内容
            String date = cur.getString(cur.getColumnIndex("date"));
            String type = cur.getString(cur.getColumnIndex("type"));
            String read = cur.getString(cur.getColumnIndex("read"));
            String status = cur.getString(cur.getColumnIndex("status"));
            SmsBean smsBean = new SmsBean();
            if(!TextUtils.isEmpty(number)&&number.contains("+")){
                smsBean.setAddress(number.substring(3));
            }else{
                smsBean.setAddress(number);
            }
            smsBean.setBody(body);
            smsBean.setDate(date);
            smsBean.setPerson(name);
            smsBean.setRead(read);
            smsBean.setStatus(status);
            smsBean.setType(type);
            smsBean.setBack(false);
            mlist.add(smsBean);
        }

        return  mlist;
    }


    public static SmsMessage[] getMessagesFromIntent(Intent intent) {

        Object[] messages = (Object[]) intent.getSerializableExtra("pdus");
        if (messages == null) {
            return null;
        }
        final int pduCount = messages.length;
        SmsMessage[] msgs = new SmsMessage[pduCount];
        try {
            for (int i = 0; i < pduCount; i++) {
                msgs[i] = SmsMessage.createFromPdu((byte[]) messages[i]);
            }
        } catch (Throwable e) {
            return null;
        }

        return msgs;
    }

    public static  List<SmsBean>  updateSMSSqlite(List<SmsBean> smsBeans, List<SmsBean> smsBeanSQ){
        if(smsBeans==null||smsBeans.size()==0){
            return null;
        }
        //则替换处理
        if(smsBeanSQ!=null&&smsBeanSQ.size()>0){
            for (int i=0;i<smsBeans.size();i++){
                SmsBean mSmsBean =smsBeans.get(i);
                Log.i(TAG,"mSmsBean="+mSmsBean.toString());
                if(smsBeanSQ!=null){
                    for (int j=0; j<smsBeanSQ.size();j++ ){
                        SmsBean mSmsBeanSQ =smsBeanSQ.get(j);
                        if(!TextUtils.isEmpty(mSmsBeanSQ.getAddress())&&!TextUtils.isEmpty(mSmsBean.getAddress())){
                            if(mSmsBean.getAddress().equals(mSmsBeanSQ.getAddress())){
                                mSmsBean.setBack(mSmsBeanSQ.isBack());
                                smsBeans.set(i,mSmsBean);
                            }
                        }
                    }
                }
            }
        }

        return smsBeans;
    }


}
