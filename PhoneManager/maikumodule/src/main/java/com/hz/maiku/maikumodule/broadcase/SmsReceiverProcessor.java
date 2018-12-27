package com.hz.maiku.maikumodule.broadcase;

import android.content.Intent;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;

import com.hz.maiku.maikumodule.bean.SmsBean;
import com.hz.maiku.maikumodule.util.SMSUtils;


/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/10/22
 * @email 252774645@qq.com
 */
public class SmsReceiverProcessor {

    private static  final  String TAG =SmsReceiverProcessor.class.getName();

    public static final String ANDROID_PROVIDER_TELEPHONY_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    public static final String ANDROID_PROVIDER_TELEPHONY_SMS_RECEIVED2 = "android.provider.Telephony.SMS_RECEIVED2";
    public static final String ANDROID_PROVIDER_TELEPHONY_SMS_RECEIVED_2 = "android.provider.Telephony.SMS_RECEIVED_2";
    public static final String ANDROID_PROVIDER_TELEPHONY_GSM_SMS_RECEIVED = "android.provider.Telephony.GSM_SMS_RECEIVED";

    public SmsReceiverProcessor() {

    }

    public SmsBean handleSms(Intent intent) {
        SmsMessage[] smss = SMSUtils.getMessagesFromIntent(intent);
        if (smss != null && smss.length >= 1) {
            StringBuilder bodyBuf = new StringBuilder();
            String phoneNumber = "";    // 电话号码
            long time = 0;
            for (SmsMessage msg : smss) {
                try {
                    bodyBuf.append(msg.getDisplayMessageBody());
                    phoneNumber = msg.getDisplayOriginatingAddress();
                    time = msg.getTimestampMillis();
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
            }
            final String smsContent = bodyBuf.toString();    // 短信内容
            // 获得短信号码和内容之后可以进行相关处理
            SmsBean bean = new SmsBean();
            try {
                if(phoneNumber.contains("+")){
                    bean.setAddress(phoneNumber.substring(3));
                }else{
                    bean.setAddress(phoneNumber);
                }
                bean.setBack(false);
                bean.setType("1");
                bean.setStatus("-1");
                bean.setRead("1");
                bean.setDate(String.valueOf(time));
                bean.setBody(smsContent);
                Log.i(TAG,"接受的短信"+bean.toString());
            }catch (Exception e){

            }
            if (TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(smsContent)) {
                return null;
            }else{
                return bean;
            }
        }else{
            return null;
        }
    }



}
