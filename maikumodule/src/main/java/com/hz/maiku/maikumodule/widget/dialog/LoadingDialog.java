package com.hz.maiku.maikumodule.widget.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import com.hz.maiku.maikumodule.R;


/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 进度条
 * @date 2018/10/11
 * @email 252774645@qq.com
 */
public class LoadingDialog extends AlertDialog {

    /**
     * 构造方法
     * @param context
     */
    public LoadingDialog(Context context) {
        super(context, R.style.common_alert_dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.view_loading_dialog);
        this.setCanceledOnTouchOutside(false);
    }


}
