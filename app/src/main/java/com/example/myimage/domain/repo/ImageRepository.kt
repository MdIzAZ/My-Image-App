package com.example.myimage.domain.repo

import androidx.paging.PagingData
import com.example.myimage.data.local.entity.MyHomeImage
import com.example.myimage.domain.model.MyImage
import kotlinx.coroutines.flow.Flow

interface ImageRepository {

    fun getEditorialImages(): Flow<PagingData<MyImage>>

    suspend fun getFullImageById(imageId: String): MyImage

    fun searchImages(
        query: String,
        page: Int = 1,
        perPage: Int = 10,
    ): Flow<PagingData<MyImage>>

    suspend fun toggleFavourite(image: MyImage) : Boolean

    fun getAllFavouriteImageIds(): Flow<List<String>>

    fun getAllFavouriteImages(): Flow<PagingData<MyImage>>



}