package com.bnsantos.offline.ui.activities

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.bnsantos.offline.R
import com.bnsantos.offline.models.Comment
import com.bnsantos.offline.ui.CommentsAdapter
import com.bnsantos.offline.ui.CommentsDiffUtilCallback
import com.bnsantos.offline.viewmodel.CommentsViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity<CommentsViewModel>(CommentsViewModel::class.java) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = LinearLayoutManager(this)
        swipeRefreshLayout.setOnRefreshListener {
            mViewModel.reload()
        }
        val adapter = CommentsAdapter()
        recyclerView.adapter = adapter

        mViewModel.read().observe(this, Observer<List<Comment>> { comments ->
            swipeRefreshLayout.isRefreshing = false
            if (comments != null) { //TODO Being executed on UI Thread
                val cb = CommentsDiffUtilCallback(adapter.mComments, comments)
                val result = DiffUtil.calculateDiff(cb)
                result.dispatchUpdatesTo(adapter)
                adapter.swap(comments)
            }
            Log.i(MainActivity::class.java.simpleName, "Comments: " + comments?.size)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.editUser){
            editUser()
            return true
        }else {
            return super.onOptionsItemSelected(item)
        }
    }

    private fun editUser(){
        startActivity(Intent(this, UserActivity::class.java))
    }
}
