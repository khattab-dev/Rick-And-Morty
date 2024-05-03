package com.slayer.common

object Constants {
    const val FAVORITE_SYNC_WORK_NAME = "sync-favorite"
    const val KEY_FAVORITE_CHARS_IDS = "favorite_characters_ids"
    const val COLLECTION_PATH_FAV_CHARS = "favorite_chars"

    // FIREBASE AUTH INVALID USER ERROR CODES
    const val USER_NOT_FOUND = "ERROR_USER_NOT_FOUND"
    const val USER_DISABLED = "ERROR_USER_DISABLED"
    const val USER_TOKEN_EXPIRED = "ERROR_USER_TOKEN_EXPIRED"
    const val INVALID_USER_TOKEN = "ERROR_INVALID_USER_TOKEN"

    // FILTER STATUS
    const val ALIVE_STATUS = "alive"
    const val DEAD_STATUS = "dead"
    const val UNKNOWN_STATUS = "unknown"

    // FILTER GENDER
    const val MALE_GENDER = "male"
    const val FEMALE_GENDER = "female"
    const val GENDERLESS_GENDER = "genderless"
    const val UNKNOWN_GENDER = "unknown"
}