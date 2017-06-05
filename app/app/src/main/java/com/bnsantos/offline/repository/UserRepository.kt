package com.bnsantos.offline.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.bnsantos.offline.Preferences
import com.bnsantos.offline.db.UserDao
import com.bnsantos.offline.models.User
import com.bnsantos.offline.network.UserService
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UserRepository @Inject constructor(
        private val mPreferences: Preferences,
        private val mDao: UserDao,
        private val mService: UserService){
    private var mMutable = MutableLiveData<User>()
    private var mUserId: String = mPreferences.userId

    private fun init(){
        mDao.read(id = mUserId).observeForever { //TODO potentially leak?
            if (it?.id == mUserId) {
                mMutable.postValue(it)
            }
        }
    }

    fun read(): LiveData<User>{
        init()
        return mMutable
    }

    fun create(user: User) {
        mService.create(user)
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                        onNext = {
                            if (it != null) {
                                mDao.insert(it)
                                mPreferences.userId = it.id
                                mUserId = it.id
                                init()
                            }

                            Log.i(this@UserRepository::class.java.simpleName, "onNext: User ${it?.id} created")
                        },
                        onError = {
                            Log.e(this@UserRepository::class.java.simpleName, "onError", it)
                        },
                        onComplete = {
                            Log.i(this@UserRepository::class.java.simpleName, "onCompleted")
                        }
                )
    }
}