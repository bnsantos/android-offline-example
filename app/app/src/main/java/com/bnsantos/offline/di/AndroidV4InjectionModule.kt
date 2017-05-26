package com.bnsantos.offline.di

import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.Multibinds

@Module abstract class AndroidV4InjectionModule {
    @Multibinds
    internal abstract fun bindv4Fragments(): Map<Class<out android.support.v4.app.Fragment>, AndroidInjector.Factory<out android.support.v4.app.Fragment>>
}