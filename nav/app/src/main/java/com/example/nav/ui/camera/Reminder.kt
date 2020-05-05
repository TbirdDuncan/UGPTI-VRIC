package com.example.nav.ui.camera

import com.google.android.gms.maps.model.LatLng
import java.util.*

data class Reminder(val id: String?,
                    var latLng: LatLng? = null,
                    var radius: Double?,
                    var message: String?)