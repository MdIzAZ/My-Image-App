package com.example.myimage.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myimage.data.local.entity.FavouriteImage
import com.example.myimage.data.local.entity.MyHomeImage
import com.example.myimage.data.local.entity.UnsplashRemoteKey

@Database(
    entities = [FavouriteImage::class, MyHomeImage::class, UnsplashRemoteKey::class],
    version = 1,
    exportSchema = false
)
abstract class MyImageAppDataBase : RoomDatabase() {

    abstract fun favouriteDao(): FavouriteDao

    abstract fun homeImagesDao(): HomeImagesDao

}