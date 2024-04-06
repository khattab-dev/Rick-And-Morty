package com.slayer.data.dataSources.api.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.slayer.data.dataSources.api.ApiService
import com.slayer.data.dataSources.api.dto.locations.LocationResult

class LocationsPagingSource(
    private val service: ApiService,
    private val searchValue: String?,
) : PagingSource<Int, LocationResult>() {
    override fun getRefreshKey(state: PagingState<Int, LocationResult>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LocationResult> {
        val page = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = service.getLocations(page, searchValue)

            LoadResult.Page(
                data = response.locationResults,
                prevKey = if (page == STARTING_PAGE_INDEX) null else page.minus(1),
                nextKey = if (response.locationResults.isEmpty()) null else page.plus(1)
            )

        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }
}