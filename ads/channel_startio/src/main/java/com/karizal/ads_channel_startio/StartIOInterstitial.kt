package com.karizal.ads_channel_startio

import android.app.Activity
import com.karizal.ads_base.AdsBaseConst
import com.karizal.ads_base.contract.InterstitialContract
import com.startapp.sdk.adsbase.Ad
import com.startapp.sdk.adsbase.StartAppAd
import com.startapp.sdk.adsbase.adlisteners.AdDisplayListener
import com.startapp.sdk.adsbase.adlisteners.AdEventListener


class StartIOInterstitial(private val data: StartIOData) : InterstitialContract {
    override val name: String = AdsBaseConst.startio
    override var isDebug: Boolean = false
    override var activity: Activity? = null
    override var onInitializeOK: (name: String) -> Unit = {}
    override var onInitializeError: (name: String) -> Unit = {}
    private var interstitial: StartAppAd? = null
    private var onHide: () -> Unit = {}
    private var onFailure: (Activity) -> Unit = {}

    private val listenerOnLoad = object : AdEventListener {

        override fun onReceiveAd(ad: Ad) {
            onInitializeOK.invoke(name)
        }

        override fun onFailedToReceiveAd(ad: Ad?) {
            onInitializeError.invoke(name)
        }
    }

    private val listenerOnDisplay = object : AdDisplayListener {
        override fun adHidden(ad: Ad?) {
            interstitial?.close()
            onHide.invoke()
            activity?.let { initialize(it, isDebug, onInitializeOK, onInitializeError) }
        }

        override fun adDisplayed(ad: Ad?) {
        }

        override fun adClicked(ad: Ad?) {
        }

        override fun adNotDisplayed(ad: Ad?) {
            activity?.let { onFailure.invoke(it) }
        }

    }

    override fun initialize(
        activity: Activity,
        isDebug: Boolean,
        onInitializeOK: (name: String) -> Unit,
        onInitializeError: (name: String) -> Unit
    ) {
        super.initialize(activity, isDebug, onInitializeOK, onInitializeError)
        interstitial = StartAppAd(activity)
        interstitial?.loadAd(listenerOnLoad)
    }

    override fun show(
        activity: Activity,
        possibleToShow: (channel: String) -> Boolean,
        onHide: () -> Unit,
        onFailure: (activity: Activity) -> Unit
    ) {
        if (possibleToShow.invoke(name).not()) {
            return onFailure.invoke(activity)
        }

        this.onHide = onHide
        this.onFailure = onFailure

        if (interstitial?.isReady == true) {
            interstitial?.showAd(listenerOnDisplay)
        } else {
            onFailure.invoke(activity)
        }
    }
}