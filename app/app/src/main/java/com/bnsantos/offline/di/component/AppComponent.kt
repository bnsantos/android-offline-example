package com.bnsantos.offline.di.component

import com.bnsantos.offline.App
import com.bnsantos.offline.di.module.AppModule
import com.bnsantos.offline.di.module.MainActivityModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        AndroidInjectionModule::class,
        AndroidSupportInjectionModule::class,
        MainActivityModule::class,
        AppModule::class
))

interface AppComponent {
    @Component.Builder interface Builder {
        @BindsInstance fun application(app: App): Builder
        fun setAppModule(module: AppModule): Builder
        fun build(): AppComponent
    }

    fun inject(app: App)
}

