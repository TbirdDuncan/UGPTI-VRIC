package com.example.nav.ui.camera

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
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
import java.io.File


class CameraFragment: Fragment() {
    private lateinit var cameraViewModel: CameraViewModel
    var editText: EditText? = null
    private val STORAGE_PERMISSION_CODE = 23

    var fotoapparat: Fotoapparat? = null
    //var info: String = editText.getText.toString()
    val folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
    val myFile = File(folder, "myData1.jpg");// Filename


    var fotoapparatState : FotoapparatState? = null

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
        cameraViewModel =
            ViewModelProviders.of(this).get(CameraViewModel::class.java)
        val root = inflater.inflate(fragment_camera, container, false)
        val textView: TextView = root.findViewById(R.id.text_camera)
        val relativeLayout: RelativeLayout = root.findViewById(R.id.layout_camera)
        cameraViewModel.text.observe(this, Observer {
            textView.text = it
        })
        cameraViewModel.layout.observe(this, Observer {
            relativeLayout.layoutAnimation

        })
        //View(activity!!.applicationContext)


        return root
    }

    override fun onStart() {
        super.onStart()
        //if (hasNoPermissions()) {
            createFotoapparat()
            requestPermission()


            fotoapparatState = FotoapparatState.ON
            fotoapparat?.start()

            fab_camera.setOnClickListener {
                takePhoto()
            }
            fotoapparat?.takePicture()?.saveToFile(myFile)





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
    private fun takePhoto() {
        if (hasNoPermissions()) {
            requestPermission()
        } else {

        }
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
    }





}