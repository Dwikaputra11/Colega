package com.example.colega.models.news


import com.google.gson.annotations.SerializedName

data class SourceResponse(
    @SerializedName("sources")
    val sources: List<SourceResponseItem>,
    @SerializedName("status")
    val status: String
)