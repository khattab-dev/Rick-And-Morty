package com.slayer.common

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.slayer.common.common_ui.DefaultDialog
import com.slayer.common.databinding.DialogNoInternetBinding
import kotlin.random.Random

fun Any?.printToLog(tag: String = "DEBUG_LOG") {
    Log.d(tag, toString())
}

fun View.gone() = run { visibility = View.GONE }

fun View.visible() = run { visibility = View.VISIBLE }

fun View.invisible() = run { visibility = View.INVISIBLE }

infix fun View.visibleIf(condition: Boolean) =
    run { visibility = if (condition) View.VISIBLE else View.GONE }

infix fun View.goneIf(condition: Boolean) =
    run { visibility = if (condition) View.GONE else View.VISIBLE }

infix fun View.invisibleIf(condition: Boolean) =
    run { visibility = if (condition) View.INVISIBLE else View.VISIBLE }

fun Fragment.toast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
}

fun Fragment.toast(@StringRes message: Int) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
}

fun Activity.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Activity.toast(@StringRes message: Int) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun safeCall(context: Context, call: () -> Unit) {
    if (isNetworkConnected(context)) {
        call.invoke()
    }
    else {
        showNoInternetDialog(context)
    }
}

@SuppressLint("MissingPermission")
private fun isNetworkConnected(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val networkCapabilities = connectivityManager.activeNetwork ?: return false
    val activeNetwork =
        connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false

    return activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}

private fun showNoInternetDialog(context: Context) {
    val dialogNoInternetBinding = DialogNoInternetBinding.inflate(LayoutInflater.from(context))
    val dialog = DefaultDialog(context, dialogNoInternetBinding.root)

    dialogNoInternetBinding.btnConfirm.setOnClickListener {
        dialog.dismiss()
    }

    dialog.show()
}

fun createSpannableString(start: Int, end: Int, text: String,context: Context): SpannableString {
    val typedValue = TypedValue()
    context.theme.resolveAttribute(com.google.android.material.R.attr.colorPrimary, typedValue, true)
    val color = typedValue.data

    val spannableString = SpannableString(text)

    // Apply a different color to the specific word
    spannableString.setSpan(
        ForegroundColorSpan(color
        ),
        start,
        end,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    return spannableString
}

fun Activity.hideKeyboard() {
    val imm: InputMethodManager =
        getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = currentFocus ?: View(this)
    imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}

fun Fragment.hideKeyboard() {
    activity?.apply {
        val imm: InputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = currentFocus ?: View(this)
        imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}

fun generateRandomIds(): String {
    val random = Random.Default
    val maxId = 826
    val numberOfIds = 10

    val idList = mutableListOf<Int>()
    repeat(numberOfIds) {
        val randomId = random.nextInt(1, maxId + 1)
        idList.add(randomId)
    }

    return idList.joinToString(",")
}