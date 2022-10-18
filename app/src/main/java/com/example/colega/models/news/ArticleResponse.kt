package com.example.colega.models.news


import com.google.gson.annotations.SerializedName

data class ArticleResponse(
    @SerializedName("author")
    val author: String,
    @SerializedName("content")
    val content: String?,
    @SerializedName("description")
    val description: String,
    @SerializedName("publishedAt")
    val publishedAt: String,
    @SerializedName("source")
    val articleSource: ArticleSource,
    @SerializedName("title")
    val title: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("urlToImage")
    val urlToImage: String,
    var isCheck: Boolean = false
)