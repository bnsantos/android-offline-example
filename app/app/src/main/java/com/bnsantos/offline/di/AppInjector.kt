package com.bnsantos.offline.di

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.bnsantos.offline.App
import com.bnsantos.offline.di.component.DaggerAppComponent
import com.bnsantos.offline.di.module.AppModule
import dagger.android.AndroidInjection

object AppInjector: Application.ActivityLifecycleCallbacks {
    fun init(app: App){
        DaggerAppComponent.builder().application(app).setAppModule(AppModule(app)).build().inject(app)
        app.registerActivityLifecycleCallbacks(this)
    }

    override fun onActivityPaused(activity: Activity?) { }

    override fun onActivityResumed(activity: Activity?) { }

    override fun onActivityStarted(activity: Activity?) { }

    override fun onActivityDestroyed(activity: Activity?) { }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) { }

    override fun onActivityStopped(activity: Activity?) { }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        if (activity != null) {
            handleActivity(activity)
        }
    }

    private fun handleActivity(activity: Activity){
        AndroidInjection.inject(activity)
    }
}