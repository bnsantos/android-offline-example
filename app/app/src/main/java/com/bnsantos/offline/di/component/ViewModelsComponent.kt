package com.bnsantos.offline.di.component

import com.bnsantos.offline.viewmodel.CommentsViewModel
import com.bnsantos.offline.viewmodel.CreateCommentViewModel
import com.bnsantos.offline.viewmodel.UserViewModel
import dagger.Subcomponent

@Subcomponent interface ViewModelsComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): ViewModelsComponent
    }
    fun commentViewModel(): CommentsViewModel
    fun userViewModel(): UserViewModel
    fun createCommentViewModel(): CreateCommentViewModel
}