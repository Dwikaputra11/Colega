package com.example.colega.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.colega.models.Source

@Entity
data class Bookmark(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo
    val userId: Int,
    @ColumnInfo
    val author: String?,
    @ColumnInfo
    val content: String?,
    @ColumnInfo
    val description: String?,
    @ColumnInfo
    val publishedAt: String?,
    @ColumnInfo
    val source: String?,
    @ColumnInfo
    val title: String?,
    @ColumnInfo
    val url: String?,
    @ColumnInfo
    val urlToImage: String?
)
