package com.bnsantos.offline.network

import com.bnsantos.offline.enqueueResponse
import com.bnsantos.offline.loadFromResource
import com.bnsantos.offline.models.User
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
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
class UserServiceTest{
    @Rule @JvmField val mMockWebService = MockWebServer()
    @Rule @JvmField val mSubscriberRule = RecordingObserver.Rule()
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

    @Test fun create(){
        enqueueResponse(mMockWebService, "create-user.json", this@UserServiceTest.javaClass.classLoader)

        val subscriber = mSubscriberRule.create<User>()
        val data = loadFromResource<User>("create-user.json", this@UserServiceTest.javaClass.classLoader, User::class.java)

        mService?.create(user = data)?.subscribe(subscriber)
        subscriber.assertValue(data).assertComplete()
    }

    @Test fun read(){
        enqueueResponse(mMockWebService, "create-user.json", this@UserServiceTest.javaClass.classLoader)

        val subscriber = mSubscriberRule.create<User>()
        val data = loadFromResource<User>("create-user.json", this@UserServiceTest.javaClass.classLoader, User::class.java)

        mService?.read(data.id)?.subscribe(subscriber)
        subscriber.assertValue(data).assertComplete()
    }

    @Test fun readDoesNotExist(){
        mMockWebService.enqueue(MockResponse().setResponseCode(404))

        val subscriber = mSubscriberRule.create<User>()

        mService?.read("!exists")?.subscribe(subscriber)
        subscriber.assertError(HttpException::class.java, "HTTP 404 Client Error")
    }
}