package com.hz.maiku.maikumodule.util;

import android.content.Context;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;
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

    public static void onAdClick(Context context, String channel, String placementId) {
        Map<String, Object> eventValues = new HashMap<>();
        eventValues.put(AFInAppEventParameterName.AF_CHANNEL, channel);
        eventValues.put(AFInAppEventParameterName.AD_REVENUE_PLACEMENT_ID, placementId);
        AppsFlyerLib.getInstance().trackEvent(context, AFInAppEventType.AD_CLICK, eventValues);
    }
}
