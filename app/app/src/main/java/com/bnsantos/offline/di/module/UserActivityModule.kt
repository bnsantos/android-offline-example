package com.bnsantos.offline.di.module

import com.bnsantos.offline.ui.activities.UserActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module abstract class UserActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeUserActivity(): UserActivity
}