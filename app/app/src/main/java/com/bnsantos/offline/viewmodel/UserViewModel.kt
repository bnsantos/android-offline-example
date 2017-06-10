package com.bnsantos.offline.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.bnsantos.offline.models.User
import com.bnsantos.offline.repository.UserRepository
import com.bnsantos.offline.vo.Resource
import javax.inject.Inject

class UserViewModel @Inject constructor(val mRepo: UserRepository): ViewModel(){
    private val mStream: MutableLiveData<Resource<User>> by lazy { MutableLiveData<Resource<User>>() }

    fun read(): LiveData<Resource<User>> {
        mRepo.read()
        return mStream
    }
    fun save(id: String, name: String, email: String): LiveData<Resource<User>> {
        mRepo.create(User(id,  name, email))
        return mStream
    }

    override fun onCleared() {
        super.onCleared()
    }
}