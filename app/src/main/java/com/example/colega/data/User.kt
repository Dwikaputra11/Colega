package com.example.colega.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo
    val username: String,
    @ColumnInfo
    var email: String,
    @ColumnInfo
    val birthDate: String,
    @ColumnInfo
    val password: String,
)