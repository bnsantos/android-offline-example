package com.bnsantos.offline.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.bnsantos.offline.models.Comment
import com.bnsantos.offline.models.User

@Database(entities = arrayOf(User::class, Comment::class), version = 0)
@TypeConverters(Converters::class)
public abstract class AppDB : RoomDatabase() {
    abstract public fun userDao(): UserDao

    abstract public fun commentDao(): CommentDao
}

