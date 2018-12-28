package com.hz.maiku.maikumodule.util;

import android.content.Context;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AppsFlyerLib;

import java.util.HashMap;
import java.util.Map;

/**
 * AppsFlyer EventUtil
 * @author Shurrik
 */
public class EventUtil {
    public static void sendEvent(Context context, String evnetType, String message) {
        Map<String, Object> eventValues = new HashMap<>();
        eventValues.put(AFInAppEventParameterName.CONTENT, message);
        AppsFlyerLib.getInstance().trackEvent(context, evnetType, eventValues);
    }
}
