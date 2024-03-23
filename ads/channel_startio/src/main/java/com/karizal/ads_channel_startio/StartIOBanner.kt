package com.karizal.ads_channel_startio

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.core.view.allViews
import com.karizal.ads_base.AdsBaseConst
import com.karizal.ads_base.contract.BannerContract
import com.startapp.sdk.ads.banner.Banner
import com.startapp.sdk.ads.banner.BannerListener
import com.startapp.sdk.adsbase.StartAppAd
import com.startapp.sdk.adsbase.StartAppSDK


class StartIOBanner(
    private val data: StartIOData
) : BannerContract {
    override val name: String = AdsBaseConst.startio
    override var isDebug: Boolean = false
    override var activity: Activity? = null

    override fun initialize(activity: Activity, isDebug: Boolean) {
        super.initialize(activity, isDebug)
        StartAppSDK.init(activity, data.app_id, false)
        StartAppSDK.enableReturnAds(false)
        StartAppAd.disableAutoInterstitial()
        StartAppAd.disableSplash()
        if (isDebug) {
            StartAppSDK.setTestAdsEnabled(true);
        }
    }

    override fun fetch(
        container: ViewGroup,
        preparing: () -> Unit,
        possibleToLoad: () -> Boolean,
        onSuccessLoaded: (channel: String) -> Unit,
        onFailedLoaded: () -> Unit
    ) {
        activity ?: return
        prepareContainerView(container)
        preparing.invoke()
        val banner = Banner(activity, object : BannerListener {
            override fun onReceiveAd(p0: View?) {
                val view = container.allViews.first()
                view.layoutParams = FrameLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                )
                onSuccessLoaded.invoke(name)
                Log.i(this@StartIOBanner.getClassName(), "StartIO.banner.onReceiveAd")
            }

            override fun onFailedToReceiveAd(p0: View?) {
                Log.i(this@StartIOBanner.getClassName(), "StartIO.banner.onFailedToReceiveAd")
            }

            override fun onImpression(p0: View?) {
                Log.i(this@StartIOBanner.getClassName(), "StartIO.banner.onImpression")
            }

            override fun onClick(p0: View?) {
                Log.i(this@StartIOBanner.getClassName(), "StartIO.banner.onClick")
            }

        })
        val bannerParameters = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        bannerParameters.addRule(RelativeLayout.CENTER_HORIZONTAL)
        bannerParameters.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)

        if (possibleToLoad.invoke()) {
            container.removeAllViewsInLayout()
            container.addView(banner, bannerParameters)
            banner.loadAd()
        }
    }
}