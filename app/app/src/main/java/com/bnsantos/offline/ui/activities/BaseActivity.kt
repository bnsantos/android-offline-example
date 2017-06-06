package com.bnsantos.offline.ui.activities

import android.arch.lifecycle.*
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import javax.inject.Inject

abstract class BaseActivity<T>(val mClass: Class<T>) : AppCompatActivity(), LifecycleRegistryOwner where T:ViewModel{
    @Suppress("LeakingThis")
    val mLifecycleRegistry = LifecycleRegistry(this)

    protected lateinit var mViewModel: T
    @Inject lateinit var mViewModelFactory: ViewModelProvider.Factory


    override fun getLifecycle(): LifecycleRegistry {
        return mLifecycleRegistry
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(mClass)
    }
}