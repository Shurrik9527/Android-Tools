package com.hz.maiku.maikumodule.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe cpu温度
 * @date 2018/9/12
 * @email 252774645@qq.com
 */
public class CpuUtil {

    private static final String TAG =CpuUtil.class.getName();
    private final String cpuFreqPath = "/sys/devices/system/cpu/cpu0/cpufreq";

    private final static String PERFORMANCE_GOVERNOR = "performance";//cpu的工作频率调整到最大模式
    private final static String POWER_SAVE_GOVERNOR = "powersave";//cpu的工作频率调整到节能模式
    private final static String ONDEMAND_GOVERNOR = "ondemand";//cpu根据当前需求而变远大于范围 即自动模式
    private final static String CONSERVATIVE_GOVERNOR = "conservative";//cpu根据当前需求而变小范围边 即保守模式
    private final static String USERSAPCE_GOVERNOR = "usersapce";//cpu根据用户需求而变 即订制模式

    //通过读取文件/proc/cpuinfo，来获取系统CPU的类型等多种信息，
    //通过读取/proc/stat所有CPU活动的信息来计算CPU使用率。
   // 通过"/sys/devices/system/cpu/";读取cpu数量

    /**
     * 获取10个传感器至cpu的温度
     * @return
     */
    public static List<Long> getCpuThermalInfo() {
        List<Long> result = new ArrayList<>();
        BufferedReader br = null;
        String line = null;
        String type =null;
        try {
            File dir = new File("/sys/class/thermal/");
            File[] files = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    if (Pattern.matches("thermal_zone[0-9]+", file.getName())) {
                        return true;
                    }
                    return false;
                }
            });
            if(files==null||files.length==0){
                result.add(0L);
                return result;
            }
            final int SIZE = files.length;
            for (int i = 0; i < SIZE; i++) {

                br = new BufferedReader(new FileReader("/sys/class/thermal/thermal_zone" + i + "/type"));
                line = br.readLine();
                if (line != null) {
                    type = line;
                }

                br = new BufferedReader(new FileReader("/sys/class/thermal/thermal_zone" + i + "/temp"));
                line = br.readLine();

                //暂时以电池温度为准

                Log.i(TAG,"type="+type+"|temperature"+line);

//                if (line != null) {
//                    long temperature = Long.parseLong(line);
//                    if(!TextUtils.isEmpty(type)&&type.equals("battery")){
//                        Log.i(TAG,"battery="+temperature);
//                        result.add(temperature);
//                        break;
//                    }
//                }

                if (line != null) {
                    long temperature = Long.parseLong(line);
                    if(!TextUtils.isEmpty(type)&&type.equals("battery")){
                        Log.i(TAG,"type0"+type);
                        if(temperature>0&&temperature<1000){
                            if(temperature>0&&temperature<100){
                                result.add(temperature);
                            }else if(temperature>100&&temperature<1000){
                                temperature =temperature/10;
                                result.add(temperature);
                            }
                        }else{
                            temperature =temperature/1000;
                            result.add(temperature);
                        }
                        break;
                    }else{
                        Log.i(TAG,"type1"+type);
                        if(temperature>0&&temperature<1000){
                            if(temperature>0&&temperature<100){
                                result.add(temperature);
                            }else if(temperature>100&&temperature<1000){
                                temperature =temperature/10;
                                result.add(temperature);
                            }
                        }else{
                            temperature =temperature/1000;
                            result.add(temperature);
                        }
                    }
                }
            }

            br.close();
        } catch (FileNotFoundException e) {
            result.add(0L);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }


    public static float cpuAverageTemperature(){

        List<Long> temper = getCpuThermalInfo();
        if(temper==null||temper.size()==0){
            return 0L;
        }


//        if(temper.size()==1){
//            BigDecimal bigDecimal= new BigDecimal((float)temper.get(0)/1000);
//            return bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();//四舍五入 保留一位小数
//        }else {
//            return 0.0f;
//        }

        try {
            if(temper.size()==1){
                BigDecimal bigDecimal= new BigDecimal((float)temper.get(0));
                return bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();//四舍五入 保留一位小数
            }else {
                Collections.sort(temper);
                //去掉最低 和最高
                temper.remove(temper.size()-1);
                Long tempCpu =0L;
                    for (Long cpu : temper){
                        tempCpu =tempCpu+cpu;
                    }
                    BigDecimal bigDecimal= new BigDecimal((float)tempCpu/temper.size());
                    return bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();//四舍五入 保留一位小数
            }
        }catch (Exception e){
            return 0.0f;
        }

    }


    public static List<String> cputemp(){
        List<String> result = new ArrayList<>();
        BufferedReader br = null;

        try {
            File dir = new File("/sys/class/thermal/");

            File[] files = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    if (Pattern.matches("thermal_zone[0-9]+", file.getName())) {
                        return true;
                    }
                    return false;
                }
            });

            final int SIZE = files.length;
            String line = null;
            String type = null;
            String temp = null;
            for (int i = 0; i < SIZE; i++) {
                br = new BufferedReader(new FileReader("/sys/class/thermal/thermal_zone" + i + "/type"));
                line = br.readLine();
                if (line != null) {
                    type = line;
                }

                br = new BufferedReader(new FileReader("/sys/class/thermal/thermal_zone" + i + "/temp"));
                line = br.readLine();
                if (line != null) {
                    long temperature = Long.parseLong(line);
                    if (temperature < 0) {
                        temp = "Unknow";
                    } else {
                        temp = (float) (temperature / 1000.0) + "°C";
                    }

                }

                result.add(type + " : " + temp);
            }

            br.close();
        } catch (FileNotFoundException e) {
            result.add(e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }


    public static String cpuInform(Context context) {
        HashMap localHashMap = new HashMap();
        try {
            ProcessBuilder localBuilder = new ProcessBuilder(new String[]{"/system/bin/cat", "/proc/cpuinfo"});
            Process localProcess = localBuilder.start();
            InputStreamReader localObject2 = new InputStreamReader(localProcess.getInputStream());
            BufferedReader a = new BufferedReader(localObject2);
            for (; ; ) {
                String line = a.readLine();
                if (line == null) {
                    break;
                }
                if (!line.trim().equals("")) {
                    String[] sp = line.split(":");
                    if (sp.length > 1) {
                        String name = sp[0].trim().toLowerCase();
                        if ("model name".equals(name) || "features".equals(name) || " cpu architecture".equals(name) || "hardware".equals(name)
                                || "serial".equals(name)) {
                            localHashMap.put(name, sp[1].trim());
                        }
                    }
                }
            }
            return URLEncoder.encode(localHashMap.toString(),"UTF-8");
        } catch (IOException localIOException) {

        }
        return null;
    }

    public static String cpuState(Context context) {
        HashMap localHashMap = new HashMap();
        try {
            ProcessBuilder localBuilder = new ProcessBuilder(new String[]{"/system/bin/cat", "/proc/stat"});
            Process localProcess = localBuilder.start();
            InputStreamReader localObject2 = new InputStreamReader(localProcess.getInputStream());
            BufferedReader a = new BufferedReader(localObject2);
            for (; ; ) {
                String line = a.readLine();
                if (line == null) {
                    break;
                }
                Log.i(TAG,"line"+line);
                if (!line.trim().equals("")) {

                    String[] sp = line.split(":");
                    if (sp.length > 1) {
                        String name = sp[0].trim().toLowerCase();
                        if ("pid".equals(name) || "comm".equals(name)) {
                            localHashMap.put(name, sp[1].trim());
                        }
                    }
                }
            }
            return URLEncoder.encode(localHashMap.toString(),"UTF-8");
        } catch (IOException localIOException) {

        }
        return null;
    }



    private static long getTotalCpuTime() { // 获取系统总CPU使用时间
        String[] cpuInfos = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream("/proc/stat")), 1000);
            String load = reader.readLine();
            reader.close();
            cpuInfos = load.split(" ");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        long totalCpu = Long.parseLong(cpuInfos[2])
                + Long.parseLong(cpuInfos[3]) + Long.parseLong(cpuInfos[4])
                + Long.parseLong(cpuInfos[6]) + Long.parseLong(cpuInfos[5])
                + Long.parseLong(cpuInfos[7]) + Long.parseLong(cpuInfos[8]);
        return totalCpu;
    }

    private static long getAppCpuTime() { // 获取应用占用的CPU时间
        String[] cpuInfos = null;
        try {
            int pid = android.os.Process.myPid();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream("/proc/" + pid + "/stat")), 1000);
            String load = reader.readLine();
            reader.close();
            cpuInfos = load.split(" ");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        long appCpuTime = Long.parseLong(cpuInfos[13])
                + Long.parseLong(cpuInfos[14]) + Long.parseLong(cpuInfos[15])
                + Long.parseLong(cpuInfos[16]);
        return appCpuTime;
    }

    /**
     * 获取CPU使用率  获取系统CPU使用时间和应用CPU使用时间, 作比较就得出应用的CPU使用率
     * @return
     */
    public static float cpuUtilizationRatio(){
        BigDecimal bigDecimal= new BigDecimal((float)getTotalCpuTime()-getAppCpuTime()/getTotalCpuTime());
        return bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();//四舍五入 保留一位小数
    }


    /**
     * 获取app 占用cpu 使用的cpu
     * @param packageName
     * @return
     */
    public static int getAppCpuUsed(String packageName)
    {
        String[] cpuInfos = null;
        int AppCpuUsedPercent = -1;
        try
        {
            int pid = android.os.Process.myPid();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    Runtime.getRuntime().exec("top -n 1").getInputStream()), 500);
            String load = reader.readLine();
            while(load != null) {
                if(load.contains(packageName) && load.contains(String.valueOf(pid))) {
                    break;
                }
                load = reader.readLine();
            }
            reader.close();
            cpuInfos = load.split("%");
            AppCpuUsedPercent = Integer.parseInt(cpuInfos[0].substring(cpuInfos[0].length() - 3).trim());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return AppCpuUsedPercent;
    }


    /**
     * 当需要节能时 则可以把模式调到powersave 模式 但是需获取手机本身是否有这个模式 才可以设置这个模式
     */


    /**
     * 获得当前CPU调控模式
     */
    public void getCpuCurGovernor() {
        try {
            DataInputStream is = null;
            Process process = Runtime.getRuntime().exec("cat " + cpuFreqPath + "/scaling_governor");
            is = new DataInputStream(process.getInputStream());
            String line = is.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 重写CPU调控模式
     * @param governor
     * @return
     */
    private boolean writeCpuGovernor(String governor) {
        DataOutputStream os = null;
        byte[] buffer = new byte[256];
        String command = "echo " + governor + " > " + cpuFreqPath + "/scaling_governor";
        Log.i(TAG, "command: " + command);
        try {
            Process process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
            Log.i(TAG, "exit value = " + process.exitValue());
        } catch (IOException e) {
            Log.i(TAG, "writeCpuGovernor: write CPU Governor(" + governor + ") failed!");
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 获得CPU所有调控模式
     * @return
     */
    private List<String> readCpuGovernors() {
        List<String> governors = new ArrayList<String>();
        DataInputStream is = null;
        try {
            Process process = Runtime.getRuntime().exec("cat " + cpuFreqPath + "/scaling_available_governors");
            is = new DataInputStream(process.getInputStream());
            String line = is.readLine();

            String[] strs = line.split(" ");
            for (int i = 0; i < strs.length; i++)
                governors.add(strs[i]);
        } catch (IOException e) {
            Log.i(TAG, "readCpuGovernors: read CPU Governors failed!");
        }
        return governors;
    }

    /**
     * cpu最大频率
     * @return
     */
    public static String getMaxCpuFreq() {
        String result = "";
        ProcessBuilder cmd;
        try {
            String[] args = { "/system/bin/cat",
                    "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq" };
            cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[24];
            while (in.read(re) != -1) {
                result = result + new String(re);
            }
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            result = "N/A";
        }
        return result.trim();
    }

    /**
     * cpu 最小频率
     * @return
     */
    public static String getMinCpuFreq() {
        String result = "";
        ProcessBuilder cmd;
        try {
            String[] args = { "/system/bin/cat",
                    "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq" };
            cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[24];
            while (in.read(re) != -1) {
                result = result + new String(re);
            }
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            result = "N/A";
        }
        return result.trim();
    }


    /**
     * cpu 当前频率
     * @return
     */
    public static String getCurCpuFreq() {
        String result = "N/A";
        try {
            FileReader fr = new FileReader(
                    "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            result = text.trim();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
