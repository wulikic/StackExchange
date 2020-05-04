package com.vesna.stackexchange.di

import com.vesna.stackexchange.presentation.search.SearchActivity
import dagger.Component

@Component(modules = [ AppModule::class ])
interface AppComponent {

    fun inject(activity: SearchActivity)
}