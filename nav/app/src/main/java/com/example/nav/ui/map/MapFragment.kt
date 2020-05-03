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

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.nav.R
import com.example.nav.ui.camera.BaseActivity
import com.example.nav.ui.camera.Reminder
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_camera.*
import kotlinx.android.synthetic.main.fragment_map.*
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.*


class MapFragment : BaseActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
  var  url = "jdbc:jtds:sqlserver://dotsc-data.ugpti.ndsu.nodak.edu/GRIT_Test"
  var un = "AssetManagement"
  var passwords = "gem7Nuwe"
  var connect: Connection?= null
  companion object {
    private const val MY_LOCATION_REQUEST_CODE = 329
    private const val NEW_REMINDER_REQUEST_CODE = 330
    private const val EXTRA_LAT_LNG = "EXTRA_LAT_LNG"

    fun newIntent(context: Context, latLng: LatLng): Intent {
      val intent = Intent(context, MapFragment::class.java)
      intent.putExtra(EXTRA_LAT_LNG, latLng)
      return intent
    }
  }

  private var map:GoogleMap? = null

  private lateinit var locationManager: LocationManager

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    val root = inflater.inflate(R.layout.fragment_map, container, false)
    //setContentView(R.layout.activity_main)

    val mapFragment =
      childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

    mapFragment.getMapAsync(this)


    locationManager = activity!!.applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    if (ContextCompat.checkSelfPermission(
        this.requireContext(),
        Manifest.permission.ACCESS_FINE_LOCATION)
      != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(
        this.requireActivity(),
        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
        MY_LOCATION_REQUEST_CODE)
    }
    return root
  }



  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    if (requestCode == NEW_REMINDER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
      showReminders()

      val reminder = getRepository().getLast()
      map?.moveCamera(CameraUpdateFactory.newLatLngZoom(reminder?.latLng, 15f))

      Toast.makeText(this.requireContext(), R.string.reminder_added_success, Toast.LENGTH_LONG).show()
    }
  }

  override fun onRequestPermissionsResult(requestCode: Int,
                                          permissions: Array<out String>,
                                          grantResults: IntArray) {
    if (requestCode == MY_LOCATION_REQUEST_CODE) {
      onmapAndPermissionReady()
    }
  }

  private fun onmapAndPermissionReady() {
    if (map != null
      && ContextCompat.checkSelfPermission(
        this.requireContext(),
        Manifest.permission.ACCESS_FINE_LOCATION)
      == PackageManager.PERMISSION_GRANTED) {
      map?.isMyLocationEnabled = true
      //newReminder.visibility = View.VISIBLE

      showReminders()

      centerCamera()
    }
  }
  val  urlGrit = "jdbc:jtds:sqlserver://dotsc-data.ugpti.ndsu.nodak.edu/GRIT_Test"
  val unGrit = "AssetManagement"
  val passwordGrit = "gem7Nuwe"
  private fun showConfigureLocationStep() {
//    fab_camera.setOnClickListener {
//
//     // var msg = "unknown"
//            try {
//                connect = CONN(urlGrit, unGrit, passwordGrit)
//                val queryCounty = "SELECT DISTINCT Lat, Long FROM RIC_Layer WHERE ID = '1003'"
//                val stmtCounty = connect!!.prepareStatement(queryCounty)
//                val rsCounty = stmtCounty.executeQuery()
//                while (rsCounty.next()) {
//                    val idLat = rsCounty.getDouble("Lat")
//                    val idLong = rsCounty.getDouble("Long")
//                    val id: String = UUID.randomUUID().toString()
//                    val latLng = LatLng(idLat, idLong)
//                    val reminder = Reminder(
//                        id,
//                        latLng = null,
//                        message = "Taking Photo",
//                        radius = 200.00
//                    )
//                    reminder.latLng = latLng
//                    if(reminder.latLng != null){
//                        getRepository().remove(reminder,success ={Toast.makeText(this.requireContext(), "removed", Toast.LENGTH_SHORT).show()}, failure = {Toast.makeText(this.requireContext(),"failed to remove", Toast.LENGTH_SHORT).show()})
//                    }
//
//
//                    addReminder(reminder)
//
//
//                }
//
//            } catch (e: SQLException) {
//                e.printStackTrace()
//            }
//
//
//
//
//
//
//
//  }

}
  private fun addReminder(reminder: Reminder) {
    getRepository().add(reminder)
  }




  private fun centerCamera() {
    fun newIntent(context: Context, latLng: LatLng): Intent {
      val intent = Intent(context, MapFragment::class.java)
      intent.putExtra(EXTRA_LAT_LNG, latLng)

    if (intent.extras != null && intent.extras!!.containsKey(EXTRA_LAT_LNG)) {
      val latLng = intent.extras!!.get(EXTRA_LAT_LNG) as LatLng
      map?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
    }
      return intent
    }
  }

  private fun showReminders() {
    map?.run {
      clear()
      val rep = getRepository().getAll()
      if (rep != null) {
        for (reminder in rep) {
          showReminderInMap(activity!!.applicationContext, this, reminder)
        }
      }
    }
  }

  override fun onMapReady(googlemap: GoogleMap) {
    map = googlemap
    map?.run {
      uiSettings.isMyLocationButtonEnabled = false
      uiSettings.isMapToolbarEnabled = false
      setOnMarkerClickListener(this@MapFragment)
    }

    onmapAndPermissionReady()
  }

  override fun onMarkerClick(marker: Marker): Boolean {
    val reminder = getRepository().get(marker.tag as String)

    if (reminder != null) {
      showReminderRemoveAlert(reminder)
    }

    return true
  }

  private fun showReminderRemoveAlert(reminder: Reminder) {
    val alertDialog = AlertDialog.Builder(this.requireContext()).create()
    alertDialog.run {
      setMessage(getString(R.string.reminder_removal_alert))
      setButton(AlertDialog.BUTTON_POSITIVE,
        getString(R.string.reminder_removal_alert_positive)) { dialog, _ ->
        dialog.dismiss()
      }
      setButton(AlertDialog.BUTTON_NEGATIVE,
        getString(R.string.reminder_removal_alert_negative)) { dialog, _ ->
        dialog.dismiss()
      }
      show()
    }
  }

//  private fun removeReminder(reminder: Reminder) {
//    getRepository().remove(
//      reminder,
//      success = {
//        showReminders()
//        Toast.makeText(this.requireContext(), "removed", Toast.LENGTH_SHORT).show()
//      },
//      failure = {
//        Toast.makeText(this.requireContext(), "failed", Toast.LENGTH_SHORT).show()
//      })
//  }
  private fun CONN(
    _user: String, _pass: String, _url: String
  ): Connection? {
    val policy = StrictMode.ThreadPolicy.Builder()
      .permitAll().build()
    StrictMode.setThreadPolicy(policy)
    var con:Connection?=null

    try {

      Class.forName("net.sourceforge.jtds.jdbc.Driver")
      con = DriverManager.getConnection( urlGrit, unGrit, passwordGrit)



    } catch (se: SQLException) {
      Log.e("ERRO", se.message)
    } catch (e: ClassNotFoundException) {
      Log.e("ERRO", e.message)
    } catch (e: Exception) {
      Log.e("ERRO", e.message)
    }
    return con
  }
}
