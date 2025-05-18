package com.example.myimage.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.myimage.data.mapper.toMyImage
import com.example.myimage.data.remote.UnsplashApi
import com.example.myimage.domain.model.MyImage

class SearchPagingSource(
    private val query: String,
    private val unsplashApi: UnsplashApi,
) : PagingSource<Int, MyImage>() {

    override fun getRefreshKey(state: PagingState<Int, MyImage>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MyImage> {
        return try {
            val currentPage = params.key ?: 1
            val response = unsplashApi.searchImages(query, currentPage, params.loadSize)
            val endOfPaginationReached = response.images.isEmpty()
            val data = response.images.map {
                it.toMyImage()
            }
            LoadResult.Page(
                data = data,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (endOfPaginationReached) null else currentPage + 1
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }


    }
}