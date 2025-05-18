package com.example.myimage.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UnsplashSearchResult(
    @SerialName("results")
    val images: List<UnsplashDto>,
    val total: Int,
    @SerialName("total_pages")
    val totalPages: Int
)