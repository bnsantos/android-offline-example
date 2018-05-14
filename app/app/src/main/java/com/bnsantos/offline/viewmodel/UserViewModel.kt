package com.bnsantos.offline.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.bnsantos.offline.models.User
import com.bnsantos.offline.repository.UserRepository
import com.bnsantos.offline.vo.Resource
import javax.inject.Inject

class UserViewModel @Inject constructor(val mRepo: UserRepository): ViewModel(){
    fun read(): LiveData<Resource<User>> {
        return mRepo.read()
    }
    fun save(id: String, name: String, email: String): LiveData<Resource<User>> {
        return mRepo.create(User(id,  name, email))
    }

    override fun onCleared() {
        super.onCleared()
    }
}