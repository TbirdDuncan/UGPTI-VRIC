package com.example.nav.ui.setting











import android.os.Bundle



import android.view.LayoutInflater



import android.view.View



import android.view.ViewGroup



import android.widget.*



import androidx.fragment.app.Fragment


import com.example.nav.R


import android.widget.ArrayAdapter
import android.widget.Toast.makeText
import kotlinx.android.synthetic.main.fragment_setting.*
import org.w3c.dom.Text


class SettingsFragment : Fragment() {

    override fun onCreateView(

        inflater: LayoutInflater,

        container: ViewGroup?,

        savedInstanceState: Bundle?

    ): View {

        val root = inflater.inflate(R.layout.fragment_setting, container, false)

        val spinnerState = root.findViewById<Spinner>(R.id.spinner)

        val spinnerCounty = root.findViewById<Spinner>(R.id.spinner2)

        val spinnerVehicleType = root.findViewById<Spinner>(R.id.spinner3)

        spinnerCounty.setEnabled(false);


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

        ArrayAdapter.createFromResource(

            activity!!.applicationContext,

            R.array.vehicle,

            android.R.layout.simple_spinner_item).also{adapter ->

            // Specify the layout to use when the list of choices appears

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            // Apply the adapter to the spinner

            spinnerVehicleType.adapter = adapter

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
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }


            fun onItemSelected(

                parent: AdapterView<*>?,

                view: View?,

                position: Int,

                id: Long,

                enable: Boolean

            ) {

                val type = parent?.getItemAtPosition(position).toString()

                spinnerCounty.setEnabled(true)

                makeText(activity, type, Toast.LENGTH_LONG).show()


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
                makeText(activity, type, Toast.LENGTH_LONG).show()
                println(type)
            }
        }


            spinnerVehicleType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {


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



                    makeText(activity, type, Toast.LENGTH_LONG).show()



                    println(type)


                }


            }


            //val textView: TextView = root.findViewById(R.id.text_home)


            //homeViewModel.text.observe(viewLifecycleOwner, Observer {


            //textView.text = it









        return root



    }







}