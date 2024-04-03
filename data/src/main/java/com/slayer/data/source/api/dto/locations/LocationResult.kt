package com.slayer.data.source.api.dto.locations


import com.slayer.domain.models.Location
import com.squareup.moshi.Json

data class LocationResult(
    @Json(name = "created")
    val created: String?,
    @Json(name = "dimension")
    val dimension: String?,
    @Json(name = "id")
    val id: Int?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "residents")
    val residents: List<String?>,
    @Json(name = "type")
    val type: String?,
    @Json(name = "url")
    val url: String?
) {
    companion object {
        fun LocationResult.toLocation(): Location {
            return Location(
                id = id ?: -1,
                name = name ?: "",
                type = type ?: "",
            )
        }
    }
}