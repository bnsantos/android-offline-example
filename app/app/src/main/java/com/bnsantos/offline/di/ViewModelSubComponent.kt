package com.bnsantos.offline.di

import com.bnsantos.offline.viewmodel.CommentsViewModel
import dagger.Subcomponent

/**
 * A sub component to create ViewModels. It is called by the
 * {@link com.android.example.github.viewmodel.GithubViewModelFactory}. Using this component allows
 * ViewModels to define {@link javax.inject.Inject} constructors.
 */
@Subcomponent interface ViewModelSubComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): ViewModelSubComponent
    }
    fun commentViewModel(): CommentsViewModel
}