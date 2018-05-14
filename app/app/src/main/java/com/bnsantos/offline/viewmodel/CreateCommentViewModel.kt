package com.bnsantos.offline.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.bnsantos.offline.Preferences
import com.bnsantos.offline.models.Comment
import com.bnsantos.offline.repository.CommentsRepository
import com.bnsantos.offline.vo.Resource
import javax.inject.Inject

class CreateCommentViewModel @Inject constructor(private val mPrefs: Preferences, private val mRepo: CommentsRepository): ViewModel() {

    fun create(text: String): LiveData<Resource<Comment>> = mRepo.create(text, mPrefs.userId)
}