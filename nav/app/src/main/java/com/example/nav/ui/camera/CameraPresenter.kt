package com.example.nav.ui.camera

import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.random.Random

//Connects Upload Service to CameraFragment
class CameraPresenter(val view: ICameraView) : IUploadListener {
    //instantiate uploadService
    private val uploadService = UploadService(this)
    //uploads the photo

    fun uploadMetaData1(photo: Bitmap) {

        val stream = ByteArrayOutputStream()
        //look into more
        photo.compress(Bitmap.CompressFormat.JPEG, 50, stream)
        val byte_arr = stream.toByteArray()
        val encodedString = Base64.encodeToString(byte_arr, 0)
        uploadService.uploadMetaData(
            "RIC",
            "@RICsdP4T",
            "2222228",
            47.040186,
            -90.0,
            50,
            "North Dakota",
            encodedString,
            "2222228.jpg"
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