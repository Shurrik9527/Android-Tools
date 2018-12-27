package com.hz.maiku.maikumodule.util;

import android.content.Context;
import android.util.TypedValue;
import com.hz.maiku.maikumodule.R;

/**
 * Created by panruijie on 16/12/26.
 * Email : zquprj@gmail.com
 */

public class ColorUtil {

    public static int getColorPrimary(Context context){
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

    public static int getBackgroundColor(Context context){
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.background, typedValue, true);
        return typedValue.data;
    }
}
