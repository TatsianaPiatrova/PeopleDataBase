package com.example.peopledatabase.repository.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CardDao {

    @Query("SELECT * FROM Card ORDER BY " +
            "CASE WHEN :order = 'name' THEN name END,"+
            "CASE WHEN :order = 'age' THEN age END")
    fun getCardsOrderBy(order:String): LiveData<List<Card>>

    @Query("SELECT * FROM Card WHERE id=(:id)")
    fun getCard(id:Int): LiveData<Card?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(card: Card)

    @Update
    fun update(card: Card)

    @Delete
    fun delete(card: Card)

}