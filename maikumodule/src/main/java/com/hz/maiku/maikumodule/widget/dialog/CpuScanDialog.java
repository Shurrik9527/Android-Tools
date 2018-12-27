package com.hz.maiku.maikumodule.widget.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hz.maiku.maikumodule.R;


/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/9/13
 * @email 252774645@qq.com
 */
public class CpuScanDialog extends DialogFragment{

    private static final String TAG = CpuScanDialog.class.getName();
    private RelativeLayout mBackGroundRl;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.cpucooler_scan_dialo_layout, null);
        ImageView imageView =view.findViewById(R.id.cpucooler_thermometer_iv);
        this.mBackGroundRl =view.findViewById(R.id.cpucooler_background_rl);
        int width =imageView.getLayoutParams().width;
        int height =imageView.getLayoutParams().height;
        RelativeLayout.LayoutParams param =new RelativeLayout.LayoutParams(5 * width / 8, 5 * height / 8);
        param.addRule(RelativeLayout.CENTER_IN_PARENT);
        imageView.setLayoutParams(param);
//        TopBottomScanView mTopBottomScanView =view.findViewById(R.id.cpucooler_scan_tbsv);
//        mTopBottomScanView.setScanAnimatorDuration(1000);
        final Dialog dialog = new Dialog(getActivity(), R.style.dialogTransparent);

        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        });

        return dialog;
    }

    /**
     * 设置背景颜色
     * @param color
     */
    public void setmBackGroundRlColor(int color){
        this.mBackGroundRl.setBackgroundColor(color);
    }



}
