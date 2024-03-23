package com.karizal.ads_channel_startio

import android.app.Application
import com.startapp.sdk.adsbase.StartAppAd
import com.startapp.sdk.adsbase.StartAppSDK


object StartIOConst {
    fun init(application: Application, data: StartIOData) {
        StartAppSDK.init(application, data.app_id, false)
        StartAppSDK.enableReturnAds(false)
        StartAppAd.disableAutoInterstitial()
        StartAppAd.disableSplash()
    }
}