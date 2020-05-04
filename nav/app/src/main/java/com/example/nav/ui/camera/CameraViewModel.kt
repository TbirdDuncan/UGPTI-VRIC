package com.example.nav.ui.camera

import android.graphics.Camera
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nav.R
import io.fotoapparat.Fotoapparat
import io.fotoapparat.view.CameraRenderer
import io.fotoapparat.view.CameraView
import kotlinx.android.synthetic.main.fragment_camera.*

class CameraViewModel : ViewModel() {
    var fotoapparat: Fotoapparat? = null

    private val _camview = MutableLiveData<CameraRenderer>().apply {
    }
    val camview: LiveData<CameraRenderer> = _camview

}