package com.hz.maiku.maikumodule.widget.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.hz.maiku.maikumodule.R;


/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/9/13
 * @email 252774645@qq.com
 */
public class CpuCleanDialog extends DialogFragment{

    private static final String TAG = CpuCleanDialog.class.getName();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.cpucooler_rotate_clean_dialog_layout, null);
        ImageView imageView =view.findViewById(R.id.cpucooler_rotate_clean_iv);
        ImageView m =view.findViewById(R.id.cpucooler_rotate_one_iv);
        Animation rotate = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_chean_anim);
        imageView.startAnimation(rotate);
        m.setAnimation(rotate);
       // imageView.clearAnimation();
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




}
