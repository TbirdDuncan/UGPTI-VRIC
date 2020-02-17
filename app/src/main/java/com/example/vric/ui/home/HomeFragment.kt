package com.example.vric.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.vric.R
import kotlinx.android.synthetic.main.fragment_home.*
import android.widget.ArrayAdapter

class HomeFragment : Fragment() {

    //private lateinit var homeViewModel: HomeViewModel

    val types = arrayOf("North Dakota", "South Dakota", "Minnesota")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        //homeViewModel =
            //ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val spinnerState = root.findViewById<Spinner>(R.id.spinner)
        val spinnerCounty = root.findViewById<Spinner>(R.id.spinner2)
        //spinner?.adapter = ArrayAdapter(activity!!.applicationContext, R.layout.support_simple_spinner_dropdown_item, types) as SpinnerAdapter
        ArrayAdapter.createFromResource(
            activity!!.applicationContext,
            R.array.stateDropdown,
            android.R.layout.simple_spinner_item).also{adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinnerState.adapter = adapter
        }
        ArrayAdapter.createFromResource(
            activity!!.applicationContext,
            R.array.counties,
            android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerCounty.adapter = adapter
        }


        //spinner.onItemClickListener = object
        spinnerState.onItemSelectedListener = object :AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                println("erreur")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val type = parent?.getItemAtPosition(position).toString()
                Toast.makeText(activity, type, Toast.LENGTH_LONG).show()
                println(type)
            }
        }
        spinnerCounty.onItemSelectedListener = object :AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                println("erreur")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val type = parent?.getItemAtPosition(position).toString()
                Toast.makeText(activity, type, Toast.LENGTH_LONG).show()
                println(type)
            }
        }
            //val textView: TextView = root.findViewById(R.id.text_home)
            //homeViewModel.text.observe(viewLifecycleOwner, Observer {
            //textView.text = it


        return root
    }

        }