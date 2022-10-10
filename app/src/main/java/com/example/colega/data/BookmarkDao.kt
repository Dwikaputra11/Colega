package com.example.colega.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BookmarkDao{
    @Query("SELECT * FROM bookmark WHERE bookmark.userId == :userId")
    fun getAllBookmark(userId: Int): LiveData<List<Bookmark>>

    @Query("DELETE FROM bookmark")
    fun deleteAll()

    @Update
    fun updateBookmark(bookmark: Bookmark)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBookmark(bookmark: Bookmark)

    @Delete
    fun deleteBookmark(bookmark: Bookmark)
}