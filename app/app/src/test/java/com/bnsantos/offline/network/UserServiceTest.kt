package com.bnsantos.offline.network

import com.bnsantos.offline.models.User
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.Okio
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

@RunWith(JUnit4::class)
class UserServiceTest{
    @Rule @JvmField val mMockWebService = MockWebServer()
    @Rule @JvmField val mSubscriberRule = RecordingSubscriber.Rule()
    private var mService: UserService? = null

    @Before fun createService(){
        mService = Retrofit.Builder()
                .baseUrl(mMockWebService.url("/"))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(UserService::class.java)
    }

    @After fun shutDownServer(){
        mMockWebService.shutdown()
    }

    @Test
    fun create(){
        enqueueResponse("create-user.json")

        val subscriber = mSubscriberRule.create<User>()

        val resourceAsStream = resourceAsInputStream("create-user.json")
        val targetReader = InputStreamReader(resourceAsStream)
        val reader = JsonReader(targetReader)
        val data = Gson().fromJson<User>(reader, User::class.java)
        targetReader.close()

        mService?.create(user = data)?.subscribe(subscriber)
        subscriber.assertValue(data).assertComplete()
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

    private fun resourceAsInputStream(fileName: String) = this@UserServiceTest.javaClass.classLoader.getResourceAsStream("api-response/" + fileName)
}