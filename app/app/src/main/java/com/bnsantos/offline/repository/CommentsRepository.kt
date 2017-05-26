package com.bnsantos.offline.repository

import android.arch.lifecycle.LiveData
import android.util.Log
import com.bnsantos.offline.db.AppDB
import com.bnsantos.offline.db.CommentDao
import com.bnsantos.offline.models.Comment
import com.bnsantos.offline.network.CommentService
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton class CommentsRepository {
    private val mDB: AppDB
    private val mDao: CommentDao
    private val mService: CommentService
    private val mData: LiveData<List<Comment>>

    @Inject
    constructor(mDB: AppDB, mDao: CommentDao, mService: CommentService) {
        this.mDB = mDB
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
                .observeOn(Schedulers.io())
                .subscribe({ comments ->
//                    mDao.insert(comments) //TODO doing on ui thread
                    Log.i(CommentsRepository::class.java.simpleName, "Comments: " + comments.size)
                }, { throwable ->
                    Log.e(CommentsRepository::class.java.simpleName, "Error", throwable)
                }, {
                    Log.i(CommentsRepository::class.java.simpleName, "onCompleted")
                })
    }

    private fun readLocal(): LiveData<List<Comment>>{
        return mDao.read()
    }
}