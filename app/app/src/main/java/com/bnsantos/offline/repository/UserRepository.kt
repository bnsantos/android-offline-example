package com.bnsantos.offline.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.bnsantos.offline.Preferences
import com.bnsantos.offline.db.UserDao
import com.bnsantos.offline.models.User
import com.bnsantos.offline.network.UserService
import com.bnsantos.offline.vo.Resource
import io.reactivex.processors.AsyncProcessor
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber
import javax.inject.Inject

class UserRepository @Inject constructor(
        private val mPreferences: Preferences,
        private val mDao: UserDao,
        private val mService: UserService){

    private val mAsyncProcessor = AsyncProcessor.create<User>()
    private val mDisposableSubscriber = ReadUserDisposable()
    private val mData : MutableLiveData<Resource<User>> by lazy { MutableLiveData<Resource<User>>() }

    private var mUserId: String = mPreferences.userId

    private fun readCached(userId: String){
        mAsyncProcessor.subscribeWith(mDisposableSubscriber)
        mDao.read(id = userId)
                .subscribeOn(Schedulers.io())
                .subscribe(mAsyncProcessor)
    }

    fun read(userId: String = mUserId): LiveData<Resource<User>>{
        readCached(userId)
        readServer()
        return mData
    }

    private fun readServer() {
        mService.read(mUserId)
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                        onNext = {
                            mDao.insert(it)
                            mData.postValue(Resource.Success(it))
                        },
                        onError = {
                            Log.e(this@UserRepository::class.java.simpleName, "onError", it)
                            mData.postValue(Resource.Error(it.localizedMessage, it))
                        },
                        onComplete = {
                            Log.i(this@UserRepository::class.java.simpleName, "onCompleted")
                        }
                )
    }

    fun create(user: User) : LiveData<Resource<User>>{
        mService.create(user)
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                        onNext = {
                            if (it != null) {
                                mDao.insert(it)
                                mPreferences.userId = it.id
                                mUserId = it.id
                                mData.postValue(Resource.Success(it))
                            }
                            Log.i(this@UserRepository::class.java.simpleName, "onNext: User ${it?.id} created")
                        },
                        onError = {
                            Log.e(this@UserRepository::class.java.simpleName, "onError", it)
                            mData.postValue(Resource.Error(it.localizedMessage, it))
                        },
                        onComplete = {
                            Log.i(this@UserRepository::class.java.simpleName, "onCompleted")
                        }
                )

        return  mData
    }

    private inner class ReadUserDisposable: DisposableSubscriber<User>() {
        override fun onError(t: Throwable?) {
            if (t != null) {
                Log.e(this@UserRepository::class.java.simpleName, "onError", t)
                mData.postValue(Resource.Error("Error", t))
            }
        }

        override fun onNext(t: User?) {
            if (t != null) {
                mData.postValue(Resource.Loading(t))
            }
        }

        override fun onComplete() {
            Log.i(this@UserRepository::class.java.simpleName, "onCompleted")
        }

    }
}