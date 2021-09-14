package com.example.peopledatabase.repository

import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.peopledatabase.repository.db.Card
import com.example.peopledatabase.repository.db.CardDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val LOG_TAG = "SQLiteOpenHelper"
private const val DATABASE_NAME = "People-database"
private const val TABLE_NAME = "Card"
private const val DATABASE_VERSION = 1
private const val CREATE_TABLE_SQL =
    "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
            "id	INTEGER NOT NULL," +
            "name	TEXT NOT NULL," +
            "age	INTEGER NOT NULL," +
            "PRIMARY KEY(id AUTOINCREMENT)" +
            ");"

class CardDataCursor(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION), CardDao {
    override fun onCreate(db: SQLiteDatabase?) {
        try {
            db?.execSQL(CREATE_TABLE_SQL)

        } catch (exception: SQLException) {
            Log.e(LOG_TAG, "SQLiteOpenHelper", exception)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.d(LOG_TAG, "onUpgrade called")
    }


    private suspend fun getCardsList(orderList:String):List<Card>{
        return withContext(Dispatchers.IO) {
            val listOfCards = mutableListOf<Card>()
            val db = writableDatabase
            val selectQuery = "SELECT * FROM $TABLE_NAME ORDER BY $orderList"
            val cursor = db.rawQuery(selectQuery,null)
            cursor?.let{
                if (cursor.moveToFirst()) {
                    do {
                        val id = cursor.getInt(cursor.getColumnIndex("id"))
                        val name = cursor.getString(cursor.getColumnIndex("name"))
                        val age = cursor.getInt(cursor.getColumnIndex("age"))
                        listOfCards.add(Card(id, name, age))
                    } while (cursor.moveToNext())
                }
            }
            cursor.close()
            listOfCards
        }
    }

    override fun getCardsOrderBy(order: String): LiveData<List<Card>> {
        Log.d(LOG_TAG, "Cursor getCardsOrderBy($order)")
        return liveData<List<Card>> {
            emit(getCardsList(order))
        }
    }



    override fun getCard(id: Int): LiveData<Card?> {

        val cardLiveData = MutableLiveData<Card>()
        val db = writableDatabase

        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE id = $id"
        val cursor = db.rawQuery(selectQuery, null)
        Log.d(LOG_TAG, "$cursor")
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {

                    val name = cursor.getString(cursor.getColumnIndex("name"))
                    val age = cursor.getInt(cursor.getColumnIndex("age"))
                    val card =  Card(id, name, age)
                    Log.d(LOG_TAG, "FROM GET CAR CURSOR $card")
                    cardLiveData.value = card
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        return cardLiveData
    }


    override fun add(card: Card) {
        Log.d(LOG_TAG, "Cursor addCard($card)")
        val db = writableDatabase
        val values = ContentValues()
        values.put("name",card.name)
        values.put("age",card.age)
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    override fun update(card: Card) {
        Log.d(LOG_TAG, "Cursor updateCard($card)")
        val db = writableDatabase
        val values = ContentValues()
        values.put("id",card.id)
        values.put("name",card.name)
        values.put("age",card.age)
        db.update(TABLE_NAME, values, "id" + "=?", arrayOf(card.id.toString()))
        db.close()
    }

    override fun delete(card: Card) {
        Log.d(LOG_TAG, "Cursor deleteCard($card)")
        val db = writableDatabase
        db.delete(TABLE_NAME, "id" + "=?", arrayOf(card.id.toString()))
        db.close()
    }
}