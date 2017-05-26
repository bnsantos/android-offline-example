package com.bnsantos.offline.network

import com.bnsantos.offline.models.Comment
import io.reactivex.Flowable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface CommentService {
    @GET("/comments/")
    fun read(): Flowable<List<Comment>>

    @POST("/comments/")
    fun create(@Header("userId") userId: String, @Body comment: Comment): Flowable<Comment>
}
