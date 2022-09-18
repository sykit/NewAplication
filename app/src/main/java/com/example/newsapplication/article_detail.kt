package com.example.newsapplication

import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class article_detail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_detail)
        val url = intent.getStringExtra("url")
        val webview: WebView = findViewById(R.id.webview)
//        webview.settings.javaScriptEnabled = true
        webview.loadUrl(url!!)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this@article_detail.finish()
    }
}