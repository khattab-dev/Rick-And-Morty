package com.slayer.domain.repositories

import kotlinx.coroutines.flow.Flow

interface CharactersRepository {
    fun getCharacters() : Flow<Any>
}