package com.vesna.stackexchange.di

import com.vesna.stackexchange.BuildConfig
import com.vesna.stackexchange.data.GetFirst20UsersSortedAlphabeticallyImpl
import com.vesna.stackexchange.data.retrofit.RetrofitApi
import com.vesna.stackexchange.domain.GetFirst20UsersSortedAlphabetically
import com.vesna.stackexchange.presentation.search.SearchProviderFactory
import dagger.Module
import dagger.Provides
import dagger.Reusable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class AppModule {

    @Provides @Reusable
    fun searchProviderFactory(searchUsers: GetFirst20UsersSortedAlphabetically): SearchProviderFactory {
        return SearchProviderFactory(searchUsers)
    }

    @Provides @Reusable
    fun getFirst20UsersSortedAlphabetically(retrofitApi: RetrofitApi): GetFirst20UsersSortedAlphabetically {
        return GetFirst20UsersSortedAlphabeticallyImpl(retrofitApi)
    }

    @Provides @Reusable
    fun retrofitApi(retrofit: Retrofit): RetrofitApi {
        return retrofit.create(RetrofitApi::class.java)
    }

    @Provides @Reusable
    fun retrofit(): Retrofit {
        return Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.API_URL)
            .build()
    }
}