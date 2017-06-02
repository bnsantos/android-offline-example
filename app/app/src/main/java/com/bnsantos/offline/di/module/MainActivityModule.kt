package com.bnsantos.offline.di.module

import com.bnsantos.offline.ui.activities.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module abstract class MainActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity
}