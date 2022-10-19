package com.example.colega.data.source

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SourceDao {
    @Query("SELECT * FROM source")
    fun getAllSource(): LiveData<List<Source>>

    @Query("DELETE FROM user")
    fun deleteAllSource()

    @Insert
    fun postSource(sources: List<Source>)
}