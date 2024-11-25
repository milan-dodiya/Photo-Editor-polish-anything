package com.example.photoeditorpolishanything

import android.app.Application
import com.google.android.gms.ads.MobileAds

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Mobile Ads SDK
        MobileAds.initialize(this) {}
    }
}