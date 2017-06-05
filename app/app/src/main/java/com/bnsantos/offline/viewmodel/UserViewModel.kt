package com.bnsantos.offline.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.bnsantos.offline.models.User
import com.bnsantos.offline.repository.UserRepository
import javax.inject.Inject

class UserViewModel @Inject constructor(val mRepo: UserRepository): ViewModel(){
    fun read(): LiveData<User> = mRepo.read()
    fun save(id: String, name: String, email: String) {
        mRepo.create(User(id,  name, email))
    }
}