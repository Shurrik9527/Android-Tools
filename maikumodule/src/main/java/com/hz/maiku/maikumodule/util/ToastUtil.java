package com.hz.maiku.maikumodule.util;

import android.content.Context;
import android.widget.Toast;

/**
 * 始终使用一个Toast显示信息,Toast显示时长始终是我们设置的Toast.LENGTH_SHORT或Toast.LENGTH_LONG
 */
public class ToastUtil {

    private static Toast toast;

    public static void showToast(Context context, String message) {
        if (toast == null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
        }
        toast.show();
    }
}
