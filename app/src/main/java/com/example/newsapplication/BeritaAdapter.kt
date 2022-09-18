package com.example.newsapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView

internal class BeritaAdapter(private var DataBerita: List<DataBerita>) :
    RecyclerView.Adapter<BeritaAdapter.MyViewHolder>() {
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.textView3) as TextView
        val imageView: ImageView = view.findViewById(R.id.imageView) as ImageView
//        var nama: TextView = view.findViewById(R.id.nama)
//        var tahun: TextView = view.findViewById(R.id.tahun)
//        var jenis: TextView = view.findViewById(R.id.jenis)
    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_berita, parent, false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val berita = DataBerita[position]

        DownloadImageFromInternet(holder.imageView).execute(berita.getimgurl())
        holder.textView.text = berita.gettitle()
        holder.imageView.setOnClickListener { v ->
            val intent = Intent(v.context, article_detail::class.java)
            intent.putExtra("url", berita.geturl())

            v.context.startActivity(intent)

        }
    }
    override fun getItemCount(): Int {
        return DataBerita.size
    }

    @SuppressLint("StaticFieldLeak")
    @Suppress("DEPRECATION")
    private inner class DownloadImageFromInternet(var imageView: ImageView) : AsyncTask<String, Void, Bitmap?>() {

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

}