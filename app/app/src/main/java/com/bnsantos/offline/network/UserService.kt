package com.bnsantos.offline.network

import com.bnsantos.offline.models.User
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserService{
    @POST("/users")
    fun create(@Body user: User): Observable<User>

    @GET("/users/{p0}")
    fun read(@Path("p0") userId: String): Observable<User>
}

