package com.example.nav.data

import android.graphics.Bitmap
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface ApiService {


    @Multipart
    @POST("RIC/upload1.php")
    open fun upload1(
        @Part image:MultipartBody.Part,
        @Query("username") username: String,
        @Query("password")password: String,
        @Query("id") id:  String,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("quality") quality: Int,
        @Query("agency") agency: String,
        @Query("filename") filename: String
    ): Call<ResponseBody>}
//    fun uploadMetaData(
//        @Part("username") username: String,
//        @Part("password") password: String,
//        @Part("id") id: String,
//        @Part("latitude") latitude: Double,
//        @Part("longitude") longitude: Double,
//        @Part("quality") quality: Int,
//        @Part("agency") agency: String,
//        @Part("filename") filename: String,
//        @Part MultipartBody.Part filePart    ): Call<UploadResponse>
//}
