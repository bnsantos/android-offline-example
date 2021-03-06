package com.bnsantos.offline.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.bnsantos.offline.models.User
import io.reactivex.Flowable

@Dao interface UserDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    @Query("SELECT * FROM User WHERE id = :p0")
    fun read(id: String): Flowable<User>
}
