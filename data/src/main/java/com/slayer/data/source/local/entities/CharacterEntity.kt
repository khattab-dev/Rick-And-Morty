package com.slayer.data.source.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CharacterEntity(
    @PrimaryKey
    val id : Int,
)
