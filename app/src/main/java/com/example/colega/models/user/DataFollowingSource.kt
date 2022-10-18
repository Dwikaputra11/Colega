package com.example.colega.models.user

data class DataFollowingSource(
    val name: String,
    val userId: String,
    val sourceId: String,
    val description: String,
    val language: String,
    val country: String,
)