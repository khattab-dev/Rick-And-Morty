package com.slayer.rickandmorty.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.slayer.domain.models.Location
import com.slayer.rickandmorty.R
import com.slayer.rickandmorty.databinding.ItemRvLocationBinding

class LocationsAdapter : PagingDataAdapter<Location, LocationsAdapter.ViewHolder>(
    LocationsDiffer()
) {
    inner class ViewHolder(
        private val binding: ItemRvLocationBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Location) {
            binding.apply {
                tvName.text = item.name
                tvType.text = item.type

                Glide.with(imageView).load(R.drawable.location).into(imageView)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemRvLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    class LocationsDiffer : DiffUtil.ItemCallback<Location>() {
        override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
            return oldItem == newItem
        }
    }
}