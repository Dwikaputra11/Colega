package com.example.colega.data.source

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface SourceDao {
    @Query("SELECT * FROM source")
    fun getAllSource(): LiveData<List<Source>>

    @Query("DELETE FROM source")
    fun deleteAllSource()

    @Query("SELECT COUNT(*) FROM source")
    fun countSourceSize(): Int

    @Insert
    fun postSource(sources: List<Source>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateSource(source: Source)
}