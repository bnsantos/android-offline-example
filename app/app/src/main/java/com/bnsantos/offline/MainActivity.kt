package com.bnsantos.offline

import android.arch.lifecycle.*
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.bnsantos.offline.models.Comment
import com.bnsantos.offline.ui.CommentsAdapter
import com.bnsantos.offline.ui.CommentsDiffUtilCallback
import com.bnsantos.offline.viewmodel.CommentsViewModel
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), LifecycleRegistryOwner {
    lateinit var mViewModel: CommentsViewModel
    @Inject lateinit var mViewModelFactory: ViewModelProvider.Factory
    val mLifecycleRegistry = LifecycleRegistry(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = CommentsAdapter()
        recyclerView.adapter = adapter

        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(CommentsViewModel::class.java)
        mViewModel.read().observe(this, Observer<List<Comment>> { comments ->
            if (comments != null) { //Being executed on UI Thread
                val cb = CommentsDiffUtilCallback(adapter.mComments, comments)
                val result = DiffUtil.calculateDiff(cb)
                result.dispatchUpdatesTo(adapter)
                adapter.swap(comments)
            }
            Log.i(MainActivity::class.java.simpleName, "Comments: " + comments?.size)
        })
    }

    override fun getLifecycle(): LifecycleRegistry {
        return mLifecycleRegistry
    }
}
