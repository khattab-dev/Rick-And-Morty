package com.slayer.rickandmorty.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.slayer.domain.repositories.models.Character
import com.slayer.rickandmorty.R
import com.slayer.rickandmorty.databinding.ItemRvCharactersBinding

class CharactersAdapter : PagingDataAdapter<Character, CharactersAdapter.ViewHolder>(
    CharactersDiffer()
) {
    inner class ViewHolder(
        private val binding: ItemRvCharactersBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Character) {
            binding.apply {
                tvName.text = item.name
                tvDetails.text =
                    tvDetails.context.getString(R.string.character_details, item.type, item.state)

                Glide.with(ivCharacter).load(item.image).into(ivCharacter)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemRvCharactersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    class CharactersDiffer : DiffUtil.ItemCallback<Character>() {
        override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem == newItem
        }
    }
}