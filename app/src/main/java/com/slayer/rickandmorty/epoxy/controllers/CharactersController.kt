package com.slayer.rickandmorty.epoxy.controllers

import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.carousel
import com.airbnb.epoxy.paging3.PagingDataEpoxyController
import com.bumptech.glide.Glide
import com.slayer.domain.models.Character
import com.slayer.rickandmorty.R
import com.slayer.rickandmorty.databinding.ItemRvCharactersBinding
import com.slayer.rickandmorty.databinding.ItemRvRandomCharactersBinding
import com.slayer.rickandmorty.epoxy.helpers.ViewBindingKotlinModel
import com.slayer.rickandmorty.epoxy.models.HeaderEpoxyModel

class CharactersController(
    private val onFavoriteClick: (Character) -> Unit
) : PagingDataEpoxyController<Character>() {
    val randomCharss: MutableList<Character> = mutableListOf()
    override fun buildItemModel(currentPosition: Int, item: Character?): EpoxyModel<*> {
        return CharacterEpoxyModel(onFavoriteClick, item!!).id(item.id)
    }

    override fun addModels(models: List<EpoxyModel<*>>) {
        val randChars = randomCharss

        HeaderEpoxyModel("Random picks").id("header_random_picks").addTo(this)

        carousel {
            id("random_chars")
            models(randChars.map { RandomCharacterEpoxyModel(it).id("random_char${it.id}") })
            padding(Carousel.Padding(0,0,0,32,0))
            numViewsToShowOnScreen(3f)
        }

        HeaderEpoxyModel("Characters").id("header_characters").addTo(this)

        super.addModels(models)
    }

    data class CharacterEpoxyModel(
        private val onFavoriteClick: (Character) -> Unit,
        val item: Character
    ) : ViewBindingKotlinModel<ItemRvCharactersBinding>(R.layout.item_rv_characters) {
        override fun ItemRvCharactersBinding.bind() {
            tvName.text = item.name
            tvDetails.text =
                tvDetails.context.getString(R.string.character_details, item.type, item.state)

            Glide.with(ivCharacter).load(item.image).into(ivCharacter)

            if (item.isFavorite) {
                btnFavorite.setIconResource(R.drawable.baseline_favorite_24)
            } else {
                btnFavorite.setIconResource(R.drawable.baseline_favorite_border_24)
            }

            btnFavorite.setOnClickListener {
                if (item.isFavorite) {
                    item.isFavorite = false
                    btnFavorite.setIconResource(R.drawable.baseline_favorite_border_24)
                } else {
                    item.isFavorite = true
                    btnFavorite.setIconResource(R.drawable.baseline_favorite_24)
                }

                onFavoriteClick.invoke(item)
            }
        }
    }

    data class RandomCharacterEpoxyModel(
        private val item: Character
    ) : ViewBindingKotlinModel<ItemRvRandomCharactersBinding>(R.layout.item_rv_random_characters) {
        override fun ItemRvRandomCharactersBinding.bind() {
            tvCharacterName.text = item.name

            Glide.with(ivCharacter).load(item.image).into(ivCharacter)
        }
    }
}