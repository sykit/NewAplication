package com.example.newsapplication

class DataBerita (title: String?,url: String?, imgurl: String?) {
    private var title: String
    private var url: String
    private var imgurl: String
    init {
        this.title = title!!
        this.url = url!!
        this.imgurl = imgurl!!

    }
    fun gettitle(): String? {
        return title
    }
    fun geturl(): String? {
        return url
    }
    fun getimgurl(): String? {
        return imgurl
    }
}