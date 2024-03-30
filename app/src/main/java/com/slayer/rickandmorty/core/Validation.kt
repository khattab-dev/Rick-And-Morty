package com.slayer.rickandmorty.core

import android.util.Patterns

fun isValidEmailAddress(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun isValidPasswordLength(password: String): Boolean {
    return password.length >= 6
}

fun arePasswordsEqual(
    password: String,
    passwordConfirmation: String
): Boolean {
    return password == passwordConfirmation
}
