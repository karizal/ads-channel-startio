package com.karizal.ads_channel_startio_sample

import android.app.Application
import com.karizal.ads_channel_startio.StartIOConst

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        StartIOConst.init(this, Const.data)
    }
}