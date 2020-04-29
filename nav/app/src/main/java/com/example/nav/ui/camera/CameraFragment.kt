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


class CameraFragment: Fragment(), ICameraView {
    private lateinit var cameraViewModel: CameraViewModel
    var editText: EditText? = null
    private val STORAGE_PERMISSION_CODE = 23

    var fotoapparat: Fotoapparat? = null
    val presenter = CameraPresenter(this)
    lateinit var byteArray:ByteArray
    var encodedImage: String? = null
    var con: Connection? = null
    var un: String? = null
    var url: String? = null
    var password: String? = null


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
                    presenter.uploadPhoto(bitmapPhoto!!.bitmap)
                    Toast.makeText(context, "presenter", Toast.LENGTH_SHORT).show()
                    iv_photo.setImageBitmap(bitmapPhoto?.bitmap)
                    iv_photo.setRotation(bitmapPhoto?.rotationDegrees!!.toFloat())
                }
            var msg = "unknown"
            try {
                con = connectionclass(un, password, url)
                var currentDateTime = LocalDateTime.now()

                val commands =
                    "Insert into VirtualCapture(SiteID, CollectionDateTime, FilePath, Quality, AgencyName) values ('7','$currentDateTime','$photo','2','UGPTI')"
                var preStmt = con!!.prepareStatement(commands)
                preStmt.executeUpdate()
                msg = "Inserted Successfully"
            } catch (ex: SQLException) {
                msg = ex.message.toString()
                Log.d("Error no 1:", msg)
            } catch (ex: IOError) {
                msg = ex.message.toString()
                Log.d("Error no 2:", msg)
            } catch (ex: AndroidRuntimeException) {
                msg = ex.message.toString()
                Log.d("Error no 3:", msg)
            } catch (ex: NullPointerException) {
                msg = ex.message.toString()
                Log.d("Error no 4:", msg)
            } catch (ex: Exception) {
                msg = ex.message.toString()
                Log.d("Error no 5:", msg)
            }
            //?.saveToFile()


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
    fun connectionclass(
        user: String?,
        passwords: String?,
        urls: String?
    ): Connection? {
        val policy =
            StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        var connection: Connection? = null
        var ConnectionURL: String? = null
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver")

            connection = DriverManager.getConnection(url,  un, password)
        } catch (se: SQLException) {
            Log.e("error no 1", se.message)
        } catch (e: ClassNotFoundException) {
            Log.e("error no 2", e.message)
        } catch (e: Exception) {
            Log.e("error no 3", e.message)
        }
        return connection
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