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

package com.example.nav.ui.camera

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.JobIntentService
import com.example.nav.R
import com.example.nav.data.TakePhotoEvent
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import io.fotoapparat.Fotoapparat
import io.fotoapparat.view.CameraRenderer
import io.fotoapparat.view.CameraView
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
    fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?
    ): CameraRenderer {

      val root = inflater.inflate(R.layout.fragment_camera, container, false)
      //setContentView(R.layout.activity_main)

      val mapFragment = root.findViewById<CameraView>(R.id.camera_view)


      return mapFragment
    }
  }
  fun createFotoapparat() {
    var fotoapparat: Fotoapparat? = null





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
      val reminder = getFirstReminder(event.triggeringGeofences)
      val message = reminder?.message
      val latLng = reminder?.latLng
      if (message != null && latLng != null) {
        sendNotification(this, message, latLng)
      }

      val stickyEvent = EventBus.getDefault().getStickyEvent(TakePhotoEvent::class.java)
// Better check that an event was actually posted before
      //if(stickyEvent != null) {

        EventBus.getDefault().postSticky(TakePhotoEvent(event.triggeringGeofences))
     // }

    }
  }

  private fun getFirstReminder(triggeringGeofences: List<Geofence>): Reminder? {
    val firstGeofence = triggeringGeofences[0]
    return (application as nav).getRepository().get(firstGeofence.requestId)
  }
}