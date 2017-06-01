package com.bnsantos.offline.network

import com.bnsantos.offline.models.Comment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.Okio
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
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

@RunWith(JUnit4::class)
class CommentServiceTest {
    @Rule @JvmField val mMockWebService = MockWebServer()
    @Rule @JvmField val mSubscriberRule = RecordingSubscriber.Rule()
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
        enqueueResponse("read-all-comments.json")

        val subscriber = mSubscriberRule.create<List<Comment>>()
        mService?.read()?.subscribe(subscriber)

        val resourceAsStream = resourceAsInputStream("read-all-comments.json")
        val targetReader = InputStreamReader(resourceAsStream)
        val reader = JsonReader(targetReader)
        val data = Gson().fromJson<List<Comment>>(reader, object : TypeToken<List<Comment>>() { }.type)
        targetReader.close()

        subscriber.assertValue(data).assertComplete()
    }

    @Test
    fun create(){
        enqueueResponse("create-comment.json")

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

    private fun enqueueResponse(fileName: String) {
        enqueueResponse(resourceAsInputStream(fileName), emptyMap<String, String>())
    }

    private fun enqueueResponse(inputStream: InputStream, headers: Map<String, String>){
        val source = Okio.buffer(Okio.source(inputStream))
        val response = MockResponse()
        for (key in headers.keys) {
            response.addHeader(key, headers[key])
        }
        mMockWebService.enqueue(response.setBody(source.readString(StandardCharsets.UTF_8)))
    }

    private fun resourceAsInputStream(fileName: String) = this@CommentServiceTest.javaClass.classLoader.getResourceAsStream("api-response/" + fileName)
}