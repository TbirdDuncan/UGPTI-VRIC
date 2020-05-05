package com.example.nav.ui.camera


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
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
import android.widget.Spinner
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.nav.R
import com.example.nav.data.TakePhotoEvent
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import io.fotoapparat.Fotoapparat
import io.fotoapparat.log.logcat
import io.fotoapparat.log.loggers
import io.fotoapparat.selector.back
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
    var fotoapparat: Fotoapparat? = null
    private val clientURL = OkHttpClient();
    var un: String? = null
    var url: String? = null
    var password: String? = null
    private var map: GoogleMap? = null
    private lateinit var locationManager: LocationManager
    var spinnercounty: Spinner? = null
    lateinit var dropdown: View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_camera, container, false)
        val mapFragment = childFragmentManager.findFragmentById(R.id.Camera) as SupportMapFragment
        mapFragment.getMapAsync(this)
        val rootView: View =
            inflater.inflate(R.layout.fragment_setting, container, false)
        dropdown = rootView.findViewById<View>(R.id.spinner2)
        spinnercounty = dropdown as Spinner
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
    }

    @SuppressLint("NewApi")
    fun takePhoto(siteID : String?){
        createFotoapparat()

        requestPermission()

        fotoapparatState = FotoapparatState.ON
        fotoapparat?.start()
        val photo = fotoapparat?.takePicture()
        Toast.makeText(this.requireContext(), "Photo Captured", Toast.LENGTH_SHORT).show()
        photo?.toBitmap()

            ?.whenAvailable { bitmapPhoto ->

                post(bitmapPhoto!!.bitmap, clientURL, siteID)

            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == NEW_REMINDER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val reminder = getRepository().getLast()
            map?.moveCamera(CameraUpdateFactory.newLatLngZoom(reminder?.latLng, 15f))
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
            showConfigureLocationStep()

        }
    }

    val urlGrit = "jdbc:jtds:sqlserver://dotsc-data.ugpti.ndsu.nodak.edu/GRIT_Test"
    val unGrit = "AssetManagement"
    val passwordGrit = "gem7Nuwe"
    private fun showConfigureLocationStep() {
        try {
            connect = CONN(urlGrit, unGrit, passwordGrit)
            val queryCounty = "SELECT DISTINCT ID, Lat, Long FROM Agency"
            val stmtCounty = connect!!.prepareStatement(queryCounty)
            val rsCounty = stmtCounty.executeQuery()
            while (rsCounty.next()) {
                val idLat = rsCounty.getDouble("Lat")
                val idLong = rsCounty.getDouble("Long")
                val id = rsCounty.getString("ID")
                val latLng = LatLng(idLat, idLong)
                val reminder = Reminder(
                    id,
                    latLng = null,
                    message = "Taking Photo",
                    radius = 50.00
                )
                reminder.latLng = latLng
                getRepository().remove()
            }

        } catch (e: SQLException) {
            e.printStackTrace()
        }
        try {
            connect = CONN(urlGrit, unGrit, passwordGrit)
            val queryCounty = "SELECT DISTINCT ID, Lat, Long FROM Agency"
            val stmtCounty = connect!!.prepareStatement(queryCounty)
            val rsCounty = stmtCounty.executeQuery()
            while (rsCounty.next()) {
                val idLat = rsCounty.getDouble("Lat")
                val idLong = rsCounty.getDouble("Long")
                val id = rsCounty.getString("ID")
                val latLng = LatLng(idLat, idLong)
                val reminder = Reminder(
                    id,
                    latLng = null,
                    message = "Taking Photo",
                    radius = 50.00
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
        return true
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


    @SuppressLint("NewApi")
    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
        createFotoapparat()
        requestPermission()
        fotoapparatState = FotoapparatState.ON
        fotoapparat?.start()
        fab_camera.setOnClickListener {
            val siteID:String? = null
            val photo = fotoapparat?.takePicture()
            Toast.makeText(context, "Photo Captured", Toast.LENGTH_SHORT).show()
            photo?.toBitmap()
                ?.whenAvailable { bitmapPhoto ->
                    post(bitmapPhoto!!.bitmap, clientURL, siteID)
                 }
        }
    }

    @SuppressLint("NewApi")
    @Throws(IOException::class)

    private fun post(photo: Bitmap, client: OkHttpClient, siteID: String?) {

        val stream = ByteArrayOutputStream()

        //look into more

        photo.compress(Bitmap.CompressFormat.JPEG, 50, stream)

        val byte_arr = stream.toByteArray()

        val encodedString = Base64.encodeToString(byte_arr, 0)

        var params = "http://dotsc.ugpti.ndsu.nodak.edu/VRIC/upload1.php".toHttpUrlOrNull()?.newBuilder()
        val current = System.currentTimeMillis().toString()
        if (params != null) {

            params.addQueryParameter("username", "RIC")
            params.addQueryParameter("password", "@RICsdP4T")
            params.addQueryParameter("id", current)
            params.addQueryParameter("latitude", "47.040186")
            params.addQueryParameter("longitude", "-90")
            params.addQueryParameter("quality", "50")
            params.addQueryParameter("agency", "Ok Client")
            params.addQueryParameter("filename", current + ".jpg")
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
            }
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
        takePhoto(event.returnID())
    }
    enum class FotoapparatState {
        ON, OFF
    }
}
