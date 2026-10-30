package com.example.myimage.data.remote

import com.example.myimage.data.remote.dto.UnsplashDto
import com.example.myimage.data.remote.dto.UnsplashSearchResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UnsplashApi {

    @GET("/photos")
    suspend fun getEditorialFeedImages(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 10
    ): List<UnsplashDto>

    @GET("/photos/{id}")
    suspend fun getFullImageById(
        @Path("id") id: String
    ): UnsplashDto

    @GET("/search/photos")
    suspend fun searchImages(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 10
    ): UnsplashSearchResult

}