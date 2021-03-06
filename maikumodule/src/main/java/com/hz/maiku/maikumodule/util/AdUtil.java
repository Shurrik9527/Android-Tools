package com.hz.maiku.maikumodule.util;

import android.content.Context;
import android.util.Log;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.hz.maiku.maikumodule.base.Constant;
import com.hz.maiku.maikumodule.bean.AdInfo;
import com.hz.maiku.maikumodule.http.HttpCenter;
import com.hz.maiku.maikumodule.http.HttpResult;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AdUtil {
    //广告出现的时间
    public static long SHOW_TIME = 0;
    private static int AD_TYPE = AdUtil.TYPE_FACEBOOK;
    /**
     * 1 开启
     * 0 关闭
     */
    public static int AD_STATUS = 1;
    public static long AD_TIME = 3600000;
    public static int ABOUT_STATUS = 0;


    private static final int TYPE_FACEBOOK = 1;
    private static final int TYPE_ADMOB = 2;
    private static final int TYPE_BAIDU = 3;
    private static final String TAG = AdUtil.class.getSimpleName();
    /**
     * 发布第一版的时候设置为false, 其余情况下都是true
     */
    public static boolean IS_SHOW_AD = true;

    /**
     * 直接显示广告
     */
    public static void showAds(Context context, String source) {
        if (IS_SHOW_AD) {
            switch (AdUtil.AD_TYPE) {
                case TYPE_FACEBOOK:
                    AdUtil.showFacebookAds(context);
                    break;
                case TYPE_ADMOB:
                    AdUtil.showAdModAds(context);
//                    AdUtil.showUnityAds(context);
                    break;
//                case TYPE_BAIDU:
//                    AdUtil.showAdtiming(context);
//                    break;
                default:
                    //判断Fackbook是否安装
                    if (AppUtil.isInstalled(context, "com.facebook.katana")) {
                        AdUtil.showFacebookAds(context);
                    } else {
                        AdUtil.showAdModAds(context);
//                        AdUtil.showUnityAds(context);
                    }
                    break;
            }
            Log.d(TAG, "Interstitial ad at " + source);
        }
    }

//    public static void showUnityAds(final Activity context){
//        String unityGameID = "3144679";
//        final String placementId = "Loading";
//
//        IUnityMonetizationListener myListener = new IUnityMonetizationListener() {
//            @Override
//            public void onUnityServicesError(UnityServices.UnityServicesError unityServicesError, String s) {
//                Log.e(TAG, "Interstitial ad onUnityServicesError." + s);
//            }
//
//            @Override
//            public void onPlacementContentReady(String s, PlacementContent placementContent) {
//                if(s.equals(placementId)) {
//                    // Retrieve the PlacementContent that is ready:
//                    AdUtil.showUnityPlacement(context, placementId);
//                }
//            }
//
//            @Override
//            public void onPlacementContentStateChange(String s, PlacementContent placementContent, UnityMonetization.PlacementContentState placementContentState, UnityMonetization.PlacementContentState placementContentState1) {
//                Log.d(TAG, "Interstitial ad onPlacementContentStateChange.");
//            }
//        };
//
//        if(UnityMonetization.isReady(placementId)) {
//            Log.d(TAG, "Interstitial ad is ready.");
//            AdUtil.showUnityPlacement(context, placementId);
//        } else {
//            UnityMonetization.initialize (context, unityGameID, myListener, false);
//        }
//    }
//
//    private static void showUnityPlacement(final Activity context, String placementId ) {
//        PlacementContent pc = UnityMonetization.getPlacementContent (placementId);
//        // Check that the PlacementContent is the desired type:
//        if (pc.getType ().equalsIgnoreCase ("SHOW_AD")) {
//            // Cast the PlacementContent as the desired type:
//            ShowAdPlacementContent p = (ShowAdPlacementContent) pc;
//            // Show the PlacementContent:
//            p.show (context, null);
//            Log.d(TAG, "Loading Interstitial Showing.");
//        }
//    }
//
//    /**
//     * Adting
//     */
//    private static void showAdtiming(final Context context) {
//        String placementId = "4965";
//        final com.aiming.mdt.sdk.ad.interstitialAd.InterstitialAd interstitialAd = new InterstitialAd(context, placementId);
//        interstitialAd.setListener(new com.aiming.mdt.sdk.ad.interstitialAd.InterstitialAdListener() {
//            String TAG = "Adtiming";
//            @Override
//            public void onADReady() {
//                // interstitialAd is load success
//                Log.d(TAG, "Interstitial ad is load success.");
//                interstitialAd.show(context);
//            }
//            @Override
//            public void onADClick() {
//                // interstitialAd click
//                Log.d(TAG, "Interstitial ad click.");
//            }
//            @Override
//            public void onADFail(String msg) {
//                // interstitialAd fail
//                Log.e(TAG, "Interstitial ad fail. errorMsg is " + msg);
//                interstitialAd.destroy(context);
//            }
//            @Override
//            public void onADClose() {
//                // interstitialAd close
//                Log.d(TAG, "Interstitial ad close.");
//                interstitialAd.destroy(context);
//            }
//        });
//        interstitialAd.loadAd(context);
//    }

    /**
     * Facebook
     * @param context
     */
    private static void showFacebookAds(final Context context) {
        //Logcat search "Test mode device hash"
        AdSettings.addTestDevice("5ecd25e8-83a8-4528-b785-45c098c0b4f7");
        final com.facebook.ads.InterstitialAd interstitialAd = new com.facebook.ads.InterstitialAd(context, Constant.PLACEMENT_ID);
        // Set listeners for the Interstitial Ad
        interstitialAd.setAdListener(new InterstitialAdListener() {
            String TAG = "Facebook";

            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.e(TAG, "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                Log.e(TAG, "Interstitial ad dismissed.");
                interstitialAd.destroy();
                //RxBus.getDefault().post(new EmptyEvent());
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
                interstitialAd.destroy();
                boolean showbtn = (boolean) SpHelper.getInstance().get(Constant.ABOUT_SHOW_BTN,false);
                if(showbtn){
                   ToastUtil.showToast(context,"Interstitial ad failed to load: " + adError.getErrorMessage());
                }
                //RxBus.getDefault().post(new EmptyEvent());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                // Show the ad
                interstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d(TAG, "Interstitial ad clicked!");
                EventUtil.onAdClick(context, TAG, Constant.PLACEMENT_ID);
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d(TAG, "Interstitial ad impression logged!");
            }
        });

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        interstitialAd.loadAd();
    }

    /**
     * It could be that you have only recently created a new Ad Unit ID and requesting for live ads.
     * It could take a few hours for ads to start getting served if that is that case.
     * If you are receiving test ads then your implementation is fine.
     * Just wait a few hours and see if you are able to receive live ads then.
     * If not, can send us your Ad Unit ID for us to look into.
     *
     * @param context
     */
    private static void showAdModAds(final Context context) {
        //初始化AdMob
        MobileAds.initialize(context);

        //初始化Interstitial Ads
        final com.google.android.gms.ads.InterstitialAd interstitialAd = new com.google.android.gms.ads.InterstitialAd(context);
        interstitialAd.setAdUnitId(Constant.UNIT_ID);
        AdRequest request = new AdRequest.Builder()
//                .addTestDevice("2709A5CDA4B233AC4191F0DB0E2320E8")
                .build();
        interstitialAd.loadAd(request);
        //初始化成功以后直接显示
        interstitialAd.setAdListener(new AdListener() {
            String TAG = "AdMob";

            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                interstitialAd.show();
                Log.e(TAG, "Interstitial ad onAdLoaded.");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.e(TAG, "Interstitial ad onAdFailedToLoad. errorCode is " + errorCode);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
                Log.d(TAG, "Interstitial ad onAdOpened.");
                EventUtil.onAdClick(context, TAG, Constant.UNIT_ID);
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Log.e(TAG, "Interstitial ad onAdLeftApplication.");
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.
                Log.d(TAG, "Interstitial ad onAdClosed.");
            }
        });
    }

    /**
     * 获取最新的广告配置
     */
    public static void getAdType() {
        AdUtil.getAdTypeAndShow(null, null);
    }

    /**
     * 获取最新的广告配置并展示
     */
    public static void getAdTypeAndShow(final Context context, final String source) {
        if (IS_SHOW_AD) {
            HttpCenter.getService().getAdType("getad_type", Constant.APP_NAME).subscribeOn(Schedulers.io())//指定网络请求所在的线程
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(Disposable disposable) throws Exception {
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())//指定的是它之后（下方）执行的操作所在的线程
                    .doFinally(new Action() {
                        @Override
                        public void run() throws Exception {
                            if (context != null) {
                                AdUtil.showAds(context, source);
                            }
                        }
                    })
                    .subscribe(new Observer<HttpResult<AdInfo>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(HttpResult<AdInfo> adInfoHttpResult) {
                            if (adInfoHttpResult.getResult() == 0) {
                                if (adInfoHttpResult.getData() != null) {
                                    AdInfo adInfo = adInfoHttpResult.getData();
                                    AdUtil.AD_TYPE = adInfo.getAd_type();
                                    AdUtil.AD_STATUS = adInfo.getAd_status();
                                    AdUtil.AD_TIME = adInfo.getAd_time();
                                    AdUtil.ABOUT_STATUS = adInfo.getAbout_status();
                                    Log.d(TAG, "Interstitial ad type is [" + adInfo.getAd_name() + "] now! status is [" + AdUtil.ABOUT_STATUS + "] and time is " + AdUtil.AD_TIME);
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(TAG, "Interstitial ad type isn't Changed!");
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
        }
    }
}
