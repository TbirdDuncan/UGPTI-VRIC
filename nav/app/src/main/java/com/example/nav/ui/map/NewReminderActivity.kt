/*
 * Copyright (c) 2018 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.example.nav.ui.map

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.example.nav.R
import com.example.nav.ui.camera.BaseActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import java.sql.Connection


class NewReminderActivity : BaseActivity(), OnMapReadyCallback {

  var  url = "jdbc:jtds:sqlserver://dotsc-data.ugpti.ndsu.nodak.edu/GRIT_Test"
  var un = "AssetManagement"
  var passwords = "gem7Nuwe"
  var connect: Connection?= null
  private lateinit var map: GoogleMap



  private val radiusBarChangeListener = object : SeekBar.OnSeekBarChangeListener {
    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {}

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
      //updateRadiusWithProgress(progress)

      //showReminderUpdate()
    }
  }



  companion object {
    private const val EXTRA_LAT_LNG = "EXTRA_LAT_LNG"
    private const val EXTRA_ZOOM = "EXTRA_ZOOM"

    fun newIntent(context: Context, latLng: LatLng, zoom: Float): Intent {
      val intent = Intent(context, NewReminderActivity::class.java)
      intent
        .putExtra(EXTRA_LAT_LNG, latLng)
        .putExtra(EXTRA_ZOOM, zoom)
      return intent
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    val root = inflater.inflate(R.layout.fragment_map, container, false)
    //setContentView(R.layout.activity_main)


    // val rootView: View = inflater.inflate(R.layout.fragment_map, container, false)

    val mapFragment =
      childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?

    mapFragment!!.getMapAsync(this)

    /*instructionTitle.visibility = View.GONE
    instructionSubtitle.visibility = View.GONE
    radiusBar.visibility = View.GONE
    radiusDescription.visibility = View.GONE
    message.visibility = View.GONE*/

    //supportActionBar?.setDisplayHomeAsUpEnabled(true)
    return root
  }

   fun onSupportNavigateUp(): Boolean {
    getActivity()?.finish()
    return true
  }

  override fun onMapReady(googleMap: GoogleMap) {
    map = googleMap
    map.uiSettings.isMapToolbarEnabled = false

    centerCamera()

   // showConfigureLocationStep()
  }

  private fun centerCamera() {
    fun newIntent(context: Context, latLng: LatLng, zoom: Float): Intent {
      val intent = Intent(context, NewReminderActivity::class.java)
      intent
        .putExtra(EXTRA_LAT_LNG, latLng)
        .putExtra(EXTRA_ZOOM, zoom)

    val latLng = intent.extras?.get(EXTRA_LAT_LNG) as LatLng
    val zoom = intent.extras?.get(EXTRA_ZOOM) as Float
    map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
      return intent
    }
  }

//  private fun showConfigureLocationStep() {
//    /*marker.visibility = View.VISIBLE
//    instructionTitle.visibility = View.VISIBLE
//    instructionSubtitle.visibility = View.VISIBLE
//    radiusBar.visibility = View.GONE
//    radiusDescription.visibility = View.GONE
//    message.visibility = View.GONE
//    instructionTitle.text = getString(R.string.instruction_where_description)*/
//
//      try {
//        map.clear()
//        connect = CONN(un, passwords, url)
//        val queryCounty = "SELECT DISTINCT Lat, Long FROM Agency"
//        val stmtCounty = connect!!.prepareStatement(queryCounty)
//        val rsCounty = stmtCounty.executeQuery()
//
//        while (rsCounty.next()) {
//          val id1 = rsCounty.getDouble("Lat")
//          val id2 = rsCounty.getDouble(("Long"))
//          val latLng = LatLng(id1, id2)
//          var reminder = Reminder(latLng = null, radius = 200.00, message = "taking photo")
//          reminder.latLng = latLng
//          addReminder(reminder)
//          showReminderInMap(this.requireContext(), map, reminder)
//
//        }
//
//      } catch (e: SQLException) {
//        e.printStackTrace()
//      }
//    }




//  private fun addReminder(reminder: Reminder) {
//    getRepository()?.add(reminder,
//      success = {
//        getActivity()?.setResult(Activity.RESULT_OK)
//        getActivity()?.finish()
//        Toast.makeText(context, "finished", Toast.LENGTH_LONG).show()
//
//      },
//      failure = {
//        Snackbar.make(main, it, Snackbar.LENGTH_LONG).show()
//      })
//  }
//
//
//  private fun CONN(
//    _user: String, _pass: String, _url: String
//  ): Connection? {
//    val policy = StrictMode.ThreadPolicy.Builder()
//      .permitAll().build()
//    StrictMode.setThreadPolicy(policy)
//    var conn: Connection? = null
//    var ConnURL: String? = null
//    try {
//      Class.forName("net.sourceforge.jtds.jdbc.Driver")
//
//      conn = DriverManager.getConnection(url,  un, passwords)
//    } catch (se: SQLException) {
//      Log.e("ERRO", se.message)
//    } catch (e: ClassNotFoundException) {
//      Log.e("ERRO", e.message)
//    } catch (e: Exception) {
//      Log.e("ERRO", e.message)
//    }
//    return conn
//  }
}
