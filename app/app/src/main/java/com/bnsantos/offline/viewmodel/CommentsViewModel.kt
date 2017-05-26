package com.bnsantos.offline.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.bnsantos.offline.models.Comment
import com.bnsantos.offline.repository.CommentsRepository
import javax.inject.Inject

class CommentsViewModel: ViewModel {
    val mRepo: CommentsRepository

    @Inject
    constructor(repo: CommentsRepository) : super() {
        mRepo = repo
    }

    public fun read(): LiveData<List<Comment>> {
        return mRepo.read()
    }
}