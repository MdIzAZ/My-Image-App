package com.example.myimage.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "unsplash_remote_keys")
data class UnsplashRemoteKey(
    @PrimaryKey
    val id : String,
    val prevPage: Int?,
    val nextPage: Int?
)
