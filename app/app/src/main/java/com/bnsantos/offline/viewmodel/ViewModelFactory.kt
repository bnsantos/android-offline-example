package com.bnsantos.offline.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.support.v4.util.ArrayMap

import com.bnsantos.offline.di.ViewModelSubComponent
import java.util.concurrent.Callable

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ViewModelFactory @Inject
constructor(viewModelSubComponent: ViewModelSubComponent) : ViewModelProvider.Factory {
    private var mCreators: ArrayMap<Class<*>, Callable<out ViewModel>>

    init {
        // we cannot inject view models directly because they won't be bound to the owner's
        // view model scope.
        mCreators = ArrayMap()
        mCreators.put(CommentsViewModel::class.java, Callable<ViewModel> { viewModelSubComponent.commentViewModel() })
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        var creator: Callable<out ViewModel>? = mCreators[modelClass]
        if (creator == null) {
            for ((key, value) in mCreators) {
                if (modelClass.isAssignableFrom(key)) {
                    creator = value
                    break
                }
            }
        }
        if (creator == null) {
            throw IllegalArgumentException("unknown model class " + modelClass)
        }
        try {
            return creator.call() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }
}
