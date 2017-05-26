package com.bnsantos.offline.di

import android.app.Application
import android.arch.persistence.room.Room
import com.bnsantos.offline.db.AppDB
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
public class DBModule {
    @Singleton @Provides fun appDB(app: Application): AppDB {
        return Room.databaseBuilder(app, AppDB::class.java, "offline.db").build()
    }
}