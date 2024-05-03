package com.slayer.rickandmorty.ui.fragments.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.slayer.common.generateRandomIds
import com.slayer.common.printToLog
import com.slayer.data.dataSources.api.ApiService
import com.slayer.data.dataSources.api.dto.characters.CharacterResult.Companion.toCharacter
import com.slayer.data.dataSources.api.paging.CharacterPagingSource
import com.slayer.domain.models.Character
import com.slayer.domain.models.NetworkResult
import com.slayer.domain.usecases.characters.CharacterExistenceInFavoriteUseCase
import com.slayer.domain.usecases.characters.DeleteCharacterFromFavUseCase
import com.slayer.domain.usecases.characters.GetRandomCharacters
import com.slayer.domain.usecases.characters.InsertCharacterToFavUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val apiService: ApiService,
    private val insertCharacterToFavUseCase: InsertCharacterToFavUseCase,
    private val deleteCharacterFromFavUseCase: DeleteCharacterFromFavUseCase,
    private val characterExistenceInFavoriteUseCase: CharacterExistenceInFavoriteUseCase,
    private val getRandomCharacters: GetRandomCharacters
) : ViewModel() {

    private val _randomCharactersResult: MutableStateFlow<List<Character>?> = MutableStateFlow(null)
    val randomCharactersResult get() = _randomCharactersResult.asStateFlow()

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

    private suspend fun doesCharacterExistInFavorite(id: Int) =
        characterExistenceInFavoriteUseCase(id)

    fun getRandomCharacters() = viewModelScope.launch(Dispatchers.IO) {
        when (val result = getRandomCharacters.invoke(generateRandomIds())) {
            is NetworkResult.Error -> {
                result.errorMsg.printToLog()
            }

            is NetworkResult.Exception -> {
                result.e.stackTraceToString().printToLog()
            }

            is NetworkResult.Success -> {
                _randomCharactersResult.value = result.data as List<Character>
            }
        }
    }
}