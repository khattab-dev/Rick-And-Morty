package com.slayer.domain.models

data class Character(
    val id: Int,
    val name: String = "",
    val type: String = "",
    val state: String = "",
    val image: String = "",
    var isFavorite: Boolean = false
)
