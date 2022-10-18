package com.example.colega.data.users

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class FollowingSource(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo
    val userId: String,
    @ColumnInfo
    val sourceId: String,
    @ColumnInfo
    val createdAt: String,
    @ColumnInfo
    val name: String,
    @ColumnInfo
    val description: String,
    @ColumnInfo
    val language: String,
    @ColumnInfo
    val country: String,
)
