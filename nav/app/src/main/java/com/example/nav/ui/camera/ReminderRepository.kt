package com.example.nav.ui.camera

import android.Manifest
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings.Global.getString
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.nav.R
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_camera.*

class ReminderRepository(private val context: Context) {

  companion object {
    private const val PREFS_NAME = "ReminderRepository"
    private const val REMINDERS = "REMINDERS"
  }

  private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
  private val gson = Gson()
  private val geofencingClient = LocationServices.getGeofencingClient(context)
  private val geofencePendingIntent: PendingIntent by lazy{
    val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
    PendingIntent.getBroadcast(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT)

  }

  fun add(reminder: Reminder) {
    if (buildGeofence(reminder) != null
        && ContextCompat.checkSelfPermission(context,
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

      geofencingClient
          .addGeofences(buildGeofencingRequest(buildGeofence(reminder) as Geofence), geofencePendingIntent)
          .addOnSuccessListener {

            saveAll(getAll() + reminder)
          }

    }
  }

  private fun buildGeofence(reminder: Reminder): Geofence? {
    val latitude = reminder.latLng?.latitude
    val longitude = reminder.latLng?.longitude
    val radius = reminder.radius

    if (latitude != null && longitude != null && radius != null) {
      return Geofence.Builder()
          .setRequestId(reminder.id)
          .setCircularRegion(
              latitude,
              longitude,
              radius.toFloat()
          )
          .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
          .setExpirationDuration(Geofence.NEVER_EXPIRE)
          .build()
    }

    return null
  }

  private fun buildGeofencingRequest(geofence: Geofence): GeofencingRequest {
    return GeofencingRequest.Builder()
        .setInitialTrigger(0)
        .addGeofences(listOf(geofence))
        .build()
  }

   fun remove() {

        geofencingClient.removeGeofences(geofencePendingIntent)?.run {
            addOnSuccessListener {
            }
            addOnFailureListener {
            }
        }
    }


  private fun saveAll(list: List<Reminder>) {
    preferences
        .edit()
        .putString(REMINDERS, gson.toJson(list))
        .apply()
  }

  fun getAll(): List<Reminder> {
    if (preferences.contains(REMINDERS)) {
      val remindersString = preferences.getString(REMINDERS, null)
      val arrayOfReminders = gson.fromJson(remindersString,
          Array<Reminder>::class.java)
      if (arrayOfReminders != null) {
        return arrayOfReminders.toList()
      }
    }
    return listOf()
  }

  fun get(requestId: String?) = getAll().firstOrNull { it.id == requestId }

  fun getLast() = getAll().lastOrNull()

}