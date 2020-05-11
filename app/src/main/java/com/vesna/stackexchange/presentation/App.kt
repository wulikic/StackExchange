package com.vesna.stackexchange.presentation

import android.app.Application
import com.vesna.stackexchange.di.DaggerAppComponent
import com.vesna.stackexchange.presentation.search.SearchActivity

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        DependencyGraph.injector = DaggerAppComponent.builder().build()
    }
}

interface Injector {
    fun inject(activity: SearchActivity)
}

object DependencyGraph {
    lateinit var injector: Injector
}