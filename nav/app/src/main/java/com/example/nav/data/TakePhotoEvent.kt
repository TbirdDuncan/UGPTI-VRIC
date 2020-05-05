package com.example.nav.data

import com.google.android.gms.location.Geofence

class TakePhotoEvent(val triggeringGeofences: MutableList<Geofence>) {
    fun returnID(): String?{
        var siteID: String? = null
        if(triggeringGeofences != null){
            siteID = triggeringGeofences.last().requestId
        }
        return siteID
    }
}