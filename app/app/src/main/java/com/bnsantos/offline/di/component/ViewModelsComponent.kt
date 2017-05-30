package com.bnsantos.offline.di.component

import com.bnsantos.offline.viewmodel.CommentsViewModel
import dagger.Subcomponent

@Subcomponent interface ViewModelsComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): ViewModelsComponent
    }
    fun commentViewModel(): CommentsViewModel
}