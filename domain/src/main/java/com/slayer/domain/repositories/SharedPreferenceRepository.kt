package com.slayer.domain.repositories

interface SharedPreferenceRepository {
    fun getDarkModeValue() : Boolean
    fun setDarkModeValue(boolean: Boolean)
}