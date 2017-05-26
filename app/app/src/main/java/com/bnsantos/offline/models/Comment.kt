package com.bnsantos.offline.models

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity
data class Comment(
        @PrimaryKey val id: String,
        val text: String,
        val createdAt: Date,
        val updatedAt: Date,
        @Embedded(prefix = "user_") val user: User
)
