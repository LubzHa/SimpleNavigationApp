package com.example.navigationapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NavigationApplication: Application() {

    override fun onCreate() {
        super.onCreate()
    }
}