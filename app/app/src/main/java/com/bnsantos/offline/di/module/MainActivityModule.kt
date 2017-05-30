package com.bnsantos.offline.di.module

import com.bnsantos.offline.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module abstract class MainActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity
}