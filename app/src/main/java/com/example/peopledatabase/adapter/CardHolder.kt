package com.example.peopledatabase.adapter

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.peopledatabase.ItemClickListener
import com.example.peopledatabase.databinding.FragmentItemBinding
import com.example.peopledatabase.repository.db.Card

class CardHolder(
    private val itemClickListener: ItemClickListener,
    binding: FragmentItemBinding

) : RecyclerView.ViewHolder(binding.root) {
    private var card: Card? = null
    private val name: TextView = binding.name
    private val age: TextView = binding.age


    fun bind(card: Card) {
        this.card = card
        name.text = card.name
        age.text = card.age.toString()
        itemView.setOnClickListener{
            itemClickListener.onItemClick(card.id)
        }
    }
}