package com.bnsantos.offline.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.bnsantos.offline.models.Comment
import com.bnsantos.offline.repository.CommentsRepository
import javax.inject.Inject

class CommentsViewModel @Inject constructor(val mRepo: CommentsRepository): ViewModel() {
    fun read(): LiveData<List<Comment>> {
        return mRepo.read()
    }

    fun reload() {
        mRepo.reload()
    }
}