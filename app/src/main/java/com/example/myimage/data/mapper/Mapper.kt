package com.example.myimage.data.mapper

import com.example.myimage.data.local.entity.FavouriteImage
import com.example.myimage.data.local.entity.MyHomeImage
import com.example.myimage.data.remote.dto.UnsplashDto
import com.example.myimage.domain.model.MyImage

fun UnsplashDto.toMyImage(): MyImage {
    return MyImage(
        id = this.id,
        imageUrlSmall = this.urls.small,
        imageUrlRegular = this.urls.regular,
        imageUrlRaw = this.urls.raw,
        photographerName = this.user.name,
        photographerUserName = this.user.username,
        photographerImageUrl = this.user.profileImage.small,
        photographerProfileLink = this.user.links.html,
        width = this.width,
        height = this.height,
        description = this.description
    )
}


fun MyImage.toFavouriteImage(): FavouriteImage {
    return FavouriteImage(
        id = this.id,
        imageUrlSmall = this.imageUrlSmall,
        imageUrlRegular = this.imageUrlRegular,
        imageUrlRaw = this.imageUrlRaw,
        photographerName = this.photographerName,
        photographerUserName = this.photographerUserName,
        photographerImageUrl = this.photographerImageUrl,
        photographerProfileLink = this.photographerProfileLink,
        width = this.width,
        height = this.height,
        description = this.description
    )
}

fun FavouriteImage.toMyImage(): MyImage {
    return MyImage(
        id = this.id,
        imageUrlSmall = this.imageUrlSmall,
        imageUrlRegular = this.imageUrlRegular,
        imageUrlRaw = this.imageUrlRaw,
        photographerName = this.photographerName,
        photographerUserName = this.photographerUserName,
        photographerImageUrl = this.photographerImageUrl,
        photographerProfileLink = this.photographerProfileLink,
        width = this.width,
        height = this.height,
        description = this.description
    )
}

fun UnsplashDto.toMyHomeImage(): MyHomeImage {
    return MyHomeImage(
        id = this.id,
        imageUrlSmall = this.urls.small,
        imageUrlRegular = this.urls.regular,
        imageUrlRaw = this.urls.raw,
        photographerName = this.user.name,
        photographerUserName = this.user.username,
        photographerImageUrl = this.user.profileImage.small,
        photographerProfileLink = this.user.links.html,
        width = this.width,
        height = this.height,
        description = this.description
    )
}


fun MyHomeImage.toMyImage() : MyImage {
    return MyImage(
        id = this.id,
        imageUrlSmall = this.imageUrlSmall,
        imageUrlRegular = this.imageUrlRegular,
        imageUrlRaw = this.imageUrlRaw,
        photographerName = this.photographerName,
        photographerUserName = this.photographerUserName,
        photographerImageUrl = this.photographerImageUrl,
        photographerProfileLink = this.photographerProfileLink,
        width = this.width,
        height = this.height,
        description = this.description
    )
}