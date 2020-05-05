package com.example.nav.ui.camera

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
import com.example.nav.data.TakePhotoEvent
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import org.greenrobot.eventbus.EventBus



class GeofenceTransitionsJobIntentService() : JobIntentService() {

  companion object takephoto{
    private const val LOG_TAG = "GeoTrIntentService"

    private const val JOB_ID = 573

    fun enqueueWork(context: Context, intent: Intent) {
      enqueueWork(
        context,
        GeofenceTransitionsJobIntentService::class.java, JOB_ID,
        intent)
    }
  }

  override fun onHandleWork(intent: Intent) {
    val geofencingEvent = GeofencingEvent.fromIntent(intent)
    if (geofencingEvent.hasError()) {
      val errorMessage = GeofenceErrorMessages.getErrorString(this,
        geofencingEvent.errorCode)
      Log.e(LOG_TAG, errorMessage)
      return
    }

    handleEvent(geofencingEvent)
  }

  private fun handleEvent(event: GeofencingEvent) {
    if (event.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
      val stickyEvent = EventBus.getDefault().getStickyEvent(TakePhotoEvent::class.java)
      EventBus.getDefault().postSticky(TakePhotoEvent(event.triggeringGeofences))
    }
  }

}