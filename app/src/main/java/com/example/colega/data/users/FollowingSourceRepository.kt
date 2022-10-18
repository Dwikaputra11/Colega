package com.example.colega.data.users

import com.example.colega.db.MyDatabase

class FollowingSourceRepository(private val followingSourceDao: FollowingSourceDao) {

    fun getAllNewsBySource(userId: String) = followingSourceDao.getNewsBySource(userId)

    fun insertUserFollow(followingSource: FollowingSource){
        MyDatabase.databaseWriteExecutor.execute {
            followingSourceDao.insertUserFollow(followingSource)
        }
    }

    fun deleteUserFollow(followingSource: FollowingSource){
        MyDatabase.databaseWriteExecutor.execute {
            followingSourceDao.deleteUserFollow(followingSource)
        }
    }
}