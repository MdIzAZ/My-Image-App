package com.example.myimage.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.myimage.data.local.MyImageAppDataBase
import com.example.myimage.data.local.entity.MyHomeImage
import com.example.myimage.data.mapper.toMyHomeImage
import com.example.myimage.data.remote.UnsplashApi
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class HomeScreenRemoteMediator(
    private val dataBase: MyImageAppDataBase,
    private val apiService: UnsplashApi,
) : RemoteMediator<Int, MyHomeImage>() {

    private val homeImagesDao = dataBase.homeImagesDao()

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
                    val loadedCount = state.pages.sumOf { it.data.size }
                    if (loadedCount == 0) {
                        1
                    } else {
                        (loadedCount / state.config.pageSize) + 1
                    }
                }
            }

            val response = apiService.getEditorialFeedImages(page = loadKey, perPage = state.config.pageSize)
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
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}
