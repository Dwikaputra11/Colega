package com.example.colega.data.source

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Source(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo
    val name: String,
    @ColumnInfo
    val category: String,
    @ColumnInfo
    val country: String,
    @ColumnInfo
    val description: String,
    @ColumnInfo
    val language: String,
    @ColumnInfo
    val url: String,
    @ColumnInfo
    var isFollow: Boolean = false,
    @ColumnInfo
    val userId: String,
    @ColumnInfo
    val sourceId: String,
)
