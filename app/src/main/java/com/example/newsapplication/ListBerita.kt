package com.example.newsapplication

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import java.io.IOException
import java.net.URLDecoder
import kotlin.math.log

class ListBerita : AppCompatActivity() {
    private var DataBerita = ArrayList<DataBerita>()
    private lateinit var BeritaAdapter: BeritaAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_berita)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)!!
        BeritaAdapter = BeritaAdapter(DataBerita)
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = BeritaAdapter
        val category=intent.getStringExtra("category")
        Log.d("test",category!!)
        run(category,null)
        val imageButton : ImageButton = findViewById(R.id.imageButton)
        val search : EditText = findViewById(R.id.editTextTextPersonName2)
        imageButton.setOnClickListener {
            run(category,search.text.toString())
        }
    }
    private val client = OkHttpClient()

    private fun run(category: String?, search: String?) {
        var param = ""
        if (!category.isNullOrEmpty()){
            param = "&category="+category
        }
        var url=""
        if (!search.isNullOrEmpty()){
            param = "&q="+ URLDecoder.decode(search, "UTF-8")
            url = "https://newsapi.org/v2/everything?apiKey=b262e11af7e94a939718989e212602c8"+param
            Log.d("test","search")
        }else{
            url = "https://newsapi.org/v2/top-headlines?country=us&apiKey=b262e11af7e94a939718989e212602c8"+param
        }

        Log.d("test",url)
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
                    runOnUiThread {
                        prepareDataBerita(JSONArray)
                    }


                }
            }
        })
    }


    private fun prepareDataBerita(jsonArray: JSONArray) {
        DataBerita.clear()
        for (i in 0 until jsonArray.length()){
            var item = jsonArray.getJSONObject(i)
            var berita = DataBerita(item.getString("title"),item.getString("url"), item.getString("urlToImage"))
            DataBerita.add(berita)
        }
        Log.d("test",DataBerita.toString())
        BeritaAdapter.notifyDataSetChanged()
    }

    @SuppressLint("StaticFieldLeak")
    @Suppress("DEPRECATION")
    private inner class DownloadImageFromInternet(var imageView: ImageView) : AsyncTask<String, Void, Bitmap?>() {
        init {
            this@ListBerita.runOnUiThread {
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

    override fun onBackPressed() {
        this.finish()
        super.onBackPressed()
    }
}