package com.example.peopledatabase.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.peopledatabase.ItemClickListener
import com.example.peopledatabase.databinding.FragmentItemBinding
import com.example.peopledatabase.repository.db.Card

class CardAdapter(private val itemClickListener: ItemClickListener) : ListAdapter<Card, CardHolder>(
    CardDiffUtil
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = FragmentItemBinding.inflate(layoutInflater, parent, false)
        return CardHolder(itemClickListener, binding)
    }

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        val car = currentList[position]
        holder.bind(car)
    }

    override fun getItemCount() = currentList.size
}