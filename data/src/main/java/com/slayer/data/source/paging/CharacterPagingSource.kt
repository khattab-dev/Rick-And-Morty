package com.slayer.data.source.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.slayer.data.ApiService
import com.slayer.data.dto.characters.CharacterResult

class CharacterPagingSource(
    private val service: ApiService,
    private val searchValue: String?,
    private val status: String?,
    private val gender: String?
) : PagingSource<Int, CharacterResult>() {
    override fun getRefreshKey(state: PagingState<Int, CharacterResult>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterResult> {
        val page = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = service.getCharacters(page, searchValue, status, gender)

            LoadResult.Page(
                data = response.characterResults,
                prevKey = if (page == STARTING_PAGE_INDEX) null else page.minus(1),
                nextKey = if (response.characterResults.isEmpty()) null else page.plus(1)
            )

        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }
}