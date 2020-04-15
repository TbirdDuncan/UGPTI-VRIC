package com.example.nav.data

import android.graphics.Bitmap
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import com.example.nav.data.UploadResponse
import retrofit2.Call
import retrofit2.http.Query

interface ApiService {
    //params
    @POST("RIC/upload1.php")
    fun uploadBitmap(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("id") id: String,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("quality") quality: Int,
        @Query("agency") agency: String,
        @Query("image") image: String,
        @Query("filename") filename: String
    ): Call<UploadResponse>

}
//params.put("username", "RIC");
//params.put("password", "@RICsdP4T");
//params.put("id", sp.date);
//params.put("latitude",sp.lat);
//params.put("longitude",sp.lon);
//params.put("quality", quality);
//params.put("agency", agency);
//params.put("image", encodedString);
//params.put("filename",sp.date+".jpg");