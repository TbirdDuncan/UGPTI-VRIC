package com.example.nav.ui.map

//import android.support.v7.app.AppCompatActivity  //pretty sure below import is the fix for this?
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
    fun getRepository() = (application as ReminderApp).getRepository()
}