package com.slayer.rickandmorty.ui.fragments.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.slayer.data.ApiService
import com.slayer.data.dto.characters.CharacterResult.Companion.toCharacter
import com.slayer.data.source.paging.CharacterPagingSource
import com.slayer.domain.models.Character
import com.slayer.domain.usecases.CharacterExistenceInFavoriteUseCase
import com.slayer.domain.usecases.DeleteCharacterFromFavUseCase
import com.slayer.domain.usecases.InsertCharacterToFavUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val apiService: ApiService,
    private val insertCharacterToFavUseCase: InsertCharacterToFavUseCase,
    private val deleteCharacterFromFavUseCase: DeleteCharacterFromFavUseCase,
    private val characterExistenceInFavoriteUseCase: CharacterExistenceInFavoriteUseCase
) : ViewModel() {

    private var prevSearchValue: String? = null
    private var prevStatus: String? = null
    private var prevGender: String? = null

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
            charactersResult.toCharacter(doesCharacterExistInFavorite(charactersResult.id ?: 0))
        }
    }

    fun submitQuery(
        searchValue: String?,
        status: String?,
        gender: String?
    ) {
        currentSearchValue = if (searchValue.isNullOrEmpty()) null else searchValue
        currentStatus = status
        currentGender = gender

        if (prevGender != currentGender || prevStatus != currentStatus || prevSearchValue != currentSearchValue) {
            pagingSource?.invalidate()

            prevGender = currentGender
            prevStatus = currentStatus
            prevSearchValue = currentSearchValue
        }
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

    fun insertCharacterToFav(character: Character) = viewModelScope.launch(Dispatchers.IO) {
        insertCharacterToFavUseCase(character)
    }

    fun deleteCharacterToFav(character: Character) = viewModelScope.launch(Dispatchers.IO) {
        deleteCharacterFromFavUseCase(character)
    }

    private suspend fun doesCharacterExistInFavorite(id: Int) = characterExistenceInFavoriteUseCase(id)
}