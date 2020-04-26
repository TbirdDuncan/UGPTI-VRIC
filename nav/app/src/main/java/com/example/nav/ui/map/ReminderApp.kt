package com.example.nav.ui.map

import android.app.Application

class ReminderApp : Application() {

    private lateinit var repository: ReminderRepository

    override fun onCreate() {
        super.onCreate()
        repository = ReminderRepository(this)
    }

    fun getRepository() = repository
}