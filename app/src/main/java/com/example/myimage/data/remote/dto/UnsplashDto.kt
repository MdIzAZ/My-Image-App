@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)

package com.example.myimage.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UnsplashDto(
    val id: String = "",
    val description: String? = null,
    val height: Int = 0,
    val width: Int = 0,
    val urls: Urls,
    val user: UserDto
)

@Serializable
data class Urls(
    val full: String = "",
    val raw: String = "",
    val regular: String = "",
    val small: String = "",
    val thumb: String = ""
)

@Serializable
data class UserDto(
    val links: UsersLinkDto? = null,
    val name: String? = "Unknown",
    @SerialName("profile_image")
    val profileImage: ProfileImageDto? = null,
    val username: String? = "Unknown"
)

@Serializable
data class UsersLinkDto(
    val html: String? = "",
    val likes: String? = "",
    val photos: String? = "",
    val portfolio: String? = "",
    val self: String? = ""
)

@Serializable
data class ProfileImageDto(
    val small: String? = ""
)