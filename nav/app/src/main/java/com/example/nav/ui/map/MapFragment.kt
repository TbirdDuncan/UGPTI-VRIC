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
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_new_reminder.*
import kotlinx.android.synthetic.main.fragment_map.*
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException


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
  /*override fun onSupportNavigateUp(): Boolean {
    //finish()
    return true
  }*/

  private var map:GoogleMap? = null

  private lateinit var locationManager: LocationManager

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    val root = inflater.inflate(R.layout.fragment_map, container, false)
    //setContentView(R.layout.activity_main)


   // val rootView: View = inflater.inflate(R.layout.fragment_map, container, false)

    val mapFragment =
      childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

    mapFragment.getMapAsync(this)

    //newReminder.visibility = View.GONE
    //currentLocation.visibility = View.GONE

//    newReminder.visibility = View.GONE
  //  currentLocation.visibility = View.GONE

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

      Snackbar.make(main, R.string.reminder_added_success, Snackbar.LENGTH_LONG).show()
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
      currentLocation.visibility = View.VISIBLE

      currentLocation.setOnClickListener {
//        marker.visibility = View.VISIBLE
//        instructionTitle.visibility = View.VISIBLE
//        instructionSubtitle.visibility = View.VISIBLE
//        radiusBar.visibility = View.GONE
//        radiusDescription.visibility = View.GONE
//        message.visibility = View.GONE
//        instructionTitle.text = getString(R.string.instruction_where_description)

        try {
          //map?.clear()
          connect = CONN(un, passwords, url)
          val queryCounty = "SELECT Lat, Long FROM Agency"
          val stmtCounty = connect!!.prepareStatement(queryCounty)
          val rsCounty = stmtCounty.executeQuery()

          while (rsCounty.next()) {
            val id1 = rsCounty.getDouble("Lat")
            val id2 = rsCounty.getDouble("Long")
            val latLng1 = LatLng(id1, id2)
            val reminder = Reminder(latLng = null, radius = 200.00, message = "taking photo")
            reminder.latLng = latLng1

            Toast.makeText(context, "adding reminder", Toast.LENGTH_SHORT).show()

            getRepository().add(reminder,
              success = {
                showReminderInMap(this.requireContext(), map as GoogleMap, reminder)


              },
              failure = {
                Toast.makeText(context, "failed", Toast.LENGTH_LONG).show()
              })



          }

        } catch (e: SQLException) {
          e.printStackTrace()
        }

      }

      showReminders()

      centerCamera()
    }
  }
  private fun showConfigureLocationStep() {

  }




//  private fun addReminder(reminder: Reminder) {
//    val rep = (activity?.application as? ReminderApp)?.getRepository()
//    rep?.add(reminder,
//      success = {
//        activity?.setResult(Activity.RESULT_OK)
//        activity?.finish()
//        Toast.makeText(context, "finished", Toast.LENGTH_LONG).show()
//
//      },
//      failure = {
//        Toast.makeText(context, "failed", Toast.LENGTH_LONG).show()
//      })
//  }


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

      conn = DriverManager.getConnection(url,  un, passwords)
    } catch (se: SQLException) {
      Log.e("ERRO", se.message)
    } catch (e: ClassNotFoundException) {
      Log.e("ERRO", e.message)
    } catch (e: Exception) {
      Log.e("ERRO", e.message)
    }
    return conn
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
        removeReminder(reminder)
        dialog.dismiss()
      }
      setButton(AlertDialog.BUTTON_NEGATIVE,
        getString(R.string.reminder_removal_alert_negative)) { dialog, _ ->
        dialog.dismiss()
      }
      show()
    }
  }

  private fun removeReminder(reminder: Reminder) {
    getRepository().remove(
      reminder,
      success = {
        showReminders()
        Toast.makeText(this.requireContext(), "removed", Toast.LENGTH_SHORT).show()
      },
      failure = {
        Toast.makeText(this.requireContext(), "failed", Toast.LENGTH_SHORT).show()
      })
  }
}
