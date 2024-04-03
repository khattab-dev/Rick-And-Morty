package com.slayer.data.source.api

import com.slayer.data.source.api.dto.characters.CharacterResponse
import com.slayer.data.source.api.dto.characters.CharacterResult
import com.slayer.data.source.api.dto.locations.LocationsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("character/{ids}")
    suspend fun getRandomCharacters(@Path("ids") id: String): Response<List<CharacterResult>>

    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int,
        @Query("name") search : String?,
        @Query("status") status : String?,
        @Query("gender") gender : String?,
    ) : CharacterResponse

    @GET("location")
    suspend fun getLocations(
        @Query("page") page: Int,
        @Query("name") search : String?,
    ) : LocationsResponse
}