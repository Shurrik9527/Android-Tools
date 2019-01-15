package com.hz.maiku.maikumodule.util;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 时间工具
 * @date 2018/9/18
 * @email 252774645@qq.com
 */
public class TimeUtil {
    public static final SimpleDateFormat DEFAULT_YM_DATE_FORMAT = new SimpleDateFormat("yyyy-MM");
    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("MM-dd HH:mm");
    public static final SimpleDateFormat DEFAULT_DATE_YMD_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat("HH:mm");
    public static final SimpleDateFormat DATE_FEN_MAO_FORMAT =new SimpleDateFormat("mm:ss");
    public static final SimpleDateFormat DEFAULT_DATE_MDHM_FORMAT = new SimpleDateFormat("MM-dd HH:mm:ss");
    public static final SimpleDateFormat DEFAULT_YM_DATE_CHINA_FORMAT = new SimpleDateFormat("yyyy年MM月");
    private static SimpleDateFormat formatter = null;

    /**
     * 获取当前时间
     * @return
     */
    public static String currentTimeStr(){
        formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return formatter.format(new Date(System.currentTimeMillis()));
    }


    /**
     * 获取当前时间
     * @return
     */
    public static Date currentTimeData(){
        return new Date(System.currentTimeMillis());
    }


    /**
     * 是否超过规定时间
     * @param beginTime 开始时间
     * @param endTime
     * @param num
     * @return
     */
    public static  boolean isTrue(String beginTime,String endTime,long num){
        if(TextUtils.isEmpty(beginTime)||TextUtils.isEmpty(endTime)){
            return false;
        }
        formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try {
            Date mBeginTime=formatter.parse(beginTime);
            Date mEndTime=formatter.parse(endTime);

            if((mEndTime.getTime()-mBeginTime.getTime())>num){//超过num时间
                return true;
            }else{
                return false;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * long time to string
     *
     * @param timeInMillis
     * @param dateFormat
     * @return
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }


    public static Date getYMDate(String content){
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
            return df.parse(content);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static Date getYMDayDate(String content){
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
            return df.parse(content);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public static String getYMStr(String content){
        try {
            return getYMStr(DEFAULT_DATE_YMD_FORMAT.parse(content));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String getMDHMStr(String content){
        try {
            return getMDHMStr(DEFAULT_DATE_FORMAT.parse(content));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public static String getYMStr(Date month){
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月");
            return df.format(month);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String getYMDStr(Date month){
        try {
            return DEFAULT_DATE_YMD_FORMAT.format(month);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String getMDHMStr(Date month){
        try {
            return DEFAULT_DATE_MDHM_FORMAT.format(month);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }



    public static String changeDayStr(Date month){
        try {
            return DEFAULT_YM_DATE_FORMAT.format(month);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @param timeInMillis
     * @return
     */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
    }


    /**
     * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @param timeInMillis
     * @return
     */
    public static String getYMDtime(long timeInMillis) {
        if(timeInMillis==0){
            return "";
        }
        return getTime(timeInMillis, DEFAULT_DATE_YMD_FORMAT);
    }


    /**
     * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @param timeInMillis
     * @return
     */
    public static String getYMtime(long timeInMillis) {
        if(timeInMillis==0){
            return "";
        }
        return getTime(timeInMillis, DEFAULT_YM_DATE_FORMAT);
    }

    /**
     * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @param timeInMillis
     * @return
     */
    public static String getYMtimeStr(long timeInMillis) {
        if(timeInMillis==0){
            return "";
        }
        return getTime(timeInMillis,DEFAULT_YM_DATE_CHINA_FORMAT);
    }


    /**
     * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @param timeInMillis
     * @return
     */
    public static String getHMMtime(long timeInMillis) {
        if(timeInMillis==0){
            return "";
        }
        return getTime(timeInMillis, DATE_FORMAT_DATE);
    }


    /**
     * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @param timeInMillis
     * @return
     */
    public static String getMDHMtime(long timeInMillis) {
        if(timeInMillis==0){
            return "";
        }
        return getTime(timeInMillis, DEFAULT_DATE_MDHM_FORMAT);
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @return
     */
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }


    /**
     * 日期逻辑
     *
     * @param date 时间
     * @return
     */
    public static String timeLogic(Date date) {

        String str = DEFAULT_DATE_FORMAT.format(date);
        String hourTime = DATE_FORMAT_DATE.format(date);
        Calendar calendar = Calendar.getInstance();
        calendar.get(Calendar.DAY_OF_MONTH);
        long now = calendar.getTimeInMillis();
        calendar.setTime(date);
        long past = calendar.getTimeInMillis();

        // 相差的秒数
        long time = (now - past) / 1000;

        StringBuffer sb = new StringBuffer();
//        if (time > 0 && time < 60) { // 1分钟内
//            return sb.append(time + " seconds ago").toString();
//        } else if (time > 60 && time < 3600) {//1小时内
//            return sb.append(time / 60 + " minutes ago").toString();
//        } else if (time >= 3600 && time < 3600 * 24) {
//            return hourTime;
//        } else if (time >= 3600 * 24 && time < 3600 * 48) {
//            return sb.append("yesterday " + hourTime).toString();
//        } else {
//            return str;
//        }

        if (time > 0 && time < 60) { // 1分钟内
            return sb.append(time + "s ago").toString();
        } else if (time > 60 && time < 3600) {//1小时内
            return sb.append(time / 60 + "m ago").toString();
        } else if (time >= 3600 && time < 3600 * 24) {
            return hourTime;
        } else if (time >= 3600 * 24 && time < 3600 * 48) {
            return str;
        } else {
            return str;
        }

    }


    /**
     * 日期逻辑
     *
     * @param date 时间
     * @return
     */
    public static String timeDefault(Date date) {
        String str = DEFAULT_DATE_FORMAT.format(date);
        String hourTime = DATE_FORMAT_DATE.format(date);
        Calendar calendar = Calendar.getInstance();
        calendar.get(Calendar.DAY_OF_MONTH);
        long now = calendar.getTimeInMillis();
        calendar.setTime(date);
        long past = calendar.getTimeInMillis();

        // 相差的秒数
        long between_days=(now-past)/(1000*3600*24);
        if(between_days>0){
            return  DEFAULT_DATE_FORMAT.format(date);
        }else{
            return  hourTime;
        }
    }





    /**
     * 计算两个日期之间相差的天数
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate,Date bdate) throws ParseException
    {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        smdate=sdf.parse(sdf.format(smdate));
        bdate=sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 计算两个日期之间相差的天数
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(long startdate,long enddate) throws ParseException
    {
        if(startdate==0||enddate==0){
            return 0;
        }
        try {
            long between_days=(enddate-startdate)/(1000*3600*24);
            return Integer.parseInt(String.valueOf(between_days));
        }catch (Exception e){

        }
        return 0;
    }


    public static String changeTime(long timeInMillis){
        return getTime(timeInMillis,DATE_FEN_MAO_FORMAT);
    }


    /**
     * date2比date1多的天数
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDays(Date date1,Date date2)
    {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1= cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if(year1 != year2)   //同一年
        {
            int timeDistance = 0 ;
            for(int i = year1 ; i < year2 ; i ++)
            {
                if(i%4==0 && i%100!=0 || i%400==0)    //闰年
                {
                    timeDistance += 366;
                }
                else    //不是闰年
                {
                    timeDistance += 365;
                }
            }
            int day =timeDistance + (day2-day1);
            if(day<0){
                return 0;
            }else{
                return timeDistance + (day2-day1) ;
            }
        }
        else    //不同年
        {
            //System.out.println("判断day2 - day1 : " + (day2-day1));
            int day =day2-day1;
            if(day<0){
                day =0;
            }
            return day;
        }
    }


    public static long getTimesMonthMorning() {
        Calendar cal = Calendar.getInstance(); cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTimeInMillis();
    }


    public static long getTimesmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (cal.getTimeInMillis());
    }

    /**
     * 得到当前年份的零点时间
     * @return
     */
    public static long[] getTimesYearMorning() {

        long[] times =new long[13];
        for (int i=0;i<times.length;i++){
            times[i]=getTimesbeforeMonthMorning(i);
        }

        return times;
    }


    /**
     * 得到月份的零点时间
     * @return
     */
    public static long getTimesbeforeMonthMorning(int month) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, -month);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTimeInMillis();
    }

    /**
     * 获取当前时间
     * @return
     */
    public static String currentMonthStr(){
        formatter = new SimpleDateFormat("MM");
        return formatter.format(new Date(System.currentTimeMillis()));
    }


    public static String videoTime(long time){
        try {

            if(time==0){
                return "0s";
            }else if(time>1000&&time<60000){
                return String.format("%.2f",(float)time/1000)+"s";
            }else if(time>60000&&time<3600000){
                String times =String.format("%.2f",(float)time/60000);
                return times+"m";
            }else if(time>3600000){
                String times =String.format("%.2f",(float)time/3600000);
                return times+"h";
            }else {
                return "0s";
            }

//            if(time==0){
//                return "0:00";
//            }else if(time>1000&&time<60000){
//                return String.format("%.2f",(float)time/1000)+"s";
//            }else if(time>60000&&time<3600000){
//                String times =String.format("%.2f",(float)time/60000);
//                if(times.contains(",")){
//                    times =times.replace(",",".");
//                }
//                String[] timeStr =times.split(".");
//                String time2 =String.valueOf(Long.parseLong(timeStr[1])*60);
//                return timeStr[0]+":"+time2;
//            }else if(time>3600000){
//                String times =String.format("%.0f",(float)time/3600000);
//                if(times.contains(",")){
//                    times =times.replace(",",".");
//                }
//                String[] timeStr =times.split(".");
//                String time2 =String.valueOf(Long.parseLong(timeStr[1])*3600);
//                return timeStr[0]+":"+time2+":00";
//            }else {
//                return "0:00";
//            }
        }catch (Exception e){
            return "0:00";
        }
    }



}
