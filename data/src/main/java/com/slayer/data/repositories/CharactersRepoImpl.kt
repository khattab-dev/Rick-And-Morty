package com.slayer.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.slayer.data.ApiService
import com.slayer.data.dto.CharacterResult.Companion.toCharacter
import com.slayer.data.pagingdatasource.CharacterPagingSource
import com.slayer.domain.repositories.models.Character
import com.slayer.domain.repositories.repositories.CharactersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CharactersRepoImpl @Inject constructor(private val apiService: ApiService) : CharactersRepository {
    override fun getCharacters(): Flow<PagingData<Character>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10
            ),
            pagingSourceFactory = { CharacterPagingSource(apiService,null,null,null) }
        ).flow.map {
            it.map { charResult ->
                charResult.toCharacter()
            }
        }
    }
}