package com.slayer.data.source.api.dto.characters


import com.squareup.moshi.Json

data class CharacterResponse(
    @Json(name = "info")
    val info: Info?,
    @Json(name = "results")
    val characterResults: List<CharacterResult>
)