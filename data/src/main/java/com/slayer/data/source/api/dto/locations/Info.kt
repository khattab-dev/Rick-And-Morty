package com.slayer.data.source.api.dto.locations


import com.squareup.moshi.Json

data class Info(
    @Json(name = "count")
    val count: Int?,
    @Json(name = "next")
    val next: String?,
    @Json(name = "pages")
    val pages: Int?,
    @Json(name = "prev")
    val prev: String?
)