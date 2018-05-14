package com.bnsantos.offline.di.module

import android.app.Application
import android.arch.lifecycle.ViewModelProvider
import android.arch.persistence.room.Room
import android.content.Context
import com.bnsantos.offline.Preferences
import com.bnsantos.offline.db.AppDB
import com.bnsantos.offline.db.CommentDao
import com.bnsantos.offline.db.UserDao
import com.bnsantos.offline.di.ViewModelFactory
import com.bnsantos.offline.di.component.ViewModelsComponent
import com.bnsantos.offline.network.CommentService
import com.bnsantos.offline.network.UserService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(subcomponents = arrayOf(ViewModelsComponent::class)) class AppModule(val mApp: Application) {
    @Singleton @Provides fun retrofit(): Retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.106:9000")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

    @Singleton @Provides fun userService(retrofit: Retrofit): UserService = retrofit.create(UserService::class.java)

    @Singleton @Provides fun commentService(retrofit: Retrofit): CommentService = retrofit.create(CommentService::class.java)

    @Singleton @Provides fun provideDB() : AppDB = Room.databaseBuilder(mApp, AppDB::class.java, "offline.db").build()

    @Singleton @Provides fun userDao(db: AppDB) : UserDao = db.userDao()

    @Singleton @Provides fun commentDao(db: AppDB): CommentDao = db.commentDao()

    @Singleton @Provides fun factory(builder: ViewModelsComponent.Builder): ViewModelProvider.Factory = ViewModelFactory(builder.build())

    @Singleton @Provides fun sharedPrefs(): Preferences = Preferences(mApp)

    @Singleton @Provides fun context(): Context = mApp
}