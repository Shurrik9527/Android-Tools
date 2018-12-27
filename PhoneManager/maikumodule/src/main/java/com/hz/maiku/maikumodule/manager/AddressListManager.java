package com.hz.maiku.maikumodule.manager;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.hz.maiku.maikumodule.bean.AddressListBean;
import com.hz.maiku.maikumodule.util.PhoneUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 通讯录管理
 * @date 2018/10/22
 * @email 252774645@qq.com
 */
public class AddressListManager {


    private static final  String TAG  =AddressListManager.class.getName();
    public static AddressListManager mInstance;
    private Context mContext;
    private  HarassInterceptManager manager;
    //私有构造方法
    private AddressListManager(Context context){
        this.mContext =context;
        this.manager = new HarassInterceptManager(context);
    }

    /**
     * 获取当前内存管理
     * @return
     */
    public static AddressListManager getmInstance(){
        if(mInstance==null){
            throw  new IllegalStateException("AddressListManager is not init...");
        }
        return mInstance;
    }

    /**
     * 初始化内存管理器
     * @param context
     */
    public static void init(Context context) {
        if (mInstance == null) {
            synchronized (AddressListManager.class) {
                if (mInstance == null) {
                    mInstance = new AddressListManager(context);
                }
            }
        }
    }


    /**
     * 异步订阅手机本身通讯录
     * @return
     */
    public Observable<List<AddressListBean>> getMobileAddressListObservable() {
        try {
            Observable<List<AddressListBean>> observable =Observable.create(new ObservableOnSubscribe<List<AddressListBean>>() {
                @Override
                public void subscribe(ObservableEmitter<List<AddressListBean>> e) throws Exception {
                    List<AddressListBean> mlist= PhoneUtils.getAllAddressListNum(mContext);
                    if(mlist!=null&&mlist.size()>0){
                        e.onNext(mlist);
                    }
                }
            }).subscribeOn(Schedulers.io());
            return observable;
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 获取手机数据库的通讯录
     * @return
     */
    public Observable<List<AddressListBean>> getSqliteAddressListObservable() {
        return Observable.create(new ObservableOnSubscribe<List<AddressListBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<AddressListBean>> e) throws Exception {
                e.onNext(manager.getAllAddressListInfos());
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io());
    }


    public void updateAddressListSqliteData(){
        try {

            Observable<List<AddressListBean>> observableaddress =getMobileAddressListObservable();
            if(observableaddress==null){
                return;
            }

            Observable.zip(observableaddress,getSqliteAddressListObservable(), new BiFunction<List<AddressListBean>, List<AddressListBean>, List<AddressListBean>>() {
                @Override
                public List<AddressListBean> apply(List<AddressListBean> addressListBeans, List<AddressListBean> addressListBeans2) throws Exception {
                    return PhoneUtils.updateAddressListSqlite(addressListBeans,addressListBeans2);
                }
            }).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<AddressListBean>>() {
                @Override
                public void accept(List<AddressListBean> addressListBeans) throws Exception {
                    if(addressListBeans!=null&&addressListBeans.size()>0){
                        //先清理数据库
                        try {
                            if(manager!=null){
                                manager.deleteAllAddressListInfo();
                                manager.instanceAddressListInfoTable(addressListBeans);
                            }
                            Log.e(TAG,"更新通讯录成功...");
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                    }else{
                        Log.e(TAG,"更新通讯录失败或者手机本地通讯录没有数据");
                    }
                }
            });
        }catch (Exception e){
            Log.e(TAG,"更新通讯记录失败或者手机本地通讯录没有数据");
        }

    }


}
