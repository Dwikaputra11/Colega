package com.example.colega.data.users

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FollowingSourceDao {

    @Query("SELECT * FROM followingsource WHERE userId == :userId")
    fun getNewsBySource(userId: String): LiveData<List<FollowingSource>>

    @Query("DELETE FROM followingsource")
    fun deleteAll()

    @Insert
    fun insertUserFollow(followingSource: FollowingSource)

    @Delete
    fun deleteUserFollow(followingSource: FollowingSource)

}