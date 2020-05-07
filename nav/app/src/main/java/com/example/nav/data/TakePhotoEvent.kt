package com.example.nav.data

import android.location.Location
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class TakePhotoEvent(val triggeringGeofence: GeofencingEvent) {
    fun returnID(): Triple<String?, Double, Double>{
        var siteID: String? = null
        var  location = triggeringGeofence.triggeringLocation
        var lat:Double
        var long:Double
        lat = location.latitude
        long = location.longitude
        if(triggeringGeofence != null) {
            var listOfGeo = triggeringGeofence.triggeringGeofences
            siteID = listOfGeo.last().requestId
        }
        return Triple(siteID,lat, long)
    }
}