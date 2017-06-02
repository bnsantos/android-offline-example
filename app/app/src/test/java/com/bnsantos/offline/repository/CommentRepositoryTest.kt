package com.bnsantos.offline.repository

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import com.bnsantos.offline.db.CommentDao
import com.bnsantos.offline.loadFromResource
import com.bnsantos.offline.models.Comment
import com.bnsantos.offline.network.CommentService
import com.google.gson.reflect.TypeToken
import io.reactivex.Observable
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsNull
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Matchers
import org.mockito.Mockito.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(JUnit4::class)
class CommentRepositoryTest {
    private lateinit var mRepo: CommentsRepository
    private lateinit var mDao: CommentDao
    private lateinit var mService: CommentService

    @Rule @JvmField val instantExecutorRule = InstantTaskExecutorRule()

    @Before fun init(){
        mDao = mock(CommentDao::class.java)
        mService = mock(CommentService::class.java)
        mRepo = CommentsRepository(mDao, mService)
    }

    @Test fun testEmpty(){
        val dbData = MutableLiveData<List<Comment>>()
        val comments = ArrayList<Comment>()
        dbData.value = comments
        `when`(mDao.read()).thenReturn(dbData)
        `when`(mService.read()).thenReturn(Observable.just(listOf()))

        val observer = mock(Observer::class.java)
        val liveData = mRepo.read()
        liveData.observeForever(observer as Observer<List<Comment>>?)

        verify<CommentDao>(mDao).read()
        verify<CommentService>(mService).read()
        verifyNoMoreInteractions(mService)
        assertThat(liveData.value, IsNull.notNullValue())
        assertThat(liveData.value?.size, `is`(0))
    }

    @Test fun testDBEmpty(){
        val dbData = MutableLiveData<List<Comment>>()
        val comments = ArrayList<Comment>()
        dbData.value = comments
        `when`(mDao.read()).thenReturn(dbData)
        val data = loadFromResource<List<Comment>>("read-all-comments.json", this@CommentRepositoryTest.javaClass.classLoader, object : TypeToken<List<Comment>>() { }.type)
        `when`(mService.read()).thenReturn(Observable.just(data))

        val observer = mock(Observer::class.java)
        val liveData = mRepo.read()
        liveData.observeForever(observer as Observer<List<Comment>>?)

        verify<CommentDao>(mDao).read()
        verify<CommentService>(mService).read()
        verify<CommentDao>(mDao).insert(Matchers.anyListOf(Comment::class.java))
        dbData.value = data //Simulating insert
        verifyNoMoreInteractions(mService)

        assertThat(liveData.value, IsNull.notNullValue())
        assertThat(liveData.value?.size, `is`(data.size))
    }

    @Test fun testDBApiMore(){
        val dbData = MutableLiveData<List<Comment>>()
        val comments = ArrayList<Comment>()
        val data = loadFromResource<List<Comment>>("read-all-comments.json", this@CommentRepositoryTest.javaClass.classLoader, object : TypeToken<List<Comment>>() { }.type)
        comments.add(data[0])
        dbData.value = comments
        `when`(mDao.read()).thenReturn(dbData)
        `when`(mService.read()).thenReturn(Observable.just(data))

        val observer = mock(Observer::class.java)
        val liveData = mRepo.read()
        liveData.observeForever(observer as Observer<List<Comment>>?)

        verify<CommentDao>(mDao).read()
        verify<CommentService>(mService).read()
        verify<CommentDao>(mDao).insert(Matchers.anyListOf(Comment::class.java))
        dbData.value = data //Simulating insert
        verifyNoMoreInteractions(mService)

        assertThat(liveData.value, IsNull.notNullValue())
        assertThat(liveData.value?.size, `is`(data.size))
    }

    @Throws(InterruptedException::class)
    fun <T> getValue(liveData: LiveData<T>): T {
        val data = arrayOfNulls<Any>(1)
        val latch = CountDownLatch(1)
        val observer = object : Observer<T> {
            override fun onChanged(o: T?) {
                data[0] = o
                latch.countDown()
                liveData.removeObserver(this)
            }
        }
        liveData.observeForever(observer)
        latch.await(2, TimeUnit.SECONDS)

        return data[0] as T
    }
}