package com.example.myimage.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.myimage.data.local.entity.FavouriteImage
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteDao {

    @Query("SELECT * FROM favourites")
    fun getAllFavouriteImages(): PagingSource<Int, FavouriteImage>

    @Query("SELECT id FROM favourites")
    fun getAllFavImagesIds(): Flow<List<String>>

    @Upsert
    suspend fun insertFavouriteImage(favouriteImage: FavouriteImage)

    @Delete
    suspend fun deleteFavouriteImage(favouriteImage: FavouriteImage)

    @Query("SELECT EXISTS(SELECT 1 FROM favourites WHERE id = :imageId)")
    suspend fun isFavourite(imageId: String): Boolean


}