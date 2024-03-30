package com.slayer.data

import com.slayer.data.dto.CharacterResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int,
        @Query("count") count: Int,
        @Query("name") search : String?,
        @Query("status") status : String?,
        @Query("gender") gender : String?,
    ) : CharacterResponse
}