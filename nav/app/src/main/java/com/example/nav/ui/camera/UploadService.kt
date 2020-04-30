package com.example.nav.ui.camera

import com.example.nav.data.ApiService
import com.example.nav.data.ImageHeaderIntercepter
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import timber.log.Timber


class UploadService(val listener: IUploadListener) {
    val retrofit: Retrofit
    init{
        val logging = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {

            override fun log(message: String) {
                Timber.tag("OkHttp").d(message)
            }
        })
        retrofit = Retrofit
        .Builder()
        .client(OkHttpClient.Builder().addInterceptor(logging).build())
        .baseUrl("http://dotsc.ugpti.ndsu.nodak.edu/")
        .addConverterFactory(JacksonConverterFactory.create())
        .build()
    }

    //the id from grit needs to be stored geofence
    fun uploadMetaData(
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
        //establish base u

        val bodyImage = createFormData("image", filename, RequestBody.create("image/*".toMediaTypeOrNull(), image))

        //builds rest api
        val service = retrofit.create(ApiService::class.java)
        //makes asynchronous post to URL
        service.upload1(
            bodyImage,
            username,
            password,
            id,
            latitude,
            longitude,
            quality,
            agency,
            filename
        ).enqueue(object : Callback<ResponseBody> {

            /**
             * @param call retrofit tried to execute this call, check out ApiService
             * @param t the error that retrofit gives us, if null states Unknown
             */
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                //debug
                listener.onFailure(t.message ?: "Unknown Error Occured")
            }

            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                //response . body that is verified as not being null
                response.body()?.let {listener.onSuccess(it.string())}
            }
        })
    }
}

interface IUploadListener {
    fun onSuccess(message: String)
    fun onFailure(message: String)
}