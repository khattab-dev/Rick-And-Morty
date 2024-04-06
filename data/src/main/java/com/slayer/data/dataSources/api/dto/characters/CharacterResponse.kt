package com.slayer.data.dataSources.api.dto.characters


import com.squareup.moshi.Json

data class CharacterResponse(
    @Json(name = "info")
    val info: Info?,
    @Json(name = "results")
    val characterResults: List<CharacterResult>
)