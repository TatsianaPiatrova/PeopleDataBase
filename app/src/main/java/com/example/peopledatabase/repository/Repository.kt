package com.example.peopledatabase.repository

import android.annotation.SuppressLint
import android.content.Context
import android.preference.PreferenceManager
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.peopledatabase.repository.db.Card
import com.example.peopledatabase.repository.db.CardDao
import com.example.peopledatabase.repository.db.PeopleDatabase
import java.util.concurrent.Executors

private const val DATABASE_NAME ="People-database"

class Repository private constructor(val context: Context){
    private val database: PeopleDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            PeopleDatabase::class.java,
            DATABASE_NAME
        ).build()


    private val cardDao: CardDao
        get(){
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val dataSource = sharedPreferences.getBoolean("switch",true)
            return if(dataSource) database.cardDao() else
                CardDataCursor(context)
        }


    private val executor = Executors.newSingleThreadExecutor()

    fun getCards(order:String): LiveData<List<Card>> = cardDao.getCardsOrderBy(order)

    fun getCard(id:Int):LiveData<Card?> = cardDao.getCard(id)

    fun updateCard(card:Card){
        executor.execute{
            cardDao.update(card)
        }
    }

    fun deleteCard(card:Card){
        executor.execute{
            cardDao.delete(card)
        }
    }

    fun addCard(card:Card){
        executor.execute{
            cardDao.add(card)
        }
    }

    companion object{
        @SuppressLint("StaticFieldLeak")
        private  var INSTANCE: Repository? = null

        fun initialize(context: Context){
            if(INSTANCE == null){
                INSTANCE = Repository(context)
            }
        }
        fun get(): Repository {
            return INSTANCE ?:
            throw IllegalStateException("Repository must be initialized")
        }
    }
}