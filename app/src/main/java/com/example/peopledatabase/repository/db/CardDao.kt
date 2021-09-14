package com.example.peopledatabase.repository.db

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface CardDao {

    @RawQuery(observedEntities = [Card::class])
    fun getCards(query: SupportSQLiteQuery): LiveData<List<Card>>

    fun getCardsOrderBy(order:String): LiveData<List<Card>>{
        val statement = "SELECT * FROM Card ORDER BY $order"

        val query: SupportSQLiteQuery = SimpleSQLiteQuery(statement, arrayOf())
        return getCards(query)
    }

    @Query("SELECT * FROM Card WHERE id=(:id)")
    fun getCard(id:Int): LiveData<Card?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(card: Card)

    @Update
    fun update(card: Card)

    @Delete
    fun delete(card: Card)

}