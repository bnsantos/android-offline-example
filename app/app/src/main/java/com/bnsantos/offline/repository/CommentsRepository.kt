package com.bnsantos.offline.repository

import android.arch.lifecycle.LiveData
import android.util.Log
import com.bnsantos.offline.db.CommentDao
import com.bnsantos.offline.models.Comment
import com.bnsantos.offline.network.CommentService
import io.reactivex.processors.AsyncProcessor
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton class CommentsRepository {
    private val mDao: CommentDao
    private val mService: CommentService
    private val mData: LiveData<List<Comment>>
    private val mAsyncProcessor: AsyncProcessor<List<Comment>>
    private val mDisposableSubscriberRead: ReadCommentsDisposable

    @Inject
    constructor(mDao: CommentDao, mService: CommentService) {
        this.mDao = mDao
        this.mService = mService
        mAsyncProcessor = AsyncProcessor.create<List<Comment>>()
        mDisposableSubscriberRead = ReadCommentsDisposable()
        mData = readLocal()

        readServer()
    }

    fun read(): LiveData<List<Comment>> {
        return mData
    }

    private fun readServer() {
        mAsyncProcessor.subscribeWith(mDisposableSubscriberRead)
        mService.read()
                .subscribeOn(Schedulers.io())
                .subscribe(mAsyncProcessor)
    }

    private fun readLocal(): LiveData<List<Comment>>{
        return mDao.read()
    }

    private fun insert(comments: List<Comment>?) {
        if (comments != null) {
            mDao.insert(comments)
        }
    }

    private inner class ReadCommentsDisposable : DisposableSubscriber<List<Comment>>() {
        override fun onNext(comments: List<Comment>?) {
            insert(comments)
            Log.i(CommentsRepository::class.java.simpleName, "Comments: " + comments?.size)
        }

        override fun onComplete() {
            Log.i(CommentsRepository::class.java.simpleName, "onCompleted")
        }

        override fun onError(t: Throwable?) {
            Log.e(CommentsRepository::class.java.simpleName, "Error", t)
        }
    }
}