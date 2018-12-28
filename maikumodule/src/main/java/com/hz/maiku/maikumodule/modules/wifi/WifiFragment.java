package com.hz.maiku.maikumodule.modules.wifi;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.model.GuidePage;
import com.app.hubert.guide.model.HighLight;
import com.appsflyer.AFInAppEventType;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.R2;
import com.hz.maiku.maikumodule.base.Constant;
import com.hz.maiku.maikumodule.bean.WifiBean;
import com.hz.maiku.maikumodule.modules.trafficstatis.TrafficStatisPresenter;
import com.hz.maiku.maikumodule.util.EventUtil;
import com.hz.maiku.maikumodule.util.SpHelper;
import com.hz.maiku.maikumodule.util.StringUtil;
import com.hz.maiku.maikumodule.util.ToastUtil;
import com.hz.maiku.maikumodule.util.WifiHelper;
import com.hz.maiku.maikumodule.widget.dialog.AlertSingleDialog;
import com.hz.maiku.maikumodule.widget.dialog.InputDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/10/30
 * @email 252774645@qq.com
 */
public class WifiFragment extends Fragment implements WifiContract.View {

    private static final String TAG = WifiFragment.class.getName();
    @BindView(R2.id.wifimanager_rv)
    RecyclerView wifimanagerRv;
    @BindView(R2.id.wifi_connected_tv)
    TextView wifiConnectedTv;
    @BindView(R2.id.wifi_rl)
    RelativeLayout wifiRl;
    @BindView(R2.id.wifi_connected_cb)
    Switch wifiConnectedCb;


    private WifiAdapter mWifiAdapter;
    private WifiContract.Presenter mPresenter;
    private static final int REQUEST_CODE = 0X04;
    private boolean mHasPermission;
    private List<WifiBean> realWifiList;
    private WifiBroadcastReceiver wifiReceiver;

    //两个危险权限需要动态申请
    private static final String[] NEEDED_PERMISSIONS = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    private int connectType = 0;//1：连接成功？2 正在连接（如果wifi热点列表发生变需要该字段）
    private InputDialog mInputDialog;
    public WifiFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static WifiFragment newInstance() {
        WifiFragment fragment = new WifiFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new WifiPresenter(this,getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.wifi_fragment, container, false);
        ButterKnife.bind(this, root);
        initView();
        initData();
        return root;
    }

    @Override
    public void setPresenter(WifiContract.Presenter presenter) {
        this.mPresenter =presenter;
    }

    @Override
    public void initView() {

        // RecycleView 布局类型设置
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        wifimanagerRv.setLayoutManager(layoutManager);
        wifimanagerRv.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                EventUtil.sendEvent(getActivity(), AFInAppEventType.START_TRIAL, "wifi");
                WifiBean wifiBean = realWifiList.get(position);
                if (wifiBean.getState().equals(WifiHelper.WIFI_STATE_UNCONNECT) || wifiBean.getState().equals(WifiHelper.WIFI_STATE_CONNECT)) {
                    String capabilities = realWifiList.get(position).getCapabilities();
                    if (WifiHelper.getWifiCipher(capabilities) == WifiHelper.WifiCipherType.WIFICIPHER_NOPASS) {//无需密码
                        WifiConfiguration tempConfig = WifiHelper.isExsits(wifiBean.getWifiName(), getContext());
                        if (tempConfig == null) {
                            WifiConfiguration exsits = WifiHelper.createWifiConfig(wifiBean.getWifiName(), null, WifiHelper.WifiCipherType.WIFICIPHER_NOPASS);
                            WifiHelper.addNetWork(exsits, getContext());
                        } else {
                            WifiHelper.addNetWork(tempConfig, getContext());
                        }
                    } else {   //需要密码，弹出输入密码dialog
                        configurationWifi(wifiBean);
                    }
                }
            }
        });

        if(WifiHelper.isOpenWifi(getContext())){
            SpHelper.getInstance().put(Constant.WIFI_OPEN,true);
            wifiConnectedCb.setChecked(true);
        }else{
            SpHelper.getInstance().put(Constant.WIFI_OPEN,false);
            wifiConnectedCb.setChecked(false);
        }

        wifiConnectedCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SpHelper.getInstance().put(Constant.WIFI_OPEN,true);
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                        showPermission();
                    } else {
                        //扫描
                        if (WifiHelper.isOpenWifi(getContext())) {
                            if (mPresenter != null) {
                                mPresenter.scanAllWifis();
                            }
                        }
                    }
                    WifiHelper.openWifi(getContext());
                    if(wifimanagerRv!=null){
                        wifimanagerRv.setVisibility(View.VISIBLE);
                    }
                }else{

                    SpHelper.getInstance().put(Constant.WIFI_OPEN,false);
                    List<WifiBean> emptyLists = new ArrayList<>();
                    if(mWifiAdapter!=null){
                        mWifiAdapter.setNewData(emptyLists);
                    }
                    if(wifiRl!=null){
                        wifiRl.setVisibility(View.GONE);
                    }

                    if(wifimanagerRv!=null){
                        wifimanagerRv.setVisibility(View.GONE);
                    }

                    WifiHelper.closeWifi(getContext());
                }
            }
        });
    }

    @Override
    public void showMessageTips(String msg) {
        ToastUtil.showToast(getContext(),msg);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void showAllWifi(List<WifiBean> lists) {
        if (lists != null && mWifiAdapter != null) {
            realWifiList = lists;
            WifiInfo connectedWifiInfo = WifiHelper.getConnectedWifiInfo(getContext());
            if (connectedWifiInfo != null) {
                WifiBean wifiConnected = null;
                for (WifiBean bean : realWifiList) {
                    if (("\"" + bean.getWifiName() + "\"").equals(connectedWifiInfo.getSSID())) {
                        wifiConnected = bean;
                        wifiConnected.setState(WifiHelper.WIFI_STATE_CONNECT);
                        realWifiList.remove(bean);
                        break;
                    }
                }

                if (wifiConnected != null) {
                    wifiRl.setVisibility(View.VISIBLE);
                    wifiConnectedTv.setText(wifiConnected.getWifiName());
                    SpHelper.getInstance().put(Constant.WIFI_OPEN,true);
                    wifiConnectedCb.setChecked(true);
                    showMessageTips("Congratulate "+wifiConnected.getWifiName()+" "+WifiHelper.WIFI_STATE_CONNECT);
                }

            } else {
                wifiRl.setVisibility(View.GONE);
            }
            Collections.sort(realWifiList);
            mWifiAdapter.setNewData(realWifiList);
        }
    }

    @Override
    public void showPermission() {

        mHasPermission = checkPermission();
        if (!mHasPermission && WifiHelper.isOpenWifi(getContext())) {  //未获取权限，申请权限
            requestPermission();
        } else if (mHasPermission && WifiHelper.isOpenWifi(getContext())) {  //已经获取权限
            //扫描
            if (mPresenter != null) {
                mPresenter.scanAllWifis();
            }
        }
    }


    @Override
    public void initData() {
        mWifiAdapter = new WifiAdapter();
        wifimanagerRv.setAdapter(mWifiAdapter);

        boolean wifiopen= (boolean) SpHelper.getInstance().get(Constant.WIFI_OPEN,false);
        if(wifiopen){
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                showPermission();
            } else {
                //扫描
                if (WifiHelper.isOpenWifi(getContext())) {
                    if (mPresenter != null) {
                        mPresenter.scanAllWifis();
                    }
                }
            }
        }else {
            if (wifiRl != null) {
                wifiRl.setVisibility(View.GONE);
            }

        }

    }


    @Override
    public boolean checkPermission() {
        for (String permission : NEEDED_PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), NEEDED_PERMISSIONS, REQUEST_CODE);
    }

    @Override
    public void initReceiver() {

        wifiReceiver = new WifiBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);//监听wifi是开关变化的状态
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);//监听wifi连接状态广播,是否连接了一个有效路由
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);//监听wifi列表变化（开启一个热点或者关闭一个热点）
        getContext().registerReceiver(wifiReceiver, filter);

    }

    @Override
    public void wifiChange() {
        boolean wifiopen= (boolean) SpHelper.getInstance().get(Constant.WIFI_OPEN,false);
        if(wifiopen){
            if (mPresenter != null) {
                mPresenter.scanAllWifis();
            }
        }
    }

    /**
     * 将"已连接"或者"正在连接"的wifi热点放置在第一个位置
     *
     * @param wifiName
     * @param type
     */
    @Override
    public void wifiListSet(String wifiName, int type) {
        int index = -1;
        WifiBean wifiInfo = new WifiBean();
        if (StringUtil.isNullOrEmpty(realWifiList)) {
            return;
        }
        for (int i = 0; i < realWifiList.size(); i++) {
            realWifiList.get(i).setState(WifiHelper.WIFI_STATE_UNCONNECT);
        }
        Collections.sort(realWifiList);//根据信号强度排序
        for (int i = 0; i < realWifiList.size(); i++) {
            WifiBean wifiBean = realWifiList.get(i);
            if (("\"" + wifiBean.getWifiName() + "\"").equals(wifiName)) {
                index = i;
                wifiInfo.setLevel(wifiBean.getLevel());
                wifiInfo.setWifiName(wifiBean.getWifiName());
                wifiInfo.setCapabilities(wifiBean.getCapabilities());
                if (type == 1) {
                    wifiInfo.setState(WifiHelper.WIFI_STATE_CONNECT);
                } else {
                    wifiInfo.setState(WifiHelper.WIFI_STATE_ON_CONNECTING);
                }
            }
        }

        if (index != -1) {
            wifiRl.setVisibility(View.VISIBLE);
            if(wifiName.contains("\"")){
                wifiConnectedTv.setText(wifiName.substring(1,wifiName.length()-1));
            }else{
                wifiConnectedTv.setText(wifiName);
            }

            if (mWifiAdapter != null) {
                mWifiAdapter.setNewData(realWifiList);
            }
        } else {
            wifiRl.setVisibility(View.GONE);
        }
    }

    @Override
    public void configurationWifi(final WifiBean wifiBean) {
        if(wifiBean!=null){
            mInputDialog = new InputDialog(getContext(), wifiBean.getWifiName(), null, new InputDialog.ConfirmListener() {
                @Override
                public void callback(String content) {
                    if(!TextUtils.isEmpty(content)){
                        String capabilities =wifiBean.getCapabilities();
                        WifiConfiguration tempConfig  =WifiHelper.isExsits(wifiBean.getWifiName(),getContext());
                        boolean isAdd =false;
                        if(tempConfig == null){
                            WifiConfiguration wifiConfiguration =  WifiHelper.createWifiConfig(wifiBean.getWifiName(),content,WifiHelper.getWifiCipher(capabilities));
                            isAdd= WifiHelper.addNetWork(wifiConfiguration,getContext());
                        }else{
                            isAdd=WifiHelper.addNetWork(tempConfig,getContext());
                        }
                        if(isAdd){
                            showMessageTips("Add "+wifiBean.getWifiName()+" success,Please wait a few seconds,If no connect ,Please try again");
                            mInputDialog.dismiss();
                        }
                    }
                }
            });
            mInputDialog.show();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        NewbieGuide.with(this).setLabel("wifiConnectedCb").addGuidePage(GuidePage.newInstance().addHighLight(wifiConnectedCb,HighLight.Shape.CIRCLE).setLayoutRes(R.layout.guide_wifi_open_layout)).setShowCounts(1).show();
        initReceiver();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasAllPermission = true;
        if (requestCode == REQUEST_CODE) {
            for (int i : grantResults) {
                if (i != PackageManager.PERMISSION_GRANTED) {
                    hasAllPermission = false; //判断用户是否同意获取权限
                    break;
                }
            } //如果同意权限

            if (hasAllPermission&&getActivity()!=null) {
                mHasPermission = true;
                NewbieGuide.with(this).setLabel("wifiConnectedCb").addGuidePage(GuidePage.newInstance().addHighLight(wifiConnectedCb,HighLight.Shape.CIRCLE).setLayoutRes(R.layout.guide_notification_open_layout)).setShowCounts(1).show();
                if (WifiHelper.isOpenWifi(getActivity()) && mHasPermission) { //如果wifi开关是开 并且 已经获取权限
                    if (mPresenter != null) {
                        mPresenter.scanAllWifis();
                    }
                }
            } else { //用户不同意权限
                mHasPermission = false;
                AlertSingleDialog dialog = new AlertSingleDialog(getContext(), "NOTICTION", "Sorry! Permissions is not get,This modle can't use,Please try again。", "Sure", new AlertSingleDialog.ConfirmListener() {
                    @Override
                    public void callback() {
                        if(getActivity()!=null){
                            getActivity().finish();
                        }
                    }
                });
                dialog.show();
            }
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(wifiReceiver);
        if(mInputDialog!=null&&mInputDialog.isShowing()){
            mInputDialog.dismiss();
            mInputDialog =null;
        }

    }

    class WifiBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {//wifi 连接上
                int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                switch (state) {
                    /**
                     * WIFI_STATE_DISABLED    WLAN已经关闭
                     * WIFI_STATE_DISABLING   WLAN正在关闭
                     * WIFI_STATE_ENABLED     WLAN已经打开
                     * WIFI_STATE_ENABLING    WLAN正在打开
                     * WIFI_STATE_UNKNOWN     未知
                     */
                    case WifiManager.WIFI_STATE_DISABLED: {
                        Log.d(TAG, "已经关闭");
                        break;
                    }
                    case WifiManager.WIFI_STATE_DISABLING: {
                        Log.d(TAG, "正在关闭");
                        break;
                    }
                    case WifiManager.WIFI_STATE_ENABLED: {
                        Log.d(TAG, "已经打开");
                        if (mPresenter != null) {
                            mPresenter.scanAllWifis();
                        }
                        break;
                    }
                    case WifiManager.WIFI_STATE_ENABLING: {
                        Log.d(TAG, "正在打开");
                        break;
                    }
                    case WifiManager.WIFI_STATE_UNKNOWN: {
                        Log.d(TAG, "未知状态");
                        break;
                    }
                }
            } else if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {//移动网络连接上
                NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (NetworkInfo.State.DISCONNECTED == info.getState()) {//wifi没连接上
                    Log.d(TAG, "wifi没连接上");
                    for (int i = 0; i < realWifiList.size(); i++) {//没连接上将 所有的连接状态都置为“未连接”
                        realWifiList.get(i).setState(WifiHelper.WIFI_STATE_UNCONNECT);
                    }
                    if (mWifiAdapter != null && realWifiList != null) {
                        mWifiAdapter.setNewData(realWifiList);
                    }
                } else if (NetworkInfo.State.CONNECTED == info.getState()) {//wifi连接上了
                    Log.d(TAG, "wifi连接上了");
                    wifiChange();
                } else if (NetworkInfo.State.CONNECTING == info.getState()) {//正在连接
//                    Log.d(TAG, "wifi正在连接");
//                    WifiInfo connectedWifiInfo = WifiHelper.getConnectedWifiInfo(getContext());
//                    connectType = 2;
//                    wifiListSet(connectedWifiInfo.getSSID(), connectType);
 //                   showMessageTips("Connecting...");
                }
            } else if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(intent.getAction())) {
                Log.d(TAG, "网络列表变化了");
                wifiChange();
            }
        }
    }


}
