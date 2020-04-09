package com.example.connecttodatabase

import android.os.StrictMode
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.ArrayList

class  Connect{
    private val url = "jdbc:jtds:sqlserver://dotsc-data.ugpti.ndsu.nodak.edu/GRIT_Test"
    //private val db = "RIC"
    private val username ="AssetManagement"
    private val password = "gem7Nuwe"



    fun dbConn() : Connection? {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        var conn: Connection? = null
        var connString: String? = null
        try{
            Class.forName("net.sourceforge.jtds.jdbc.Driver")

            conn =DriverManager.getConnection(url,  username, password)

        }catch (ex: SQLException){
            Log.e("error", ex.message)
        }catch (ex1: ClassNotFoundException){
            Log.e("error", ex1.message)
        }catch (ex2: Exception){
            Log.e("error", ex2.message)
        }
        return conn
    }

}