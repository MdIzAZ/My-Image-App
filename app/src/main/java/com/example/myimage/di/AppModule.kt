package com.example.myimage.di

import android.content.Context
import androidx.room.Room
import com.example.myimage.data.local.MyImageAppDataBase
import com.example.myimage.data.remote.UnsplashApi
import com.example.myimage.data.repo.DownloaderImp
import com.example.myimage.data.repo.ImageRepoImplementation
import com.example.myimage.data.repo.NetworkConnectivityObserverImp
import com.example.myimage.domain.repo.Downloader
import com.example.myimage.domain.repo.ImageRepository
import com.example.myimage.domain.repo.NetworkConnectivityObserver
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideUnsplashApiService(): UnsplashApi {
        val contentType = "application/json".toMediaType()
        val json = Json { ignoreUnknownKeys = true }

        val retrofit = Retrofit.Builder()
            .addConverterFactory(json.asConverterFactory(contentType))
            .baseUrl("https://api.unsplash.com/")
            .build()

        return retrofit.create(UnsplashApi::class.java)
    }


    @Provides
    @Singleton
    fun provideDownloader(@ApplicationContext context: Context): Downloader {
        return DownloaderImp(context)
    }


    @Provides
    @Singleton
    fun provideImageRepository(
        apiService: UnsplashApi,
        dataBase: MyImageAppDataBase
    ): ImageRepository {
        return ImageRepoImplementation(apiService, dataBase)
    }

    @Provides
    @Singleton
    fun provideScope() = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    @Provides
    @Singleton
    fun provideConnectivityObserver(
        @ApplicationContext context: Context,
        scope: CoroutineScope,
    ): NetworkConnectivityObserver {
        return NetworkConnectivityObserverImp(context, scope)
    }

    @Provides
    @Singleton
    fun provideDataBase(
        @ApplicationContext context: Context
    ): MyImageAppDataBase {
        return Room.databaseBuilder(
            context,
            MyImageAppDataBase::class.java,
            "favourite_image_db"
        ).build()
    }





}