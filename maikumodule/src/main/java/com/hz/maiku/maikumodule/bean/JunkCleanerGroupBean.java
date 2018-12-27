package com.hz.maiku.maikumodule.bean;

import java.util.ArrayList;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 垃圾清理 文件群
 * @date 2018/9/6
 * @email 252774645@qq.com
 */
public class JunkCleanerGroupBean {

    private long totalSize;
    private ArrayList<JunkCleanerProcessInformBean> mApkList;
    private ArrayList<JunkCleanerProcessInformBean> mProcessList;
    private ArrayList<JunkCleanerProcessInformBean> mSysCacheList;
    private ArrayList<JunkCleanerProcessInformBean> mTempList;
    private ArrayList<JunkCleanerProcessInformBean> mLogList;


    public JunkCleanerGroupBean() {

    }

    public JunkCleanerGroupBean(ArrayList<JunkCleanerProcessInformBean> mApkList, ArrayList<JunkCleanerProcessInformBean> mProcessList, ArrayList<JunkCleanerProcessInformBean> mSysCacheList, ArrayList<JunkCleanerProcessInformBean> mTempList, ArrayList<JunkCleanerProcessInformBean> mLogList) {
        this.mApkList = mApkList;
        this.mProcessList = mProcessList;
        this.mSysCacheList = mSysCacheList;
        this.mTempList = mTempList;
        this.mLogList = mLogList;
    }

    public ArrayList<JunkCleanerProcessInformBean> getApkList() {
        return mApkList;
    }

    public JunkCleanerGroupBean setApkList(ArrayList<JunkCleanerProcessInformBean> mApkList) {
        this.mApkList = mApkList;
        return this;
    }

    public ArrayList<JunkCleanerProcessInformBean> getProcessList() {
        return mProcessList;
    }

    public JunkCleanerGroupBean setProcessList(ArrayList<JunkCleanerProcessInformBean> mProcessList) {
        this.mProcessList = mProcessList;
        return this;
    }

    public ArrayList<JunkCleanerProcessInformBean> getSysCacheList() {
        return mSysCacheList;
    }

    public JunkCleanerGroupBean setSysCacheList(ArrayList<JunkCleanerProcessInformBean> mSysCacheList) {
        this.mSysCacheList = mSysCacheList;
        return this;
    }

    public ArrayList<JunkCleanerProcessInformBean> getTempList() {
        return mTempList;
    }

    public JunkCleanerGroupBean setTempList(ArrayList<JunkCleanerProcessInformBean> mTempList) {
        this.mTempList = mTempList;
        return this;
    }

    public ArrayList<JunkCleanerProcessInformBean> getLogList() {
        return mLogList;
    }

    public JunkCleanerGroupBean setLogList(ArrayList<JunkCleanerProcessInformBean> mLogList) {
        this.mLogList = mLogList;
        return this;
    }


    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public ArrayList<JunkCleanerProcessInformBean> getJunkList(int index) {
        switch (index) {
            case JunkCleanerTypeBean.PROCESS:
                return mProcessList;
            case JunkCleanerTypeBean.CACHE:
                return mSysCacheList;
            case JunkCleanerTypeBean.APK:
                return mApkList;
            case JunkCleanerTypeBean.LOG:
                return mLogList;
            case JunkCleanerTypeBean.TEMP:
                return mTempList;
        }
        return null;
    }
    
    
}
