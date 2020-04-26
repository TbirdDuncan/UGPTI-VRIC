package com.example.nav.ui.camera

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.fotoapparat.Fotoapparat

class CameraViewModel : ViewModel() {
    var fotoapparat: Fotoapparat? = null

    private val camera_text = MutableLiveData<String>().apply {

    }
    val text: LiveData<String> = camera_text
    private val camera_layout = MutableLiveData<String>().apply {

    }
    val layout: LiveData<String> = camera_layout
}