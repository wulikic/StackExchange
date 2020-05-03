package com.vesna.stackexchange.di

import android.app.Activity
import dagger.Component

@Component(modules = [ AppModule::class ])
interface AppComponent {


    fun inject(activity: Activity)
}