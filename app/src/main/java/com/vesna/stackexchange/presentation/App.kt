package com.vesna.stackexchange.presentation

import android.app.Application
import com.vesna.stackexchange.di.AppComponent
import com.vesna.stackexchange.di.DaggerAppComponent

class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().build()
    }
}