package com.slayer.data.dto


import com.slayer.domain.models.Character
import com.squareup.moshi.Json

data class CharacterResponse(
    @Json(name = "info")
    val info: Info?,
    @Json(name = "results")
    val characterResults: List<CharacterResult>
)