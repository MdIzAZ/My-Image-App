package com.example.myimage.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "home_screen")
data class MyHomeImage(
    @PrimaryKey(autoGenerate = true)
    val idCount: Int = 0,
    val id: String,
    val imageUrlSmall: String,
    val imageUrlRegular: String,
    val imageUrlRaw: String,
    val photographerName: String,
    val photographerUserName: String,
    val photographerImageUrl: String,
    val photographerProfileLink: String,
    val width: Int,
    val height: Int,
    val description: String?
)