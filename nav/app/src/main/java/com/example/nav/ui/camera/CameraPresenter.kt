package com.example.nav.ui.camera

import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.util.*

//Connects Upload Service to CameraFragment
class CameraPresenter(val view: ICameraView) : IUploadListener {
    //instantiate uploadService
    private val uploadService = UploadService(this)
    //uploads the photo

    fun uploadPhoto(photo: Bitmap) {
        val stream = ByteArrayOutputStream()
        //look into more
        photo.compress(Bitmap.CompressFormat.PNG, 2, stream)
        val byte_arr = stream.toByteArray()
        val encodedString = Base64.encodeToString(byte_arr, 0)
        uploadService.uploadPhoto(
            "RIC",
            "@RICsdP4T",
            System.currentTimeMillis().toString(),
            0.0,
            0.0,
            2,
            "North Dakota",
            encodedString,
            "${System.currentTimeMillis()}.jpg"
        )
    }

    override fun onSuccess(message: String) {
        view.showMessage(message)
    }

    override fun onFailure(message: String) {
        view.showMessage(message)
    }
}

interface ICameraView {
    fun showMessage(message: String)
}