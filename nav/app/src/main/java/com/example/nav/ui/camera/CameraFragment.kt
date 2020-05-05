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
<<<<<<< Updated upstream
=======
import io.fotoapparat.view.CameraView
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
        return root;
    }
=======


        return root
    }

    companion object {
        private const val MY_LOCATION_REQUEST_CODE = 329
        private const val NEW_REMINDER_REQUEST_CODE = 330
        private const val EXTRA_LAT_LNG = "EXTRA_LAT_LNG"
        fun newIntent(context: Context, latLng: LatLng): Intent {
            val intent = Intent(context, CameraFragment::class.java)
            intent.putExtra(EXTRA_LAT_LNG, latLng)

            return intent
        }
    }

    fun takePhoto(siteID : String?) {
        createFotoapparat()

        requestPermission()

        fotoapparatState = FotoapparatState.ON
        fotoapparat?.start()
        //       Toast.makeText(context, "Test", Toast.LENGTH_LONG).show()
        var photo = fotoapparat?.takePicture()
        //      Toast.makeText(context, "After Photo", Toast.LENGTH_SHORT).show()
        photo?.toBitmap()

            ?.whenAvailable { bitmapPhoto ->
                    post(bitmapPhoto!!.bitmap, clientURL, siteID)
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cameraview = view.findViewById<CameraView>(R.id.camera_view)
    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == NEW_REMINDER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            showReminders()

            val reminder = getRepository().getLast()
            map?.moveCamera(CameraUpdateFactory.newLatLngZoom(reminder?.latLng, 15f))

            Toast.makeText(
                this.requireContext(),
                R.string.reminder_added_success,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            onmapAndPermissionReady()
        }
    }

    private fun onmapAndPermissionReady() {
        if (map != null
            && ContextCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            map?.isMyLocationEnabled = true
            //newReminder.visibility = View.VISIBLE

            showReminders()
            showConfigureLocationStep()
            centerCamera()
        }
    }

    val urlGrit = "jdbc:jtds:sqlserver://dotsc-data.ugpti.ndsu.nodak.edu/GRIT_Test"
    val unGrit = "AssetManagement"
    val passwordGrit = "gem7Nuwe"
    private fun showConfigureLocationStep() {
        try {
            connect = CONN(urlGrit, unGrit, passwordGrit)

            val queryCounty = "SELECT DISTINCT Id, Lat, Long FROM Agency"

            val stmtCounty = connect!!.prepareStatement(queryCounty)

            val rsCounty = stmtCounty.executeQuery()

            while (rsCounty.next()) {

                val idLat = rsCounty.getDouble("Lat")
                val idLong = rsCounty.getDouble("Long")
                val id = rsCounty.getString("Id")
                val latLng = LatLng(idLat, idLong)
                val reminder = Reminder(
                    id,
                    latLng = null,
                    message = "Taking Photo",
                    radius = 200.00
                )
                reminder.latLng = latLng
                getRepository().remove()

            }

        } catch (e: SQLException) {
            e.printStackTrace()
        }
        try {
            connect = CONN(urlGrit, unGrit, passwordGrit)

            val queryCounty = "SELECT DISTINCT Id, Lat, Long FROM Agency"

            val stmtCounty = connect!!.prepareStatement(queryCounty)

            val rsCounty = stmtCounty.executeQuery()


            while (rsCounty.next()) {

                val idLat = rsCounty.getDouble("Lat")
                val idLong = rsCounty.getDouble("Long")
                val id = rsCounty.getString("Id")
                val latLng = LatLng(idLat, idLong)
                val reminder = Reminder(
                    id,
                    latLng = null,
                    message = "Taking Photo",
                    radius = 200.00
                )
                reminder.latLng = latLng

                addReminder(reminder)

            }
            Toast.makeText(this.requireContext(), "Geofences added", Toast.LENGTH_SHORT).show()

        } catch (e: SQLException) {
            e.printStackTrace()
        }


    }

    private fun addReminder(reminder: Reminder) {
        getRepository().add(reminder)
    }


    private fun centerCamera() {
        fun newIntent(context: Context, latLng: LatLng): Intent {
            val intent = Intent(context, MapFragment::class.java)
            intent.putExtra(EXTRA_LAT_LNG, latLng)

            if (intent.extras != null && intent.extras!!.containsKey(EXTRA_LAT_LNG)) {
                val latLng = intent.extras!!.get(EXTRA_LAT_LNG) as LatLng
                map?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
            }
            return intent
        }
    }

    private fun showReminders() {
        map?.run {
            clear()
            val rep = getRepository().getAll()

            for (reminder in rep) {
                showReminderInMap(activity!!.applicationContext, this, reminder)
            }

        }
    }

    override fun onMapReady(googlemap: GoogleMap) {
        map = googlemap
        map?.run {
            uiSettings.isMyLocationButtonEnabled = false
            uiSettings.isMapToolbarEnabled = false
            setOnMarkerClickListener(this@CameraFragment)
        }

        onmapAndPermissionReady()
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val reminder = getRepository().get(marker.tag as String)

        if (reminder != null) {
            showReminderRemoveAlert(reminder)
        }

        return true
    }

    private fun showReminderRemoveAlert(reminder: Reminder) {
        val alertDialog = AlertDialog.Builder(this.requireContext()).create()
        alertDialog.run {
            setMessage(getString(R.string.reminder_removal_alert))
            setButton(
                AlertDialog.BUTTON_POSITIVE,
                getString(R.string.reminder_removal_alert_positive)
            ) { dialog, _ ->
                dialog.dismiss()
            }
            setButton(
                AlertDialog.BUTTON_NEGATIVE,
                getString(R.string.reminder_removal_alert_negative)
            ) { dialog, _ ->
                dialog.dismiss()
            }
            show()
        }
    }


    private fun CONN(
        _user: String, _pass: String, _url: String
    ): Connection? {
        val policy = StrictMode.ThreadPolicy.Builder()
            .permitAll().build()
        StrictMode.setThreadPolicy(policy)
        var con: Connection? = null

        try {

            Class.forName("net.sourceforge.jtds.jdbc.Driver")
            con = DriverManager.getConnection(urlGrit, unGrit, passwordGrit)


        } catch (se: SQLException) {
            Log.e("ERRO", se.message)
        } catch (e: ClassNotFoundException) {
            Log.e("ERRO", e.message)
        } catch (e: Exception) {
            Log.e("ERRO", e.message)
        }
        return con
    }

    //camera
    var editText: EditText? = null

    private val STORAGE_PERMISSION_CODE = 2


    //new upload stuff


    //var info: String = editText.getText.toString()


    var fotoapparatState: FotoapparatState? = null


    val permissions = arrayOf(

        android.Manifest.permission.CAMERA,

        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,

        android.Manifest.permission.READ_EXTERNAL_STORAGE,

        android.Manifest.permission.WAKE_LOCK,

        android.Manifest.permission.ACCESS_FINE_LOCATION

    )


>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
                    presenter.uploadPhoto(bitmapPhoto!!.bitmap)
=======

                    //presenter.uploadMetaData1(bitmapPhoto!!.bitmap)

//                  uploadImage(photo)

                    //OkHttpClient attempt


                    //okhttpFunction(MediaType, encodedString, client)

                    post(bitmapPhoto!!.bitmap, clientURL, null)

>>>>>>> Stashed changes
                    Toast.makeText(context, "presenter", Toast.LENGTH_SHORT).show()
                    iv_photo.setImageBitmap(bitmapPhoto?.bitmap)
                    iv_photo.setRotation(bitmapPhoto?.rotationDegrees!!.toFloat())
                }
<<<<<<< Updated upstream
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
=======

            //var msg = "unknown"


        }


    }

    fun getFirstReminder(triggeringGeofences: List<Geofence>): Reminder? {
        val firstGeofence = triggeringGeofences[0]
        return (getActivity()?.application as nav).getRepository().get(firstGeofence.requestId)
    }


//I was working on this

//    private fun uploadImage(path: String) {

//        val map = HashMap<String, String>()

//        map.put("url", "http://dotsc.ugpti.ndsu.nodak.edu/RIC/upload1.php")

//        map.put("filename", path)

//        MultiPartRequester(this, map, this)

//    }


    //"RIC",

    //            "@RICsdP4T",

    //            "2222228",

    //            47.040186,

    //            -90.0,

    //            50,

    //            "North Dakota",

    //            encodedString,

    //            "2222228.jpg"


    //class okhttpFunction(val mediaType: MediaType, val photo: String, val client: OkHttpClient) {

    @Throws(IOException::class)

    private fun post(
        photo: Bitmap,
        client: OkHttpClient,
        siteID: String?
    ) {

        val stream = ByteArrayOutputStream()

        photo.compress(Bitmap.CompressFormat.JPEG, 50, stream)

        val byte_arr = stream.toByteArray()

        val encodedString = Base64.encodeToString(byte_arr, 0)

        var params = "http://dotsc.ugpti.ndsu.nodak.edu/VRIC/".toHttpUrlOrNull()

            ?.newBuilder()
        val current = System.currentTimeMillis().toString()

        if (params != null) {

            params.addQueryParameter("username", "RIC")

            params.addQueryParameter("password", "@RICsdP4T")

            params.addQueryParameter("id", current)

            params.addQueryParameter("latitude", "47.040186")

            params.addQueryParameter("longitude", "-90")

            params.addQueryParameter("quality", "50")

            params.addQueryParameter("agency", "Ok Client")

            params.addQueryParameter("filename", current+".jpg")

            params.addQueryParameter("siteID", siteID)

        }

        val body = encodedString.toRequestBody()

        val request = Request.Builder()

            .url(params!!.build())

            .post(body)

            .build()

        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: java.io.IOException) {

                e.printStackTrace()

                Toast.makeText(context, "failure", Toast.LENGTH_LONG);

            }


            override fun onResponse(call: Call, response: Response) {

                val real = response.toString()

                if ("TRUE" in real)

                    Toast.makeText(context, "Success", Toast.LENGTH_LONG);

>>>>>>> Stashed changes
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
        if(!hasNoPermissions() && fotoapparatState == FotoapparatState.OFF){
            val intent = Intent(activity!!.applicationContext, fragment_camera::class.java)
            startActivity(intent)

        }
    }
<<<<<<< Updated upstream
=======

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onEvent(event: TakePhotoEvent) {
        //EventBus.getDefault().removeStickyEvent(event::class.java)
        takePhoto(event.returnID())
    }


>>>>>>> Stashed changes
    enum class FotoapparatState {
        ON, OFF
    }

    override fun showMessage(message: String) {
        //presents what comes back from cameraPresenter
        Toast.makeText(context, message, Toast.LENGTH_LONG)
    }


}