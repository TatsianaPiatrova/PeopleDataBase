package com.example.peopledatabase.repository.db


import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Card(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var name: String = "",
    var age: Int = 0,
)