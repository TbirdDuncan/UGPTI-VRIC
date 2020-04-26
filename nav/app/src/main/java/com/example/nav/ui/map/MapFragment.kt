package com.example.nav.ui.map

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.LocationManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.example.nav.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_new_reminder.*
import kotlinx.android.synthetic.main.fragment_map.*

/**
 * A simple [Fragment] subclass.
 */
class MapFragment : Fragment() { //Somehow, I have to put BaseActivity() into this function, but it cannot coexist with Fragment()..."Only one class may appear in a supertype list"

    private var map: GoogleMap? = null


    companion object {
        public const val MY_LOCATION_REQUEST_CODE = 329
        public const val NEW_REMINDER_REQUEST_CODE = 330
        private const val EXTRA_LAT_LNG = "EXTRA_LAT_LNG"

        fun newIntent(context: Context, latLng: LatLng): Intent {
            val intent = Intent(context, MapFragment::class.java)
            intent.putExtra(EXTRA_LAT_LNG, latLng)
            return intent
        }
    }

    private lateinit var locationManager: LocationManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

    val root = inflater.inflate(R.layout.fragment_map, container, false)
        //setContentView(R.layout.activity_main)

//            val mapFragment = supportFragmentManager
//                .findFragmentById(R.id.navigation_map) as SupportMapFragment
//            mapFragment.getMapAsync(this)


//            newReminder.visibility = View.GONE
//            currentLocation.visibility = View.GONE
//        newReminder.setOnClickListener {
//            map?.run {
//                val intent = NewReminderActivity.newIntent(
//                    activity!!.applicationContext, //ui.map.MapFragmentBaseActivity2 ...cannot figure out where this is coming from??
//                    cameraPosition.target,
//                    cameraPosition.zoom
//                )
//                startActivityForResult(intent, BaseActivity2.NEW_REMINDER_REQUEST_CODE)
//            }
//        }
//
//
//
//        if (ContextCompat.checkSelfPermission(
//                activity!!.applicationContext,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            )
//            != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                this.requireActivity(),
//                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                BaseActivity2.MY_LOCATION_REQUEST_CODE
//            )
//        }
        //setContentView(R.layout.activity_main)

//        val mapFragment = supportFragmentManager
//            .findFragmentById(R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync(OnMapReadyCallback(map as (GoogleMap) ->Unit))

        newReminder.visibility = View.GONE
        currentLocation.visibility = View.GONE
        newReminder.setOnClickListener {
            map?.run {
                val intent = NewReminderActivity.newIntent(
                    activity!!.applicationContext,
                    cameraPosition.target,
                    cameraPosition.zoom)
                startActivityForResult(intent, NEW_REMINDER_REQUEST_CODE)
            }
        }

        locationManager = (Context.LOCATION_SERVICE) as LocationManager

        if (ContextCompat.checkSelfPermission(
                activity!!.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this.requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MY_LOCATION_REQUEST_CODE)
        }


        // Inflate the layout for this fragment

        return root
    }
//}

    class BaseActivity2 : BaseActivity(), OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener {  //I added in this line to try to compensate? Also some brackets were modified.


        lateinit var context: Context

        private var map: GoogleMap? = null
        private lateinit var locationManager: LocationManager


        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == NEW_REMINDER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                showReminders()



                val reminder = getRepository().getLast()
                map?.moveCamera(CameraUpdateFactory.newLatLngZoom(reminder?.latLng, 15f))

                Snackbar.make(main, R.string.reminder_added_success, Snackbar.LENGTH_LONG).show()
            }
        }

        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            if (requestCode == MY_LOCATION_REQUEST_CODE) {
                onMapAndPermissionReady()
            }
        }

        private fun onMapAndPermissionReady() {
            if (map != null
                && ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                map?.isMyLocationEnabled = true
                newReminder.visibility = View.VISIBLE
                currentLocation.visibility = View.VISIBLE

                currentLocation.setOnClickListener {
                    val bestProvider = locationManager.getBestProvider(Criteria(), false)
                    val location = locationManager.getLastKnownLocation(bestProvider)
                    if (location != null) {
                        val latLng = LatLng(location.latitude, location.longitude)
                        map?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                    }
                }

                showReminders()

                centerCamera()
            }
        }

        private fun centerCamera() {
            if (intent.extras != null && intent.extras!!.containsKey(EXTRA_LAT_LNG)) {
                val latLng = intent.extras!!.get(EXTRA_LAT_LNG) as LatLng
                map?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
            }
        }

        private fun showReminders() {
            map?.run {
                clear()
                for (reminder in getRepository().getAll()) {
                    showReminderInMap(context, this, reminder)
                }
            }
        }

        override fun onMapReady(googleMap: GoogleMap) {
            map = googleMap
            map?.run {
                uiSettings.isMyLocationButtonEnabled = false
                uiSettings.isMapToolbarEnabled = false
                //setOnMarkerClickListener(this@MainActivity)   //this@MainActivity
            }

            onMapAndPermissionReady()
        }

        override fun onMarkerClick(marker: Marker): Boolean {
            val reminder = getRepository().get(marker.tag as String)

            if (reminder != null) {
                showReminderRemoveAlert(reminder)
            }

            return true
        }

        private fun showReminderRemoveAlert(reminder: Reminder) {
            val alertDialog = AlertDialog.Builder(this).create()
            alertDialog.run {
                setMessage(getString(R.string.reminder_removal_alert))
                setButton(
                    AlertDialog.BUTTON_POSITIVE,
                    getString(R.string.reminder_removal_alert_positive)
                ) { dialog, _ ->
                    removeReminder(reminder)
                    dialog.dismiss()
                }
                setButton(
                    AlertDialog.BUTTON_NEGATIVE,
                    getString(R.string.reminder_removal_alert_negative)
                ) { dialog, _ ->
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
                    Snackbar.make(main, R.string.reminder_removed_success, Snackbar.LENGTH_LONG)
                        .show()
                },
                failure = {
                    Snackbar.make(main, it, Snackbar.LENGTH_LONG).show()
                })
        }


    }
}
