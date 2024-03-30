package com.slayer.data

import com.slayer.data.dto.characters.CharacterResponse
import com.slayer.data.dto.locations.LocationsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
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