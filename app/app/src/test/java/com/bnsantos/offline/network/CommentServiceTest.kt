package com.bnsantos.offline.network

import com.bnsantos.offline.enqueueResponse
import com.bnsantos.offline.loadFromResource
import com.bnsantos.offline.models.Comment
import com.google.gson.reflect.TypeToken
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsNull.notNullValue
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(JUnit4::class)
class CommentServiceTest {
    @Rule @JvmField val mMockWebService = MockWebServer()
    @Rule @JvmField val mSubscriberRule = RecordingObserver.Rule()
    private var mService : CommentService? = null

    @Before fun createServer() {
        mService = Retrofit.Builder()
                .baseUrl(mMockWebService.url("/"))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(CommentService::class.java)
    }

    @After fun shutDownServer() {
        mMockWebService.shutdown()
    }

    @Test
    fun readAll(){
        enqueueResponse(mMockWebService, "read-all-comments.json", this@CommentServiceTest.javaClass.classLoader)

        val subscriber = mSubscriberRule.create<List<Comment>>()
        mService?.read()?.subscribe(subscriber)
        val data = loadFromResource<List<Comment>>("read-all-comments.json", this@CommentServiceTest.javaClass.classLoader, object : TypeToken<List<Comment>>() { }.type)
        subscriber.assertValue(data).assertComplete()
    }

    @Test
    fun create(){
        enqueueResponse(mMockWebService, "create-comment.json", this@CommentServiceTest.javaClass.classLoader)

        val subscriber = mSubscriberRule.create<Comment>()
        val comment = Comment(
                id = "user-1-comment-1",
                text = "comment created"
        )
        mService?.create("user-1", comment)?.subscribe(subscriber)
        val value = subscriber.takeValue()
        assertThat(value, notNullValue())
        assertThat(value.id, `is`(comment.id))
        assertThat(value.text, `is`(comment.text))
        assertThat(value.user?.id, `is`("user-1"))
        subscriber.assertComplete()
    }

    @Test
    fun createInvalidUser(){
        mMockWebService.enqueue(MockResponse().setResponseCode(401).setBody("{\"status\": 401, \"msg\": \"unauthorized\"}"))
        val subscriber = mSubscriberRule.create<Comment>()
        val comment = Comment(
                id = "user-1-comment-1",
                text = "invalid comment"
        )
        mService?.create("user-invalid", comment)?.subscribe(subscriber)
        subscriber.assertError(HttpException::class.java, "HTTP 401 Client Error")
    }
}