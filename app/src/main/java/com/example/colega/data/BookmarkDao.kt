package com.example.colega.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BookmarkDao{
    @Query("SELECT * FROM bookmark WHERE userId == :userId")
    fun getAllBookmark(userId: Int): LiveData<List<Bookmark>>

    @Query("DELETE FROM bookmark")
    fun deleteAll()

    @Insert
    fun insertBookmark(bookmark: Bookmark)

    @Delete
    fun deleteBookmark(bookmark: Bookmark)
}