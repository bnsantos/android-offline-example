package com.bnsantos.offline.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.bnsantos.offline.models.Comment

@Dao public interface CommentDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(vararg comment: Comment)

    @Query("SELECT * FROM Comment ORDER BY createdAt ASC")
    abstract fun read(): LiveData<List<Comment>>
}
