package com.example.nav.ui.camera

import android.graphics.Bitmap
import com.example.nav.data.ApiService
import com.example.nav.data.UploadResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory


class UploadService(val listener: IUploadListener) {
    //the id from grit needs to be stored geofence
    fun uploadPhoto(
        username: String,
        password: String,
        id: String,
        latitude: Double,
        longitude: Double,
        quality: Int,
        agency: String,
        image: String,
        filename: String
    ) {
        //create instance of retrofit
        //establish base url
        val retrofit = Retrofit.Builder().baseUrl("http://dotsc.ugpti.ndsu.nodak.edu/").addConverterFactory(
            JacksonConverterFactory.create()).build()
        //builds rest api
        val service = retrofit.create(ApiService::class.java)
        //makes asynchronous post to URL
        service.uploadBitmap(
            username,
            password,
            id,
            latitude,
            longitude,
            quality,
            agency,
            image,
            filename
        ).enqueue(object : Callback<UploadResponse> {


            /**
             * @param call retrofit tried to execute this call, check out ApiService
             * @param t the error that retrofit gives us, if null states Unknown
             */
            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                //debug
                listener.onFailure(t.message ?: "Unknown Error Occured")
            }

            override fun onResponse(
                call: Call<UploadResponse>,
                response: Response<UploadResponse>
            ) {
                //response . body that is verified as not being null
                response.body()?.let {listener.onSuccess(it.message)}

            }

        })
    }

}

interface IUploadListener {
    fun onSuccess(message: String)
    fun onFailure(message: String)
}