package com.slayer.domain.repositories.models

data class Character(
    val id: Int,
    val name: String,
    val type: String,
    val state: String,
    val image: String
)
