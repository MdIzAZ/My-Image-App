package com.example.myimage.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import coil.network.HttpException
import com.example.myimage.data.local.MyImageAppDataBase
import com.example.myimage.data.local.entity.MyHomeImage
import com.example.myimage.data.local.entity.UnsplashRemoteKey
import com.example.myimage.data.mapper.toMyHomeImage
import com.example.myimage.data.remote.UnsplashApi
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class HomeScreenRemoteMediator(
    private val dataBase: MyImageAppDataBase,
    private val apiService: UnsplashApi,
) : RemoteMediator<Int, MyHomeImage>() {

    private val homeImagesDao = dataBase.homeImagesDao()

    /*     override suspend fun load(
            loadType: LoadType,
            state: PagingState<Int, MyHomeImage>,
        ): MediatorResult {

            try {

                val currentPage = when (loadType) {
                    LoadType.REFRESH -> {
                        val responseKey = getRemoteKeyClosestToCurrentPosition(state)
                        responseKey?.nextPage?.minus(1) ?: 1
                    }

                    LoadType.PREPEND -> {
                        val responseKey = getRemoteKeyForFirstItem(state)
                        val prevPage = responseKey?.prevPage
                            ?: return MediatorResult.Success(endOfPaginationReached = responseKey != null)
                        prevPage
                    }

                    LoadType.APPEND -> {
                        val responseKey = getRemoteKeyForLastItem(state)
                        val nextPage = responseKey?.nextPage
                            ?: return MediatorResult.Success(endOfPaginationReached = responseKey != null)
                        nextPage
                    }
                }

                val response = apiService.getEditorialFeedImages(page = currentPage)
                val endOfPaginationReached = response.isEmpty()
                val prevPage = if (currentPage == 1) null else currentPage - 1
                val nextPage = if (endOfPaginationReached) null else currentPage + 1

                val remoteKeys = response.map { unsplashDto ->
                    UnsplashRemoteKey(
                        id = unsplashDto.id,
                        prevPage = prevPage,
                        nextPage = nextPage
                    )
                }

                val homeImages = response.map {
                    it.toMyHomeImage()
                }

                dataBase.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        homeImagesDao.deleteAllHomeImages()
                        homeImagesDao.deleteAllRemoteKeys()
                    }
                    homeImagesDao.insertHomeImages(homeImages)
                    homeImagesDao.insertRemoteKeys(remoteKeys)
                }

                return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

            } catch (e: Exception) {
                e.printStackTrace()
                return MediatorResult.Error(e)
            }


        }
    */


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MyHomeImage>,
    ): MediatorResult {

        return try {

            val loadKey = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    val lastIem = state.lastItemOrNull()
                    if (lastIem == null) {
                        1
                    } else {
                        (lastIem.idCount / state.config.pageSize) + 1
                    }
                }
            }

            val response = apiService.getEditorialFeedImages(page = loadKey)
            val endOfPaginationReached = response.isEmpty()

            val homeImages = response.map { it.toMyHomeImage() }

            dataBase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    homeImagesDao.deleteAllHomeImages()
                }
                homeImagesDao.insertHomeImages(homeImages)
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    /*private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, MyHomeImage>,
    ): UnsplashRemoteKey? {

        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                homeImagesDao.getRemoteKey(id)
            }
        }

    }*/

    /*private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, MyHomeImage>,
    ): UnsplashRemoteKey? {

        return state.pages
            .firstOrNull {
                it.data.isNotEmpty()
            }
            ?.data
            ?.firstOrNull()
            ?.let { myImage ->
                homeImagesDao.getRemoteKey(id = myImage.id)
            }

    }*/

    /*private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, MyHomeImage>,
    ): UnsplashRemoteKey? {

        return state.pages
            .lastOrNull {
                it.data.isNotEmpty()
            }
            ?.data
            ?.lastOrNull()
            ?.let { myImage ->
                homeImagesDao.getRemoteKey(id = myImage.id)
            }

    }*/


}
