package com.bnsantos.offline.di

import com.bnsantos.offline.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module abstract class MainActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity
}