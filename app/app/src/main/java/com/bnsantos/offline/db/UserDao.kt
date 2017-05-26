package com.bnsantos.offline.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import com.bnsantos.offline.models.User

@Dao public interface UserDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)
}
