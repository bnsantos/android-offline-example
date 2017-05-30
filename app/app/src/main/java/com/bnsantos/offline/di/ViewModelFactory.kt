package com.bnsantos.offline.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.support.v4.util.ArrayMap

import com.bnsantos.offline.di.component.ViewModelsComponent
import com.bnsantos.offline.viewmodel.CommentsViewModel
import java.util.concurrent.Callable

class ViewModelFactory (viewModelsComponent: ViewModelsComponent) : ViewModelProvider.Factory {

    private val creators = ArrayMap<Class<*>, Callable<out ViewModel>>()

    init {
        creators.put(CommentsViewModel::class.java, Callable { viewModelsComponent.commentViewModel() })
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>?): T {
        var creator: Callable<out ViewModel>? = creators[modelClass]
        if (creator == null) {
            for ((key, value) in creators) {
                if (modelClass != null && modelClass.isAssignableFrom(key)) {
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
