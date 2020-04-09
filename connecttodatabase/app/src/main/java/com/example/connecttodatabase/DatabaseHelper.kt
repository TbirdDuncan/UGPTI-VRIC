package com.example.connecttodatabase

import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

import java.io.PrintWriter
import java.io.StringWriter
import java.lang.Exception
import java.util.ArrayList

class DatabaseHelper {
    lateinit var ctx: Context

    private var isConnected = false
    private lateinit var rv: RecyclerView
    private lateinit var query: String
    private lateinit var records: ArrayList<Any>
    private lateinit var adapter: RecyclerView.Adapter<*>
    private var recordCount: Int = 0
    private var functionType: Int = 0
    lateinit var connectionClass: Connect

    inner class SyncData : AsyncTask<String, String, String>() {
        private var message = "No connection or window fire wall, not enough permissions Error:"
        lateinit var prog: ProgressDialog

        override fun onPreExecute() {
            records.clear()
            recordCount = 0
            prog = ProgressDialog.show(ctx, "Reading Data...", "loading", true)
        }

        override fun doInBackground(vararg params: String?): String {
            try {
                var myConn = connectionClass?.dbConn()
                if (myConn == null) {
                    isConnected = false
                } else {
                    val statement = myConn!!.createStatement()
                    val cursor = statement.executeQuery(query)
                    if (cursor != null) {
                        while (cursor!!.next()) {

                            try {
                                when (functionType) {
                                    1 -> records?.add(
                                        County(

                                            cursor!!.getString("Name")
                                        )
                                    )
                                    2 -> records?.add(
                                        State(

                                            cursor!!.getString("State")
                                        )
                                    )

                                }
                                recordCount++
                            } catch (ex: Exception) {
                                ex.printStackTrace()
                            }
                        }
                        message = "Found $recordCount"
                        isConnected = true
                    } else {
                        message = "no records"
                        isConnected = false
                    }
                }
            }catch (ex: Exception){
                ex.printStackTrace()
                val writer = StringWriter()
                ex.printStackTrace(PrintWriter(writer))
                message = writer.toString()
                isConnected = false
            }
            return message
        }

        override fun onPostExecute(result: String?) {
            prog.dismiss()
            Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show()
            if(isConnected == false){


            }else{
                try{
                    rv.adapter = adapter
                }catch (ex: Exception){

                }
            }
        }
    }
    fun getCounty(rv: RecyclerView){
        this.rv = rv
        query = "SELECT DISTINCT Name FROM Agency"
        records = ArrayList<CountyActivity>() as ArrayList<Any>
        adapter = CountyAdapter(records as ArrayList<County>)
        functionType = 1
        SyncData().execute("")


    }
    fun getState(rv: RecyclerView){
        this.rv = rv
        query = "SELECT DISTINCT State FROM Agency"
        records = ArrayList<StateActivity>() as ArrayList<Any>
        adapter = Stateadapter(records as ArrayList<State>)
        functionType = 2
        SyncData().execute("")


    }

}