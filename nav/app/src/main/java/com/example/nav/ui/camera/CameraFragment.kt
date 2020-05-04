/*
 * Copyright (c) 2018 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.example.nav.ui.camera


import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.LocationManager
import android.os.Bundle
import android.os.StrictMode
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.nav.R
import com.example.nav.data.TakePhotoEvent
import com.example.nav.ui.map.showReminderInMap
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import io.fotoapparat.Fotoapparat
import io.fotoapparat.log.logcat
import io.fotoapparat.log.loggers
import io.fotoapparat.selector.back
import io.fotoapparat.view.CameraView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_camera.*
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.ByteArrayOutputStream
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.*


class CameraFragment : BaseActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    var connect: Connection? = null
    internal var uploadURL = "http://dotsc.ugpti.ndsu.nodak.edu/RIC/upload1.php"

    var arraylist: ArrayList<HashMap<String, String>>? = null
    lateinit var btnSchedule: Button


    var fotoapparat: Fotoapparat? = null

    //val presenter = CameraPresenter(this)

    private val clientURL = OkHttpClient();


    lateinit var byteArray: ByteArray

    var encodedImage: String? = null

    var con: Connection? = null

    var un: String? = null

    var url: String? = null

    var password: String? = null


    private var map: GoogleMap? = null

    private lateinit var locationManager: LocationManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_camera, container, false)
        //setContentView(R.layout.activity_main)

        val mapFragment = childFragmentManager.findFragmentById(R.id.Camera) as SupportMapFragment

        mapFragment.getMapAsync(this)



        locationManager =
            activity!!.applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager






        url = "jdbc:jtds:sqlserver://dotsc-data.ugpti.ndsu.nodak.edu/RIC"

        un = "RIC"

        password = "@RICsdP4T"


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

//        fun onCreateView(
//            inflater: LayoutInflater,
//            container: ViewGroup?,
//            savedInstanceState: Bundle?
//        ): View? {
//
//            val root = inflater.inflate(R.layout.fragment_camera, container, false)
//            //setContentView(R.layout.activity_main)
//
//            val cam = root.findViewById<CameraView>(R.id.camera_view)
//
//
//            return root
//        }


    }

    fun takePhoto() {
        createFotoapparat()

        requestPermission()

        fotoapparatState = FotoapparatState.ON
        fotoapparat?.start()
        //       Toast.makeText(context, "Test", Toast.LENGTH_LONG).show()
        var photo = fotoapparat?.takePicture()
        //      Toast.makeText(context, "After Photo", Toast.LENGTH_SHORT).show()
        photo?.toBitmap()

            ?.whenAvailable { bitmapPhoto ->

                    post(bitmapPhoto!!.bitmap, clientURL)

            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cameraview = view.findViewById<CameraView>(R.id.camera_view)
    }

    fun CreateFoto() {

        fotoapparat = Fotoapparat(
            context = GeofenceTransitionsJobIntentService(),
            view = GeofenceTransitionsJobIntentService.takephoto.onCreateView(
                LayoutInflater.from(
                    GeofenceTransitionsJobIntentService()
                ), container
            ),

            scaleType = io.fotoapparat.parameter.ScaleType.CenterCrop,

            lensPosition = back(),

            logger = loggers(

                logcat()

            ),

            cameraErrorCallback = { error ->

                println("Recorder errors: $error")

            }

        )

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

            val queryCounty = "SELECT DISTINCT Lat, Long FROM Agency"

            val stmtCounty = connect!!.prepareStatement(queryCounty)

            val rsCounty = stmtCounty.executeQuery()



            while (rsCounty.next()) {

                val idLat = rsCounty.getDouble("Lat")
                val idLong = rsCounty.getDouble("Long")
                val id: String = UUID.randomUUID().toString()
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

            val queryCounty = "SELECT DISTINCT Lat, Long FROM Agency"

            val stmtCounty = connect!!.prepareStatement(queryCounty)

            val rsCounty = stmtCounty.executeQuery()


            while (rsCounty.next()) {

                val idLat = rsCounty.getDouble("Lat")
                val idLong = rsCounty.getDouble("Long")
                val id: String = UUID.randomUUID().toString()
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


    private fun createFotoapparat() {


        fotoapparat = Fotoapparat(
            context = this.requireContext(),
            view = camera_view,

            scaleType = io.fotoapparat.parameter.ScaleType.CenterCrop,

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
        EventBus.getDefault().register(this)

        //if (hasNoPermissions()) {

        createFotoapparat()

        requestPermission()
        val event: GeofencingEvent? = null
//        if (event?.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
//            val reminder = getFirstReminder(event.triggeringGeofences)
//            val message = reminder?.message
//            val latLng = reminder?.latLng
//            fab_camera.performClick()
//
//
//        }


        fotoapparatState = FotoapparatState.ON

        fotoapparat?.start()

        fab_camera.setOnClickListener {

            Toast.makeText(context, "Test", Toast.LENGTH_LONG).show()

            var photo = fotoapparat?.takePicture()

            Toast.makeText(context, "After Photo", Toast.LENGTH_SHORT).show()

            photo?.toBitmap()

                ?.whenAvailable { bitmapPhoto ->

                    //sends image to the presenter

                    //presenter.uploadMetaData1(bitmapPhoto!!.bitmap)

//                  uploadImage(photo)

                    //OkHttpClient attempt


                    //okhttpFunction(MediaType, encodedString, client)

                    post(bitmapPhoto!!.bitmap, clientURL)

                    Toast.makeText(context, "presenter", Toast.LENGTH_SHORT).show()

                    //iv_photo.setImageBitmap(bitmapPhoto?.bitmap)

                    //iv_photo.setRotation(bitmapPhoto?.rotationDegrees!!.toFloat())

                }

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

    private fun post(photo: Bitmap, client: OkHttpClient) {

        val stream = ByteArrayOutputStream()

        //look into more

        photo.compress(Bitmap.CompressFormat.JPEG, 50, stream)

        val byte_arr = stream.toByteArray()

        val encodedString = Base64.encodeToString(byte_arr, 0)

        var params = "http://dotsc.ugpti.ndsu.nodak.edu/RIC/upload1.php".toHttpUrlOrNull()

            ?.newBuilder()

        if (params != null) {

            params.addQueryParameter("username", "RIC")

            params.addQueryParameter("password", "@RICsdP4T")

            params.addQueryParameter("id", "55555")

            params.addQueryParameter("latitude", "47.040186")

            params.addQueryParameter("longitude", "-90")

            params.addQueryParameter("quality", "50")

            params.addQueryParameter("agency", "Ok Client")

            params.addQueryParameter("filename", "55555.jpg")

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

            }


        }

        )
    };


    interface IUploadListener {

        fun onSuccess(message: String)

        fun onFailure(message: String)

    }


    //}

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


    override fun onStop() {

        super.onStop()
        EventBus.getDefault().unregister(this)




        fotoapparat?.stop()

        FotoapparatState.OFF

    }


    override fun onResume() {

        super.onResume()

        if (!hasNoPermissions() && fotoapparatState == FotoapparatState.OFF) {

            val intent = Intent(activity!!.applicationContext, CameraFragment::class.java)

            startActivity(intent)


        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onEvent(event: TakePhotoEvent) {
        //EventBus.getDefault().removeStickyEvent(event::class.java)
        takePhoto()
    }


    enum class FotoapparatState {

        ON, OFF

    }
}
