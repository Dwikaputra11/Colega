package com.example.colega.models.news


import com.google.gson.annotations.SerializedName

data class NewsModel(
    @SerializedName("articles")
    val articleResponses: List<ArticleResponse>,
    @SerializedName("status")
    val status: String,
    @SerializedName("totalResults")
    val totalResults: Int
)