package com.bnsantos.offline.di.module

import com.bnsantos.offline.ui.activities.MainActivity
import com.bnsantos.offline.ui.fragments.CreateCommentFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module abstract class MainActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributeCreateCommentFragment(): CreateCommentFragment
}