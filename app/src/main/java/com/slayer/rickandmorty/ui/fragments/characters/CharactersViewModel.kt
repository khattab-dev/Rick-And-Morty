package com.slayer.rickandmorty.ui.fragments.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.slayer.data.ApiService
import com.slayer.data.dto.CharacterResult.Companion.toCharacter
import com.slayer.data.pagingdatasource.CharacterPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val apiService: ApiService,
) : ViewModel() {
    private var currentSearchValue: String? = null
    private var currentStatus: String? = null
    private var currentGender: String? = null

    private var pagingSource: CharacterPagingSource? = null
        get() {
            if (field == null || field?.invalid == true) {
                field = CharacterPagingSource(
                    apiService,
                    currentSearchValue,
                    currentStatus,
                    currentGender
                )
            }
            return field
        }

    val characterFlow = Pager(
        config = PagingConfig(
            pageSize = 10
        ),
        pagingSourceFactory = { pagingSource!! }
    ).flow.cachedIn(viewModelScope).map {
        it.map { charactersResult ->
            charactersResult.toCharacter()
        }
    }

    fun submitQuery(
        searchValue: String?,
        status: String?,
        gender: String?
    ) {
        currentSearchValue = searchValue
        currentStatus = status
        currentGender = gender

        pagingSource?.invalidate()
    }

    fun getCurrentSearchValue() = currentSearchValue

    fun getCurrentStatus() = currentStatus

    fun getCurrentGender() = currentGender

    fun setCurrentStatus(status: String?) {
        currentStatus = status
    }

    fun setCurrentGender(gender: String?) {
        currentGender = gender
    }
}