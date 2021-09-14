package com.example.peopledatabase.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.peopledatabase.repository.Repository
import com.example.peopledatabase.repository.db.Card

class MainViewModel : ViewModel() {
    private var sort: MutableLiveData<String> = MutableLiveData("name")

    private val repository = Repository.get()
    var cardListLiveData: LiveData<List<Card>> = Transformations.switchMap(sort){
            order -> repository.getCards(order)
    }

    fun sortBy(order: String){
        sort.value = order
    }
}