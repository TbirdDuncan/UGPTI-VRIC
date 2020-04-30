package com.example.nav.data
import android.app.Activity
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.nav.data.AsyncTaskCompleteListener
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.ContentType
import org.apache.http.entity.mime.MIME
import org.apache.http.entity.mime.MultipartEntityBuilder
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.params.HttpConnectionParams
import org.apache.http.util.EntityUtils
import java.io.File


class MultiPartRequester(
    private val fragment: Fragment, private val map: MutableMap<String, String>,
    asyncTaskCompleteListener: AsyncTaskCompleteListener
) {
    private var mAsynclistener: AsyncTaskCompleteListener? = null
    private var httpclient: HttpClient? = null
    private var request: AsyncHttpRequest? = null

    init {

        // is Internet Connection Available...


        mAsynclistener = asyncTaskCompleteListener as AsyncTaskCompleteListener
        request = AsyncHttpRequest().execute(map["url"]) as AsyncHttpRequest
    }

    internal inner class AsyncHttpRequest : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg urls: String): String? {
            map.remove("url")
            try {

                val httppost = HttpPost(urls[0])
                httpclient = DefaultHttpClient()

                HttpConnectionParams.setConnectionTimeout(
                    httpclient!!.getParams(), 600000
                )

                val builder = MultipartEntityBuilder
                    .create()

                for (key in map.keys) {

                    if (key.equals("filename", ignoreCase = true)) {
                        val f = File(map[key])

                        builder.addBinaryBody(
                            key, f,
                            ContentType.MULTIPART_FORM_DATA, f.getName()
                        )
                    } else {
                        builder.addTextBody(
                            key, map[key], ContentType
                                .create("text/plain", MIME.DEFAULT_CHARSET)
                        )
                    }
                    Log.d("TAG", key + "---->" + map[key])
                    // System.out.println(key + "---->" + map.get(key));
                }

                httppost.setEntity(builder.build())



                val response = httpclient!!.execute(httppost)

                return EntityUtils.toString(
                    response.getEntity(), "UTF-8"
                )

            } catch (e: Exception) {
                e.printStackTrace()
            } catch (oume: OutOfMemoryError) {
                System.gc()


            } finally {
                if (httpclient != null)
                    httpclient!!.getConnectionManager().shutdown()

            }
            return null
        }

        override fun onPostExecute(response: String) {

            if (mAsynclistener != null) {
                mAsynclistener!!.onTaskCompleted(response)
            }
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(fragment.context, msg, Toast.LENGTH_SHORT).show()
    }

}