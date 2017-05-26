package com.bnsantos.offline.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class User(
        @PrimaryKey val id: String,
        val name: String,
        val email: String
)

