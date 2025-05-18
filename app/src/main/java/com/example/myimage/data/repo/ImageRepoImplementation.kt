package com.example.myimage.data.repo

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.myimage.data.local.MyImageAppDataBase
import com.example.myimage.data.local.entity.MyHomeImage
import com.example.myimage.data.mapper.toFavouriteImage
import com.example.myimage.data.mapper.toMyImage
import com.example.myimage.data.paging.HomeScreenRemoteMediator
import com.example.myimage.data.paging.SearchPagingSource
import com.example.myimage.data.remote.UnsplashApi
import com.example.myimage.domain.model.MyImage
import com.example.myimage.domain.repo.ImageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class ImageRepoImplementation constructor(
    private val unsplashApi: UnsplashApi,
    private val imageDB: MyImageAppDataBase,
) : ImageRepository {

    private val favImageDao = imageDB.favouriteDao()
    private val homeImageDao = imageDB.homeImagesDao()

    @OptIn(ExperimentalPagingApi::class)
    override fun getEditorialImages(): Flow<PagingData<MyImage>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = HomeScreenRemoteMediator(imageDB, unsplashApi),
            pagingSourceFactory = { homeImageDao.getAllHomeImages() }
        )
            .flow
            .map {
                it.map { myHomeImage->
                    myHomeImage.toMyImage()
                }
            }
    }

    override suspend fun getFullImageById(imageId: String): MyImage {
        return unsplashApi.getFullImageById(imageId).toMyImage()
    }

    override fun searchImages(
        query: String,
        page: Int,
        perPage: Int,
    ): Flow<PagingData<MyImage>> {
        return Pager(
            config = PagingConfig(pageSize = perPage),
            pagingSourceFactory = { SearchPagingSource(query, unsplashApi) }
        ).flow
    }

    override suspend fun toggleFavourite(image: MyImage): Boolean {
        val isFavourite = favImageDao.isFavourite(image.id)
        if (isFavourite) {
            favImageDao.deleteFavouriteImage(image.toFavouriteImage())
            return false
        } else {
            favImageDao.insertFavouriteImage(image.toFavouriteImage())
            return true
        }
    }

    override fun getAllFavouriteImageIds(): Flow<List<String>> {
        return favImageDao.getAllFavImagesIds()
    }

    override fun getAllFavouriteImages(): Flow<PagingData<MyImage>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { favImageDao.getAllFavouriteImages() }
        )
            .flow
            .map {
                it.map { fav -> fav.toMyImage() }
            }
    }

}