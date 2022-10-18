package com.example.colega.data.users

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {
    @Query("SELECT * FROM user WHERE username LIKE :username")
    fun findUser(username:String): LiveData<User>

    @Query("SELECT COUNT() FROM user WHERE username == :username")
    fun countExistUser(username: String): Int

    @Insert
    fun addUser(user: User)

    @Update
    fun updateUser(user: User)

    @Delete
    fun deleteUser(user: User)
}