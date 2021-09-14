package com.example.peopledatabase

import android.app.Application
import com.example.peopledatabase.repository.Repository

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        Repository.initialize(this)
    }
}