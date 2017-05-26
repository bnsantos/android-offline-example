package com.bnsantos.offline.di

import com.bnsantos.offline.network.CommentService
import com.bnsantos.offline.network.UserService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module class ApiModule {
    private val mRetrofit: Retrofit

    init {
        mRetrofit = Retrofit.Builder()
                .baseUrl("http://192.168.0.106:9000")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    @Singleton @Provides fun UserService(): UserService {
        return mRetrofit.create(UserService::class.java)
    }

    @Singleton @Provides fun CommentService(): CommentService {
        return mRetrofit.create(CommentService::class.java)
    }
}