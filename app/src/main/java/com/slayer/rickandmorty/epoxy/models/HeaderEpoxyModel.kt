package com.slayer.rickandmorty.epoxy.models

import com.slayer.rickandmorty.R
import com.slayer.rickandmorty.databinding.ItemRvTextHeaderBinding
import com.slayer.rickandmorty.epoxy.helpers.ViewBindingKotlinModel

data class HeaderEpoxyModel(
    private val title : String
) : ViewBindingKotlinModel<ItemRvTextHeaderBinding>(R.layout.item_rv_text_header) {
    override fun ItemRvTextHeaderBinding.bind() {
        tvHeader.text = title
    }
}