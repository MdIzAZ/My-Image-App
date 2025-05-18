package com.example.myimage.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.myimage.data.local.entity.MyHomeImage
import com.example.myimage.data.local.entity.UnsplashRemoteKey

@Dao
interface HomeImagesDao {

    @Query("SELECT * FROM home_screen")
    fun getAllHomeImages() : PagingSource<Int, MyHomeImage>

    @Upsert
    suspend fun insertHomeImages(images: List<MyHomeImage>)

    @Query("DELETE FROM home_screen")
    suspend fun deleteAllHomeImages()

    @Query("SELECT * FROM unsplash_remote_keys WHERE id = :id")
    suspend fun getRemoteKey(id: String): UnsplashRemoteKey

    @Upsert
    suspend fun insertRemoteKeys(keys: List<UnsplashRemoteKey>)

    @Query("DELETE FROM unsplash_remote_keys")
    suspend fun deleteAllRemoteKeys()

}