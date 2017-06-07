package com.bnsantos.offline.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.bnsantos.offline.Preferences
import com.bnsantos.offline.db.UserDao
import com.bnsantos.offline.models.User
import com.bnsantos.offline.network.UserService
import com.bnsantos.offline.vo.Resource
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UserRepository @Inject constructor(
        private val mPreferences: Preferences,
        private val mDao: UserDao,
        private val mService: UserService){

    private val mRead : MutableLiveData<Resource<User>> by lazy { MutableLiveData<Resource<User>>() }
    private val mCreate : MutableLiveData<Resource<User>> by lazy { MutableLiveData<Resource<User>>() }

    private var mUserId: String = mPreferences.userId

    private fun init(){
        mDao.read(id = mUserId).observeForever { //TODO potentially leak? check this later
            if (it != null) {
                if (it.id == mUserId) {
                    mRead.postValue(Resource.Loading(it))
                }
            }
        }
    }

    fun read(): LiveData<Resource<User>>{
        init()
        readServer()
        return mRead
    }

    private fun readServer() {
        mService.read(mUserId)
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                        onNext = {
                            mDao.insert(it)
                            mRead.postValue(Resource.Success(it))
                        },
                        onError = {
                            Log.e(this@UserRepository::class.java.simpleName, "onError", it)
                            mRead.postValue(Resource.Error(it.localizedMessage, it))
                        }, onComplete = {
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
                                mCreate.postValue(Resource.Success(it))
                            }
                            Log.i(this@UserRepository::class.java.simpleName, "onNext: User ${it?.id} created")
                        },
                        onError = {
                            Log.e(this@UserRepository::class.java.simpleName, "onError", it)
                            mCreate.postValue(Resource.Error(it.localizedMessage, it))
                        },
                        onComplete = {
                            Log.i(this@UserRepository::class.java.simpleName, "onCompleted")
                        }
                )

        return  mCreate
    }
}