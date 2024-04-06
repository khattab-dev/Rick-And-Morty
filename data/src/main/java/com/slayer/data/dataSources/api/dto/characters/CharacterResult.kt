package com.slayer.data.dataSources.api.dto.characters


import com.slayer.domain.models.Character
import com.squareup.moshi.Json

data class CharacterResult(
    @Json(name = "created")
    val created: String?,
    @Json(name = "episode")
    val episode: List<String?>?,
    @Json(name = "gender")
    val gender: String?,
    @Json(name = "id")
    val id: Int?,
    @Json(name = "image")
    val image: String?,
    @Json(name = "location")
    val location: Location?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "origin")
    val origin: Origin?,
    @Json(name = "species")
    val species: String?,
    @Json(name = "status")
    val status: String?,
    @Json(name = "type")
    val type: String?,
    @Json(name = "url")
    val url: String?
) {
    companion object {
        fun CharacterResult.toCharacter(isFavorite : Boolean): Character {
            return Character(
                id = id ?: -1,
                image = image ?: "",
                name = name ?: "",
                type = gender ?: "",
                state = status ?: "",
                isFavorite = isFavorite
            )
        }
    }
}