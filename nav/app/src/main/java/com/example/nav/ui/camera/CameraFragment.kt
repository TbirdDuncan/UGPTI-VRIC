package com.example.nav.ui.camera

import android.Manifest
import android.R.attr.data
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.Image
import android.media.ImageReader
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import android.provider.MediaStore.Images
import android.util.AndroidRuntimeException
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.DownloadListener
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
import io.fotoapparat.result.PhotoResult
import io.fotoapparat.selector.back
import kotlinx.android.synthetic.main.fragment_camera.*
import java.io.File
import java.io.IOError
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.time.LocalDateTime
import java.util.*


class CameraFragment: Fragment() {
    //private lateinit var cameraViewModel: CameraViewModel
    var editText: EditText? = null
    private val STORAGE_PERMISSION_CODE = 23

    var fotoapparat: Fotoapparat? = null
    //val presenter = CameraPresenter(this)
    lateinit var byteArray:ByteArray
    var encodedImage: String? = null
    var con: Connection? = null
    var un: String? = null
    var url: String? = null
    var password: String? = null
    private val clientURL =  OkHttpClient();

    //var info: String = editText.getText.toString()

    val dest = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/" + UUID.randomUUID().toString() + ".jpg")

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
        val root = inflater.inflate(fragment_camera, container, false)

        url = "jdbc:jtds:sqlserver://dotsc-data.ugpti.ndsu.nodak.edu/RIC"
        un = "RIC"
        password = "@RICsdP4T"
        return root;
    }
    private fun createFotoapparat() {

        fotoapparat = Fotoapparat(
            context = activity!!.applicationContext,
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

    override fun onStart() {
        super.onStart()
        //if (hasNoPermissions()) {
        createFotoapparat()
        requestPermission()


        fotoapparatState = FotoapparatState.ON
        fotoapparat?.start()



        fab_camera.setOnClickListener {
            Toast.makeText(context, "Test", Toast.LENGTH_LONG).show()
            var photo = fotoapparat?.takePicture()
            Toast.makeText(context, "After Photo", Toast.LENGTH_SHORT).show()
            photo?.toBitmap()
                ?.whenAvailable { bitmapPhoto ->
                    //sends image to the presenter
                    //presenter.uploadPhoto(bitmapPhoto!!.bitmap)
                    //Toast.makeText(context, "presenter", Toast.LENGTH_SHORT).show()
                    post(bitmapPhoto!!.bitmap, clientURL)             
                    //iv_photo.setImageBitmap(bitmapPhoto?.bitmap)
                    //iv_photo.setRotation(bitmapPhoto?.rotationDegrees!!.toFloat())
                }

            //?.saveToFile()

        }

    }
    
    @Throws(IOException::class)
    private fun post(photo: Bitmap, client: OkHttpClient){
        val stream = ByteArrayOutputStream()
        photo.compress(Bitmap.CompressFormat.JPEG, 50, stream)
        val byte_arr = stream.toByteArray()
        val encodedString = Base64.encodeToString(byte_arr, 0)
            var params = "http://dotsc.ugpti.ndsu.nodak.edu/RIC/upload1.php".toHttpUrlOrNull()
                ?.newBuilder()
            if (params != null) {
                params.addQueryParameter("username", "RIC")
                params.addQueryParameter("password", "@RICsdP4T")
                params.addQueryParameter("id", "333335")
                params.addQueryParameter("latitude", "47.040186")
                params.addQueryParameter("longitude", "-90")
                params.addQueryParameter("quality", "50")
                params.addQueryParameter("agency", "Ok Client")
                params.addQueryParameter("filename", "333335.jpg")
            }
            val body = encodedString.toRequestBody()
            val request= Request.Builder()
                .url(params!!.build())
                .post(body)
                .build()
        client.newCall(request).enqueue(object: Callback{
            override fun onFailure(call: Call, e: java.io.IOException) {
                e.printStackTrace()
                Toast.makeText(context, "failure", Toast.LENGTH_LONG);
            }

            override fun onResponse(call: Call, response: Response) {
            val real = response.toString()
            if("TRUE" in real)
                Toast.makeText(context, "Success", Toast.LENGTH_LONG);
            }

        }
        )};
    private fun requestPermission() {
        ActivityCompat.requestPermissions(this.requireActivity(), permissions, 0)
    }

    override fun onStop() {
        super.onStop()
        fotoapparat?.stop()
        FotoapparatState.OFF
    }

    override fun onResume() {
        super.onResume()

            fab_camera.performClick()

    }
    enum class FotoapparatState {
        ON, OFF
    }

    override fun showMessage(message: String) {
        //presents what comes back from cameraPresenter
        Toast.makeText(context, message, Toast.LENGTH_LONG)
    }


}
