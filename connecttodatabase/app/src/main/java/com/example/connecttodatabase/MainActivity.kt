package com.example.connecttodatabase

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity


class MainActivity: AppCompatActivity(){

    companion object{
        val db = DatabaseHelper()
    }
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db.connectionClass = Connect()
    }

    fun OnClick1(view: View){
        val i = Intent(this, CountyActivity::class.java)
        startActivity(i)

    }
    fun OnClick(view: View){
        val a = Intent(this, StateActivity::class.java)
        startActivity(a)

    }
}