package com.bnsantos.offline.di

import com.bnsantos.offline.App
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        AndroidInjectionModule::class,
        AndroidV4InjectionModule::class,
        ApiModule::class,
        DBModule::class,
        MainActivityModule::class
))

interface AppComponent {
    @Component.Builder interface Builder {
        @BindsInstance fun application(app: App): Builder
        fun build(): AppComponent
    }

    fun inject(app: App)
}

