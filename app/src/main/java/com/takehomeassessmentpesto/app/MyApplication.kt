package com.takehomeassessmentpesto.app

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application(), LifecycleObserver {

    override fun onCreate() {
        super.onCreate()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        // perform common functionality that you want to perform on stop
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        // perform common functionality that you want to perform on stop
    }

}