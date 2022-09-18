package com.example.newsapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.newsapplication.R.id.imageView
import com.squareup.picasso.Picasso
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import java.io.IOException
import java.net.URL


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val allCategory :Button = findViewById(R.id.button7)
        allCategory.setOnClickListener {
            opeListArticle("")
        }
        val general :Button = findViewById(R.id.button3)
        general.setOnClickListener {
            opeListArticle("general")
        }
        val business :Button = findViewById(R.id.button)
        business.setOnClickListener {
            opeListArticle("business")
        }
        val entertainment :Button = findViewById(R.id.button2)
        entertainment.setOnClickListener {
            opeListArticle("business")
        }
        val health :Button = findViewById(R.id.button4)
        health.setOnClickListener {
            opeListArticle("health")
        }
        val science :Button = findViewById(R.id.button5)
        science.setOnClickListener {
            opeListArticle("science")
        }
        val sports :Button = findViewById(R.id.button6)
        sports.setOnClickListener {
            opeListArticle("sports")
        }
        val technology :Button = findViewById(R.id.button8)
        technology.setOnClickListener {
            opeListArticle("technology")
        }
        run()
    }
    @SuppressLint("StaticFieldLeak")
    @Suppress("DEPRECATION")
    private inner class DownloadImageFromInternet(var imageView: ImageView) : AsyncTask<String, Void, Bitmap?>() {
        init {
            this@MainActivity.runOnUiThread {
                Toast.makeText(applicationContext, "Please wait, it may take a few minute...",     Toast.LENGTH_SHORT).show()

            }
        }
        override fun doInBackground(vararg urls: String): Bitmap? {
            val imageURL = urls[0]
            var image: Bitmap? = null
            try {
                val `in` = java.net.URL(imageURL).openStream()
                image = BitmapFactory.decodeStream(`in`)
            }
            catch (e: Exception) {
                Log.e("Error Message", e.message.toString())
                e.printStackTrace()
            }
            return image
        }
        override fun onPostExecute(result: Bitmap?) {
            imageView.setImageBitmap(result)
        }
    }



    private val client = OkHttpClient()

    fun run() {

        var url = "https://newsapi.org/v2/top-headlines?country=us&apiKey=b262e11af7e94a939718989e212602c8"
        url = url
        val request = Request.Builder()
            .url(url)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("error",e.localizedMessage)
//                this@tampilan_awal.goLoginPage()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
//
//                    for ((name, value) in response.headers) {
//                        println("$name: $value")
//                    }
                    val hasil = response.body!!.string()
                    val jsonObject = JSONTokener(hasil).nextValue() as JSONObject
                    val JSONArray = jsonObject.getJSONArray("articles") as JSONArray

                    val articles = JSONArray.getJSONObject(0)
                    val imgurl = articles.getString("urlToImage")
                    val url = articles.getString("url")
                    val title = articles.getString("title")
                    val deskripsi = articles.getString("description")
                    val textView: TextView = findViewById(R.id.textView3) as TextView
                    val imageView: ImageView = findViewById(imageView) as ImageView
                    runOnUiThread {
                        textView.setText(title)
                        imageView.setOnClickListener {
                            openArticleDetail(url)
                        }
                        this@MainActivity.DownloadImageFromInternet(imageView).execute(imgurl)
                    }


                }
            }
        })
    }

    private fun openArticleDetail(url: String) {
        val intent = Intent(this@MainActivity, article_detail::class.java)
        intent.putExtra("url", url)
        startActivity(intent)
    }

    private fun opeListArticle(param: String) {
        val intent = Intent(this@MainActivity, ListBerita::class.java)
        intent.putExtra("category", param)
        startActivity(intent)
    }


}