package com.bnsantos.offline.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.bnsantos.offline.db.CommentDao
import com.bnsantos.offline.models.Comment
import com.bnsantos.offline.models.User
import com.bnsantos.offline.network.CommentService
import com.bnsantos.offline.vo.Resource
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton class CommentsRepository @Inject constructor(private val mDao: CommentDao, private val mService: CommentService) {
    private lateinit var mData: LiveData<List<Comment>>
    private val mCreate : MutableLiveData<Resource<Comment>> by lazy { MutableLiveData<Resource<Comment>>() }

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

    private fun createLocal(comment: Comment) {
        Observable.create<Boolean> {
            mDao.insert(comment)
        }
        .subscribeOn(Schedulers.io())
                .subscribeBy (
                    onNext = {
                        Log.i(CommentsRepository::class.java.simpleName, "onNext: Comment [${it}] received")
                    },
                    onError =  {
                        mCreate.postValue(Resource.Error("Error while creating comment", it))
                        Log.e(CommentsRepository::class.java.simpleName, "onError", it)
                    },
                    onComplete = {
                        Log.i(CommentsRepository::class.java.simpleName, "onCompleted")
                    }
                )

    }

    fun create(text: String, userId: String): LiveData<Resource<Comment>> {
        //TODO make JavaRX operations to fetch local user then create comment cache it and then create it through API
        //TODO 2 create random id generator
        val comment = Comment("comment-" + userId + "-1", text, user = User(userId, "Ainda uploading", "teste@email.com"))

        createLocal(comment)

        mService.create(userId, comment)
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                        onNext = {
                            mCreate.postValue(Resource.Success(it))
                            Log.i(CommentsRepository::class.java.simpleName, "onNext: Comment [${it}] received")
                        },
                        onError =  {
                            mCreate.postValue(Resource.Error("Error while creating comment", it))
                            Log.e(CommentsRepository::class.java.simpleName, "onError", it)
                        },
                        onComplete = {
                            Log.i(CommentsRepository::class.java.simpleName, "onCompleted")
                        }
                )

        return mCreate
    }
}