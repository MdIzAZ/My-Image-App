package com.example.myimage.domain.model

data class MyImage(
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
    val description: String?,
)
