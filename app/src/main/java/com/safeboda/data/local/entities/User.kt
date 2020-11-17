package com.safeboda.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var userId: String
)