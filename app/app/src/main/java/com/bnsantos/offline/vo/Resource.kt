package com.bnsantos.offline.vo

sealed class Resource<T>{
    data class Loading<T>(val data: T) : Resource<T>()
    data class Success<T>(val data: T) : Resource<T>()
    data class Error<T>(val message: String, val exception: Throwable): Resource<T>()
}