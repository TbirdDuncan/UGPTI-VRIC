package com.example.connecttodatabase

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.connecttodatabase.R

class StateActivity: AppCompatActivity(){

    lateinit var rv : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_state)

        rv = findViewById(R.id.rvState)
        rv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        MainActivity.db.ctx = this
        MainActivity.db.getState(rv)

    }

}
