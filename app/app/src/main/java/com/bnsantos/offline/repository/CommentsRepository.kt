package com.bnsantos.offline.repository

import android.arch.lifecycle.LiveData
import android.util.Log
import com.bnsantos.offline.db.CommentDao
import com.bnsantos.offline.models.Comment
import com.bnsantos.offline.network.CommentService
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton class CommentsRepository @Inject constructor(private val mDao: CommentDao, private val mService: CommentService) {
    private lateinit var mData: LiveData<List<Comment>>

    private fun init() {
        mData = readLocal()
        readServer()
    }

    fun read(): LiveData<List<Comment>> {
        init()
        return mData
    }

    private fun readServer() {
        mService.read()
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                        onNext = {
                            insert(it)
                            Log.i(CommentsRepository::class.java.simpleName, "onNext: Comments [${it?.size}] received")
                        },
                        onError =  {
                            Log.e(CommentsRepository::class.java.simpleName, "onError", it)
                        },
                        onComplete = {
                            Log.i(CommentsRepository::class.java.simpleName, "onCompleted")
                        }
                )
    }

    private fun readLocal(): LiveData<List<Comment>>{
        return mDao.read()
    }

    private fun insert(comments: List<Comment>?) {
        if (comments != null) {
            mDao.insert(comments)
        }
    }

    fun reload() {
        readServer()
    }
}