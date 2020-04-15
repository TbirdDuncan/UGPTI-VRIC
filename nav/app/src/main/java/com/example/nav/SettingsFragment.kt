package com.example.nav



import android.annotation.SuppressLint
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.sql.*
import java.util.*


class SettingsFragment : AppCompatActivity() {
    var spinnercountry: Spinner? = null
    var spinnercounty: Spinner? = null


    lateinit var un: String
    lateinit var passwords: String
    lateinit var url: String

    var connect: Connection?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_setting)

        val navView: BottomNavigationView = findViewById(R.id.nav_view1)

        val navController = findNavController(R.id.nav_host_fragment1)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation1_camera, R.id.navigation1_help, R.id.navigation1_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        url = "jdbc:jtds:sqlserver://dotsc-data.ugpti.ndsu.nodak.edu/GRIT_Test"
        un = "AssetManagement"
        passwords = "gem7Nuwe"
        spinnercountry = findViewById(R.id.spinner) as Spinner?
        spinnercounty = findViewById(R.id.spinner2) as Spinner?
        connect = CONN(url, un, passwords)
        val query = "SELECT DISTINCT State FROM Agency"


        try {
            connect = CONN(un, passwords, url)
            val stmt = connect!!.prepareStatement(query)
            val rs = stmt.executeQuery()
            val data = ArrayList<String?>()
            while (rs.next()) {
                val id = rs.getString("State")
                data.add(id)
            }

            val array = data.toTypedArray()
            val NoCoreAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
                this,
                android.R.layout.simple_list_item_1, array
            )

            spinnercountry!!.adapter = NoCoreAdapter
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        spinnercountry!!.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View,
                position: Int, id: Long
            ) {
                val name = spinnercountry!!.selectedItem.toString()

                if(name.equals("MN")){
                    try {
                        connect = CONN(un, passwords, url)
                        val queryCounty = "SELECT DISTINCT Name FROM Agency WHERE State = 'MN'"
                        val stmtCounty = connect!!.prepareStatement(queryCounty)
                        val rsCounty = stmtCounty.executeQuery()
                        val dataCounty = ArrayList<String?>()
                        while (rsCounty.next()) {
                            val id1 = rsCounty.getString("Name")
                            dataCounty.add(id1)
                        }
                        val arrayCounty = dataCounty.toTypedArray()
                        val NoCoreAdapterCounty: ArrayAdapter<*> = ArrayAdapter<Any?>(
                           this@SettingsFragment, android.R.layout.simple_list_item_1, arrayCounty
                        )
                        spinnercounty!!.adapter = NoCoreAdapterCounty
                    }catch (e: SQLException) {
                        e.printStackTrace()
                    }



                }else if(name.equals("ND")){
                    try {
                        connect = CONN(un, passwords, url)
                        val queryCounty = "SELECT DISTINCT Name FROM Agency WHERE State = 'ND'"
                        val stmtCounty = connect!!.prepareStatement(queryCounty)
                        val rsCounty = stmtCounty.executeQuery()
                        val dataCounty = ArrayList<Any?>()
                        while (rsCounty.next()) {
                            val id1 = rsCounty.getString("Name")
                            dataCounty.add(id1)
                        }
                        val arrayCounty = dataCounty.toTypedArray()
                        val NoCoreAdapterCounty: ArrayAdapter<*> = ArrayAdapter<Any?>(
                            this@SettingsFragment, android.R.layout.simple_list_item_1, arrayCounty
                        )
                        spinnercounty!!.adapter = NoCoreAdapterCounty
                    }catch (e: SQLException) {
                        e.printStackTrace()
                    }



                }else if(name.equals("SD")){
                    try {
                        connect = CONN(un, passwords, url)
                        val queryCounty = "SELECT DISTINCT Name FROM Agency WHERE State = 'SD'"
                        val stmtCounty = connect!!.prepareStatement(queryCounty)
                        val rsCounty = stmtCounty.executeQuery()
                        val dataCounty = ArrayList<Any?>()
                        while (rsCounty.next()) {
                            val id1 = rsCounty.getString("Name")
                            dataCounty.add(id1)
                        }
                        val arrayCounty = dataCounty.toTypedArray()
                        val NoCoreAdapterCounty: ArrayAdapter<*> = ArrayAdapter<Any?>(
                            this@SettingsFragment, android.R.layout.simple_list_item_1, arrayCounty
                        )
                        spinnercounty!!.adapter = NoCoreAdapterCounty
                    }catch (e: SQLException) {
                        e.printStackTrace()
                    }



                }else if(name.equals("MT")){
                        try {
                            connect = CONN(un, passwords, url)
                            val queryCounty = "SELECT DISTINCT Name FROM Agency WHERE State = 'MT'"
                            val stmtCounty = connect!!.prepareStatement(queryCounty)
                            val rsCounty = stmtCounty.executeQuery()
                            val dataCounty = ArrayList<Any?>()
                            while (rsCounty.next()) {
                                val id1 = rsCounty.getString("Name")
                                dataCounty.add(id1)
                            }
                            val arrayCounty = dataCounty.toTypedArray()
                            val NoCoreAdapterCounty: ArrayAdapter<*> = ArrayAdapter<Any?>(
                                this@SettingsFragment, android.R.layout.simple_list_item_1, arrayCounty
                            )
                            spinnercounty!!.adapter = NoCoreAdapterCounty
                        }catch (e: SQLException) {
                            e.printStackTrace()
                        }



                    }




                Toast.makeText(this@SettingsFragment, name, Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    @SuppressLint("NewApi")
    private fun CONN(
        _user: String, _pass: String, _url: String
    ): Connection? {
        val policy = StrictMode.ThreadPolicy.Builder()
            .permitAll().build()
        StrictMode.setThreadPolicy(policy)
        var conn: Connection? = null
        var ConnURL: String? = null
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver")

            conn =DriverManager.getConnection(url,  un, passwords)
        } catch (se: SQLException) {
            Log.e("ERRO", se.message)
        } catch (e: ClassNotFoundException) {
            Log.e("ERRO", e.message)
        } catch (e: Exception) {
            Log.e("ERRO", e.message)
        }
        return conn
    }
}