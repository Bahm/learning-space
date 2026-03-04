package com.example.learningspace.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.learningspace.R
import com.example.learningspace.data.DeckWithCardCount
import com.example.learningspace.databinding.ItemDeckBinding

class DeckAdapter(
    private val onItemClick: (DeckWithCardCount) -> Unit,
    private val onDeleteClick: (DeckWithCardCount) -> Unit
) : ListAdapter<DeckWithCardCount, DeckAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemDeckBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DeckWithCardCount) {
            binding.deckName.text = item.deck.name
            binding.cardCount.text = binding.root.context.getString(R.string.card_count, item.cardCount)
            binding.root.setOnClickListener { onItemClick(item) }
            binding.deleteButton.setOnClickListener { onDeleteClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDeckBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<DeckWithCardCount>() {
        override fun areItemsTheSame(oldItem: DeckWithCardCount, newItem: DeckWithCardCount) =
            oldItem.deck.id == newItem.deck.id
        override fun areContentsTheSame(oldItem: DeckWithCardCount, newItem: DeckWithCardCount) =
            oldItem == newItem
    }
}
