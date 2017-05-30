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

@Singleton class CommentsRepository {
    private val mDao: CommentDao
    private val mService: CommentService
    private val mData: LiveData<List<Comment>>

    @Inject
    constructor(mDao: CommentDao, mService: CommentService) {
        this.mDao = mDao
        this.mService = mService
        mData = readLocal()
        readServer()
    }

    fun read(): LiveData<List<Comment>> {
        return mData
    }

    private fun readServer() {
        mService.read()
                .observeOn(Schedulers.newThread())
                .subscribeBy(
                        onNext = { comments ->
                            mDao.insert(comments)
                            Log.i(CommentsRepository::class.java.simpleName, "Comments: " + comments.size)
                        },
                        onError = {
                            Log.e(CommentsRepository::class.java.simpleName, "Error", it)
                        },
                        onComplete = {
                            Log.i(CommentsRepository::class.java.simpleName, "onCompleted")
                        })
    }

    private fun readLocal(): LiveData<List<Comment>>{
        return mDao.read()
    }
}