package com.slayer.data.repositories

import android.content.SharedPreferences
import com.slayer.domain.repositories.SharedPreferenceRepository
import javax.inject.Inject

class SharedPrefRepoImpl @Inject constructor(
    private val preferences: SharedPreferences
) : SharedPreferenceRepository {
    private val isDarkMode ="isDarkMode"

    override fun getDarkModeValue(): Boolean {
       return preferences.getBoolean(isDarkMode,false)
    }

    override fun setDarkModeValue(boolean: Boolean) {
        preferences.edit().putBoolean(isDarkMode,boolean).apply()
    }
}