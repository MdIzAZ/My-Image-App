package com.example.myimage.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UnsplashDto(
    val id: String,
    val description: String?,
    val height: Int,
    val width: Int,
    val urls: Urls,
    val user: UserDto
)

@Serializable
data class Urls(
    val full: String,
    val raw: String,
    val regular: String,
    val small: String,
    val thumb: String
)

@Serializable
data class UserDto(
    val links: UsersLinkDto,
    val name: String,
    @SerialName("profile_image")
    val profileImage: ProfileImageDto,
    val username: String
)

@Serializable
data class UsersLinkDto(
    val html: String,
    val likes: String,
    val photos: String,
    val portfolio: String,
    val self: String
)

@Serializable
data class ProfileImageDto(
    val small: String
)