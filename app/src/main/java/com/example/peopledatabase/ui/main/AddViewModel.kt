package com.example.peopledatabase.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.peopledatabase.repository.Repository
import com.example.peopledatabase.repository.db.Card

class AddViewModel : ViewModel() {
    private val repository = Repository.get()

    private val cardIdLiveData = MutableLiveData<Int>()
    var cardLiveDate: LiveData<Card?> =
        Transformations.switchMap(cardIdLiveData) {
                cardId -> repository.getCard(cardId)
        }


    fun addCard(card: Card) {
        repository.addCard(card)
    }

    fun loadCard(cardId: Int) {
        cardIdLiveData.value = cardId
    }

    fun update(card: Card) {
        repository.updateCard(card)
    }

    fun delete(card: Card) {
        repository.deleteCard(card)
    }

}