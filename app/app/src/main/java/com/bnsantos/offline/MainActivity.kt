package com.bnsantos.offline

import android.arch.lifecycle.*
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.bnsantos.offline.models.Comment
import com.bnsantos.offline.viewmodel.CommentsViewModel
import javax.inject.Inject

class MainActivity : AppCompatActivity(), LifecycleRegistryOwner {
    lateinit var mViewModel: CommentsViewModel
    @Inject lateinit var mViewModelFactory: ViewModelProvider.Factory
    val mLifecycleRegistry = LifecycleRegistry(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(CommentsViewModel::class.java)
        mViewModel.read().observe(this, Observer<List<Comment>> { comments ->
            Log.i(MainActivity::class.java.simpleName, "Comments: " + comments?.size)
        })
    }

    override fun getLifecycle(): LifecycleRegistry {
        return mLifecycleRegistry
    }
}
