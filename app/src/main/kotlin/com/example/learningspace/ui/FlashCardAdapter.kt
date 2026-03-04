package com.example.learningspace.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.learningspace.data.FlashCard
import com.example.learningspace.databinding.ItemFlashCardBinding

class FlashCardAdapter(
    private val onItemClick: (FlashCard) -> Unit,
    private val onEditClick: (FlashCard) -> Unit,
    private val onDeleteClick: (FlashCard) -> Unit
) : ListAdapter<FlashCard, FlashCardAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemFlashCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(card: FlashCard) {
            binding.questionText.text = card.question
            binding.root.setOnClickListener { onItemClick(card) }
            binding.editButton.setOnClickListener { onEditClick(card) }
            binding.deleteButton.setOnClickListener { onDeleteClick(card) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFlashCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<FlashCard>() {
        override fun areItemsTheSame(oldItem: FlashCard, newItem: FlashCard) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: FlashCard, newItem: FlashCard) = oldItem == newItem
    }
}
