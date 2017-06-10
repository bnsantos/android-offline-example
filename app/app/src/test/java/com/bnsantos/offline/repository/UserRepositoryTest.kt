package com.bnsantos.offline.repository

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.bnsantos.offline.Preferences
import com.bnsantos.offline.db.UserDao
import com.bnsantos.offline.models.User
import com.bnsantos.offline.network.UserService
import com.bnsantos.offline.vo.Resource
import io.reactivex.Flowable
import io.reactivex.Observable
import org.hamcrest.core.IsNull
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito

@RunWith(JUnit4::class)
class UserRepositoryTest {
    private lateinit var mRepo: UserRepository
    private lateinit var mDao: UserDao
    private lateinit var mService: UserService
    private lateinit var mPrefs: Preferences

    @Rule @JvmField val instantExecutorRule = InstantTaskExecutorRule()

    @Before fun init(){
        mDao = Mockito.mock(UserDao::class.java)
        mService = Mockito.mock(UserService::class.java)
        mPrefs = Mockito.mock(Preferences::class.java)
        Mockito.`when`(mPrefs.userId).thenReturn("u1")
        mRepo = UserRepository(mPrefs, mDao, mService)
    }

    @Test fun testCache(){
        val cached = User("u1", "John Doe", "john.doe@email.com")
        val server = User("u1", "John Doe", "john.doe.new.email@email.com")
        Mockito.`when`(mDao.read("u1")).thenReturn(Flowable.just(cached))
        Mockito.`when`(mService.read("u1")).thenReturn(Observable.just(server))

        val observer = Mockito.mock(Observer::class.java)
        val liveData = mRepo.read()
        liveData.observeForever(observer as Observer<Resource<User>>?)

        Mockito.verify<UserDao>(mDao).read("u1")
        Mockito.verify<UserService>(mService).read("u1")
        Mockito.verifyNoMoreInteractions(mService)
        assertThat(liveData.value, IsNull.notNullValue())

        assertEquals(liveData.value, Resource.Loading(cached))
        Thread.sleep(500) //Wait little bit to value change to server response. Figured out a better way to do this
        assertEquals(liveData.value, Resource.Success(server))
    }

    @Test fun testEmptyCache(){
        val user = User("u1", "John Doe", "john.doe@email.com")
        Mockito.`when`(mDao.read("u1")).thenReturn(Flowable.empty())
        Mockito.`when`(mService.read("u1")).thenReturn(Observable.just(user))

        val observer = Mockito.mock(Observer::class.java)
        val liveData = mRepo.read()
        liveData.observeForever(observer as Observer<Resource<User>>?)
        assertThat(liveData.value, IsNull.nullValue())

        Mockito.verify<UserDao>(mDao).read("u1")
        Mockito.verify<UserService>(mService).read("u1")
        Mockito.verifyNoMoreInteractions(mService)
        Thread.sleep(200) //Wait little bit to value change to server response. Figured out a better way to do this
        assertThat(liveData.value, IsNull.notNullValue())
        assertEquals(liveData.value, Resource.Success(user))
    }

    @Test fun testCreate(){
        val user = User("u1", "John Doe", "john.doe@email.com")
        Mockito.`when`(mService.create(user)).thenReturn(Observable.just(user))

        val observer = Mockito.mock(Observer::class.java)
        val liveData = mRepo.create(user)
        liveData.observeForever(observer as Observer<Resource<User>>?)

        Mockito.verify<UserService>(mService).create(user)
        Mockito.verifyNoMoreInteractions(mService)

        assertThat(liveData.value, IsNull.notNullValue())
        assertEquals(liveData.value, Resource.Success(user))
    }

}