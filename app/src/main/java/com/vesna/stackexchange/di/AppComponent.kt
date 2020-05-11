package com.vesna.stackexchange.di

import com.vesna.stackexchange.presentation.Injector
import dagger.Component

@Component(modules = [ AppModule::class, ConfigModule::class ])
interface AppComponent : Injector