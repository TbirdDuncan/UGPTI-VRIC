package com.example.nav.data

import okhttp3.*


class ImageHeaderIntercepter:Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = Headers.Builder()
        builder.add("Content-Type", "bitmap, charset=utf-8")
        return chain.proceed(chain.request().newBuilder().headers(builder.build()).build())
    }
}

//val JSON: MediaType = MediaType.get("application/json; charset=utf-8")


