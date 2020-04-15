package com.example.nav.ui.setting



import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.StrictMode
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.nav.R
import kotlinx.android.synthetic.main.fragment_setting.*
import java.sql.*
import java.util.*


class SettingsFragment : Fragment() {
    var spinnercountry: Spinner? = null
    var spinnercounty: Spinner? = null


    lateinit var un: String
    lateinit var passwords: String
    lateinit var url: String
    //var ctx:Context? = null
    var connect: Connection?= null
    lateinit var dropdown: View
    lateinit var dropdown1: View


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {


        val rootView: View =
            inflater.inflate(R.layout.fragment_setting, container, false)

        dropdown1 = rootView.findViewById<View>(R.id.spinner)
        dropdown = rootView.findViewById<View>(R.id.spinner2)

        //setContentView(R.layout.fragment_setting)


        url = "jdbc:jtds:sqlserver://dotsc-data.ugpti.ndsu.nodak.edu/GRIT_Test"
        un = "AssetManagement"
        passwords = "gem7Nuwe"
        spinnercountry = dropdown1 as Spinner
        spinnercounty = dropdown as Spinner
        connect = CONN(url, un, passwords)


        try {
            connect = CONN(un, passwords, url)
            val data = ArrayList<String?>()
            val id5 = "Select State"
            val id1 = "Minnesota"
            val id2 = "North Dakota"
            val id3 = "Montana"
            val id4 = "South Dakota"
            data.add(id5)
            data.add(id1)
            data.add(id2)
            data.add(id3)
            data.add(id4)


            val array = data.toTypedArray()
            val NoCoreAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
                this.requireContext(),
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
                if(name.equals("Minnesota")){
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
                           activity!!.applicationContext, android.R.layout.simple_list_item_1, arrayCounty
                        )
                        spinnercounty!!.adapter = NoCoreAdapterCounty
                    }catch (e: SQLException) {
                        e.printStackTrace()
                    }



                }else if(name.equals("North Dakota")){
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
                            activity!!.applicationContext, android.R.layout.simple_list_item_1, arrayCounty
                        )
                        spinnercounty!!.adapter = NoCoreAdapterCounty
                    }catch (e: SQLException) {
                        e.printStackTrace()
                    }



                }else if(name.equals("South Dakota")){
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
                            activity!!.applicationContext, android.R.layout.simple_list_item_1, arrayCounty
                        )
                        spinnercounty!!.adapter = NoCoreAdapterCounty
                    }catch (e: SQLException) {
                        e.printStackTrace()
                    }



                }else if(name.equals("Montana")){
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
                                activity!!.applicationContext, android.R.layout.simple_list_item_1, arrayCounty
                            )
                            spinnercounty!!.adapter = NoCoreAdapterCounty
                        }catch (e: SQLException) {
                            e.printStackTrace()
                        }



                    }




                Toast.makeText(activity!!.applicationContext, name, Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        return rootView
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