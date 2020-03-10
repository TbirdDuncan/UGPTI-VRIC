package com.example.nav.ui.camera

import android.Manifest
import android.R.attr.data
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.nav.R
import com.example.nav.R.layout.fragment_camera
import io.fotoapparat.Fotoapparat
import io.fotoapparat.log.logcat
import io.fotoapparat.log.loggers
import io.fotoapparat.parameter.ScaleType
import io.fotoapparat.selector.back
import kotlinx.android.synthetic.main.fragment_camera.*
import java.io.*
import java.net.URL
import java.net.URLConnection


class CameraFragment : Fragment(), ICameraView {
    private lateinit var cameraViewModel: CameraViewModel
    var editText: EditText? = null
    private val STORAGE_PERMISSION_CODE = 23

    var fotoapparat: Fotoapparat? = null
    //var info: String = editText.getText.toString()
    val filename = "test.png"
    val sd = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    //val dest = File(sd, filename)
    var fotoapparatState: FotoapparatState? = null
    val presenter = CameraPresenter(this)

    val permissions = arrayOf(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    )


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(fragment_camera, container, false)
        return root;
    }

    override fun onStart() {
        super.onStart()
        //if (hasNoPermissions()) {
        createFotoapparat()
        requestPermission()


        fotoapparatState = FotoapparatState.ON
        fotoapparat?.start()
        Toast.makeText(context, "Test foto", Toast.LENGTH_LONG).show()


        fab_camera.setOnClickListener {
            Toast.makeText(context, "Test listener", Toast.LENGTH_LONG).show()
            takePhoto()
        }


    }

    private fun createFotoapparat() {

        fotoapparat = Fotoapparat(
            context = this.requireContext(),
            view = camera_view,
            scaleType = ScaleType.CenterCrop,
            lensPosition = back(),
            logger = loggers(
                logcat()
            ),
            cameraErrorCallback = { error ->
                println("Recorder errors: $error")
            }
        )
    }


    private fun hasNoPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            this.requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
            this.requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
            this.requireContext(),
            Manifest.permission.CAMERA
        ) != PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this.requireActivity(), permissions, 0)
    }

    enum class FotoapparatState {
        ON, OFF
    }


    override fun onStop() {
        super.onStop()
        fotoapparat?.stop()
        FotoapparatState.OFF
    }

    override fun onResume() {
        super.onResume()
        if (!hasNoPermissions() && fotoapparatState == FotoapparatState.OFF) {
            val intent = Intent(activity!!.applicationContext, fragment_camera::class.java)
            startActivity(intent)
            //finish()
        }
    }

    fun takePhoto() {
        Toast.makeText(context, "Test", Toast.LENGTH_LONG).show()
        val photo = fotoapparat?.takePicture()
        Toast.makeText(context, "After Photo", Toast.LENGTH_LONG).show()
        photo?.toBitmap()
            ?.whenAvailable { bitmapPhoto ->
                //sends image to the presenter
                presenter.uploadPhoto(bitmapPhoto!!.bitmap)
                iv_photo.setImageBitmap(bitmapPhoto?.bitmap)
                iv_photo.setRotation(bitmapPhoto?.rotationDegrees!!.toFloat())
            }
        //?.saveToFile()

    }

    override fun showMessage(message: String) {
        //presents what comes back from cameraPresenter
        Toast.makeText(context, message, Toast.LENGTH_LONG)
    }

}