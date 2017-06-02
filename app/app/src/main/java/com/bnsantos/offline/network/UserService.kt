package com.bnsantos.offline.network

import com.bnsantos.offline.models.User
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService{
    @POST("/users")
    fun create(@Body user: User): Observable<User>
}

