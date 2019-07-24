package com.hz.maiku.maikumodule.manager;

import android.content.Context;
import com.hz.maiku.maikumodule.widget.dialog.LoadingDialog;


/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 进度条
 * @date 2018/10/11
 * @email 252774645@qq.com
 */
public class LoadingDialogManager {


    private static LoadingDialog mDialog;
    private static Context mContext;
    public static boolean isShow() {
        if (mDialog == null) {
            return false;
        }
        return mDialog.isShowing();
    }

    /**
     * 显示进度条
     * @param context
     */
    public static void show(Context context){
        if(context==null){
            return;
        }
        if(mDialog ==null){
            mContext =context;
            mDialog = new LoadingDialog(context);
        }
        mDialog.show();
    }


    /**
     * 隐藏进度条
     */
    public static void hideProgressDialog() {
        if(mContext==null){
            return;
        }
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
    }


}
