package com.bnsantos.offline.repository

import android.arch.lifecycle.LiveData
import android.content.Context
import android.util.Log
import com.bnsantos.offline.db.UserDao
import com.bnsantos.offline.models.User
import com.bnsantos.offline.network.UserService
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton class UserRepository @Inject constructor(
        private var mUserId: String,
        private val mDao: UserDao,
        private val mService: UserService,
        private val mContext: Context){
    private lateinit var mData: LiveData<User>

    private fun init(){
        mData = mDao.read(mUserId)
    }

    fun read(): LiveData<User>{
        init()
        return mData
    }

    fun create(user: User) {
        mService.create(user)
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                        onNext = {
                            if (it != null) {
                                mDao.insert(it)
                                val editor = mContext.getSharedPreferences("shared_prefs", Context.MODE_PRIVATE).edit()
                                editor.putString("USER_ID", it.id)
                                editor.apply()
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