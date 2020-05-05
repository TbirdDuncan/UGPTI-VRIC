package com.example.nav.ui.camera
import android.app.Application

class nav : Application() {
  private lateinit var repository: ReminderRepository
  override fun onCreate() {
    super.onCreate()
    repository = ReminderRepository(this)
  }
  fun getRepository() = repository
}