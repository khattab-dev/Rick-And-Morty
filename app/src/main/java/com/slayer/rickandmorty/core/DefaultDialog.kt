package com.slayer.rickandmorty.core

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.slayer.rickandmorty.R

class DefaultDialog(private val context: Context, private val view: View) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view)
    }


    override fun onStart() {
        super.onStart()
        val width = (context.resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (context.resources.displayMetrics.heightPixels * 0.5).toInt()

        this.window?.setBackgroundDrawableResource(R.drawable.background_dialog)

        this.window?.setLayout(
            width, ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

}