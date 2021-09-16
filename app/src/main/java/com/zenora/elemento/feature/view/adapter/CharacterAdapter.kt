package com.zenora.elemento.feature.view.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zenora.elemento.CharactersListQuery
import com.zenora.elemento.databinding.ItemCharacterBinding

class CharacterAdapter {
}

class CharacterDiffUtil : DiffUtil.ItemCallback<CharactersListQuery.Result>() {

    class CharacterViewHolder(val binding: ItemCharacterBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun areItemsTheSame(
        oldItem: CharactersListQuery.Result,
        newItem: CharactersListQuery.Result
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: CharactersListQuery.Result,
        newItem: CharactersListQuery.Result
    ): Boolean {
        return oldItem == newItem
    }

}