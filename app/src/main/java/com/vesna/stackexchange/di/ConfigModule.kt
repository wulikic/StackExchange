package com.vesna.stackexchange.di

import com.vesna.stackexchange.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.Reusable
import javax.inject.Named

@Module
class ConfigModule {

    @Provides
    @Reusable
    @Named("api")
    fun baseUrl(): String {
        return BuildConfig.API_URL
    }
}