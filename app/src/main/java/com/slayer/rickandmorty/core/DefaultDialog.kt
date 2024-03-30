package com.slayer.rickandmorty.core

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup

class DefaultDialog(private val context: Context, private val view: View) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view)
    }


    override fun onStart() {
        super.onStart()
        val width = (context.resources.displayMetrics.widthPixels * 0.85).toInt()

        this.window?.setLayout(
            width, ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

}