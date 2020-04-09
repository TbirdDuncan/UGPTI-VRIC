package com.example.connecttodatabase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.AppCompatSpinner
//https://www.youtube.com/watch?v=QSdEYbEn0Ek
class CountyAdapter(val county: ArrayList<County>) : RecyclerView.Adapter<CountyAdapter.ViewHolder>(){
    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        val txtCountyName = itemView.findViewById(R.id.txtCountyName) as TextView

    }
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.lo_county,p0,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return county.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val county : County  = county[p1]


        p0?.txtCountyName.text = county.Name


    }

}

class Stateadapter(val state: ArrayList<State>) : RecyclerView.Adapter<Stateadapter.ViewHolder>(){
    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        val txtStateName = itemView.findViewById(R.id.txtStateName) as TextView

    }
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.lo_state,p0,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return state.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val state : State = state[p1]

        p0?.txtStateName.text = state.State


    }

}



