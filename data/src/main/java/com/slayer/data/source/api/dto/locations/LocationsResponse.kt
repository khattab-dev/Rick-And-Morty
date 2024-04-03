package com.slayer.data.source.api.dto.locations


import com.squareup.moshi.Json

data class LocationsResponse(
    @Json(name = "info")
    val info: Info?,
    @Json(name = "results")
    val locationResults: List<LocationResult>
)