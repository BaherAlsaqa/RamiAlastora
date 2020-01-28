package com.ramialastora.ramialastora.admob;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.ramialastora.ramialastora.R;
import com.ramialastora.ramialastora.interfaces.Constants;
import com.ramialastora.ramialastora.utils.AppSharedPreferences;

//Created by Baher Alsaqa

public interface MobileAdsInterface {

    static void bannerAds(Context context, String adsKey, View view) {
        AdView adView = new AdView(context);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(adsKey);
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        adView = view.findViewById(R.id.adView);
        adView.setVisibility(View.VISIBLE);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

    }

    static InterstitialAd interstitialAds(Context context, int onClick, String adsKey) {

        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        InterstitialAd mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(adsKey);

        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
            }
        });

        return mInterstitialAd;
    }

    static void showInterstitialAd(InterstitialAd interstitialAd, Context context){
        AppSharedPreferences appSharedPreferences = new AppSharedPreferences(context);
        int x = appSharedPreferences.readInteger(Constants.count_ads);
        Log.d(Constants.Log + "7", "x = " + x);
        appSharedPreferences.writeInteger(Constants.count_ads, x + 1);
        x = appSharedPreferences.readInteger(Constants.count_ads);
        if (x==1 | x==5) {
            // Show Interstitial Ads
            if (interstitialAd.isLoaded()) {
                Log.d(Constants.Log + "onclick", "The interstitial is loaded");
                interstitialAd.show();
            } else {
                Log.d(Constants.Log + "onclick", "The interstitial wasn't loaded yet.");
            }
        }else if (x>=9){
            Log.d("log" + "131", "x = "+x);
            appSharedPreferences.writeInteger(Constants.count_ads, 0);
        }
    }

}
