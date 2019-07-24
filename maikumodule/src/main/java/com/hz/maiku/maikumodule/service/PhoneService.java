package com.hz.maiku.maikumodule.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.hz.maiku.maikumodule.event.RefreshRewordEvent;
import com.hz.maiku.maikumodule.manager.CallLogManager;
import com.hz.maiku.maikumodule.manager.HarassInterceptManager;
import com.hz.maiku.maikumodule.util.RxBus.RxBus;

import java.lang.reflect.Method;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 电话服务
 * @date 2018/10/19
 * @email 252774645@qq.com
 */
public class PhoneService extends Service{

    private static final String TAG =PhoneService.class.getName();
    private static final int flog =2;
    private static int previousMuteMode = -1;
    private TelephonyManager telephony;
    int booolsms=1;
    int boool=1;
    private boolean isTrue =false;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                NotificationChannel channel = new NotificationChannel("id","name", NotificationManager.IMPORTANCE_LOW);
                final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                manager.createNotificationChannel(channel);
                Notification notification = new Notification.Builder(getApplicationContext(),"id").build();
                startForeground(100, notification);
                //取消通知
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 延迟1s
                        SystemClock.sleep(2000);
                        stopForeground(true);
                        // 移除Service弹出的通知
                        manager.cancel(100);
                    }
                }).start();
            }
        }catch (Exception e) {

        }
        isTrue =false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent!=null){
            booolsms=intent.getIntExtra("bool", 2);
            boool=booolsms;
        }
        this.telephony=(TelephonyManager) super.getSystemService(Context.TELEPHONY_SERVICE);
        this.telephony.listen(new PhoneService.PhoneStateListenerlmpl(), PhoneStateListener.LISTEN_CALL_STATE);
        return super.onStartCommand(intent, flags, startId);
    }

    private class PhoneStateListenerlmpl extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            AudioManager mAudioManager=(AudioManager)PhoneService.this.getSystemService(Context.AUDIO_SERVICE);
            switch(state){
                case TelephonyManager.CALL_STATE_IDLE:
                    if(isTrue){
                        CallLogManager.getmInstance().updateCallLogSqliteData();
                        RxBus.getDefault().post(new RefreshRewordEvent());
                        isTrue =false;
                    }
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                {
                    isTrue =true;
                    //静音
                    if(judgement(incomingNumber)){//拦截指定的电话号码
                        mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                        try {
                            //挂断电话   方法一
                            Method method = Class.forName("android.os.ServiceManager").getMethod("getService", String.class);
                            // 获取远程TELEPHONY_SERVICE的IBinder对象的代理
                            IBinder binder = (IBinder) method.invoke(null, new Object[] { Context.TELEPHONY_SERVICE });
                            // 将IBinder对象的代理转换为ITelephony对象
                            ITelephony telephony = ITelephony.Stub.asInterface(binder);
                            // 挂断电话
                            telephony.endCall();
                            //挂断电话   方法二
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Log.e(TAG, "来电:"+incomingNumber);
                        mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
//	                     //再恢复正常铃声
//	                     data(incomingNumber);
//	 	 	            sendSMS(incomingNumber);
//	 	               	record(incomingNumber);  //****拦截记录***
//	 	               	notification(incomingNumber);//*****状态提示符****
                    }
                }
                break;
                case TelephonyManager.CALL_STATE_OFFHOOK:

                    break;

            }

        }

    }


    private boolean judgement(String number){
        HarassInterceptManager manager = new HarassInterceptManager(this);
        if(manager.isBlack(number)){
            return true;
        }else {
            return false;
        }
    }

//	public boolean rigion(String number){ //按地区拦截
//
//		if(number.length()>8){ //防止空号
//			SharedPreferences sp=getSharedPreferences("rule_record", MODE_PRIVATE);
//			String oldAddr=sp.getString("provincename", "请输入地区")+sp.getString("cityname", "");
//			int model=sp.getInt("spinner", 0);
//
//			String str=number.substring(0, 7);
//			String nowAddr=TelDao.query(str);
//
//			if(model==0){ //只拦截一下地区
//				 if(oldAddr.equals(nowAddr)){//是这个地区
//	  					return false; //不拦截
//	  				}else{  //不是这个地区
//	  					return true; //拦截
//	  				}
//
//			}else if(model==1){//只接听一下地区
//				 if(oldAddr.equals(nowAddr)){
//						return true;
//					}else{
//						return false;
//					}
//			}
//
//		}else{ return false;}
//
//		return false;
//	}
//
//	private void data(String number){    //******陌生电话拦截记录******
//		int ncount=0,max=0;
//		boolean bool=false;//判断陌生拦截是否有此号码
//		String phonenumber="";
//		SharedPreferences id=this.getSharedPreferences("rule_record",MODE_PRIVATE);
//		int ruleid=id.getInt("imageid", 0);
//		if(ruleid==2){ //是陌生模式时才启动
//        Calendar c=Calendar.getInstance();
//		int stime=c.get(Calendar.HOUR_OF_DAY);
//		int sminute=c.get(Calendar.MINUTE); //开始时间
//
//		mydbhelper=new MyDbHelper(PhoneService.this);
//		mydbhelper.open();
//		Cursor cur=mydbhelper.querData("stranger");
//		cur.moveToFirst();
//
//	    while(!cur.isAfterLast()){   //判断是否有此号码
//	    	if(number.equals(cur.getString(0))){
//	    		bool=true;
//	    		phonenumber=phonenumber.replace(phonenumber, cur.getString(0));
//	    		ncount=cur.getInt(2);
//	    		break;
//	    	}
//	    	cur.moveToNext();
//	    }
//
//	   if(boool==2){
//		    if(bool){ //当有此号码时
//		    	ncount=ncount+1;
//		    	mydbhelper.updataData(phonenumber, ncount);
//		    }else{ //当没有此号码时
//		    	cur.moveToFirst();
//		    	ncount=0;
//		    	while(!cur.isAfterLast()){   //判断陌生电话拦截次数是不大于规定值
//		    		ncount=cur.getInt(1);
//					if(ncount>=max){
//						max=ncount;
//					}
//					cur.moveToNext();
//				}
//
//				if(max>=10){  //大于最大值
//					reorder();//删除第一个,只保留第10个陌生记录信息
//					mydbhelper.insertData(number, 10, 1, stime,sminute, "", "stranger");
//				}else{
//					max=max+1;
//					mydbhelper.insertData(number, max, 1, stime, sminute,"", "stranger");
//				}
//
//		       }
//
//		    boool=1;
//		    mydbhelper.close();
//	     }
//		}
//
//	}
//	private void reorder(){ //删除第一个
//		int n;
//		String number="";
//		mydbhelper.deleteData(1);
//		Cursor cur=mydbhelper.querData("stranger");
//		cur.moveToFirst();
//		while(!cur.isAfterLast()){
//			number=number.replace(number, cur.getString(0));
//			n=cur.getInt(1);
//			if(n!=1){
//				n=n-1;
//				mydbhelper.updataData(n, number);
//			}
//			cur.moveToNext();
//		}
//		cur.close();
//	}
//
//
//    private boolean subtime(int shour,int sminute){ //第一次记录时间与现在时间相减
//    	int time=0;
//    	int n;
//		SharedPreferences sp=getSharedPreferences("rule_record", MODE_PRIVATE);
//		n=sp.getInt("phonebreak", 0);
//    	Calendar c=Calendar.getInstance();
//		int hour=c.get(Calendar.HOUR_OF_DAY);
//		int minute=c.get(Calendar.MINUTE);
//		if(sminute>minute){
//			time=(hour-shour-1)*60+(minute+60-sminute);
//		}else{
//			time=(hour-shour)*60+(minute-sminute);
//		}
//		if(time>=n){return false;}
//		else{ return true;}
//    }
//
//	private void sendSMS(String number){    //******陌生号码拦截发送短信*****
//		int count=0; //多少次之后发送短信
//		SharedPreferences id=this.getSharedPreferences("rule_record",MODE_PRIVATE);
//		int ruleid=id.getInt("imageid", 0);
//		String quest=id.getString("query", "你好！");
//		count=id.getInt("smscount", 0);
//
//		mydbhelper=new MyDbHelper(PhoneService.this);
//		mydbhelper.open();
//
//		if(ruleid==2&&booolsms==2){ //是陌生模式时才启动
//			Cursor cur=mydbhelper.querData("stranger");
//			cur.moveToFirst();
//		while(!cur.isAfterLast()){
//			if(cur.getInt(2)==count&&number.equals(cur.getString(0))){ //此号码需在陌生号码表中，和此表中拦截次数向比较
//				PendingIntent pi=PendingIntent.getActivity(this,
//						0, new Intent(this,PhoneService.class),0);
//				SmsManager sms=SmsManager.getDefault();
//				sms.sendTextMessage(number, null, quest, pi, null);
//				break;
//			}
//			cur.moveToNext();
//		}
//		cur.close();
//		booolsms=1;//防止多发
//
//		}else{} ;
//		mydbhelper.close();
//	}
//
//	private void record(String phonenumber){  //*******拦截记录****
//		boolean bool=false;   //*****判断号码是不拦截过*********
//		int frequency=0;
//
//		mydbhelper=new MyDbHelper(PhoneService.this);
//		mydbhelper.open();
//		Calendar c=Calendar.getInstance();
//		int year=c.get(Calendar.YEAR);
//		int month=c.get(Calendar.MONTH);
//		int day=c.get(Calendar.DAY_OF_WEEK);
//		int hour=c.get(Calendar.HOUR_OF_DAY);
//		int minute=c.get(Calendar.MINUTE);
//		String data=String.valueOf(year)+"/"+String.valueOf(month)+"/"+String.valueOf(day);
//		String time=String.valueOf(hour)+":"+String.valueOf(minute);
//		Cursor cur=mydbhelper.querData("record");
//		cur.moveToFirst();
//		while(!cur.isAfterLast()){    //*****判断是不以拦截过******
//			if(phonenumber.equals(cur.getString(0))){
//				frequency=cur.getInt(1);
//				frequency++;//统计拦截次数
//				bool=true;break;//是拦截过
//			}
//			cur.moveToNext();
//		}
//		if(bool){    //此号码已拦截过
//			mydbhelper.updataData(time,frequency, data, phonenumber, "record");
//		}else{
//		mydbhelper.insertData(phonenumber, 1, time, data, "record");
//		}
//		mydbhelper.close();
//	}
//
//	private void notification(String phone){
//		Intent intent=new Intent(PhoneService.this,Record.class);
//		PendingIntent pi=PendingIntent.getActivity(PhoneService.this, 0, intent, 0);
//
//		NotificationManager nm=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//		Notification notify=new Notification.Builder(PhoneService.this)
//		.setAutoCancel(true)
//		.setTicker("未接电话")
//		.setSmallIcon(R.drawable.p13)
//		.setContentText("一条新通知")
//		.setContentText("终结者拦截号码为:"+phone)
//		.setDefaults(Notification.DEFAULT_LIGHTS)
//		.setContentIntent(pi)
//		.build();
//		nm.notify(0x123, notify);
//	}



}
