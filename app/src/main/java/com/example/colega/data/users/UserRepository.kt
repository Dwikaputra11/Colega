package com.example.colega.data.users

import androidx.lifecycle.LiveData
import com.example.colega.db.MyDatabase

class UserRepository(private val userDao: UserDao) {

    fun findUser(username:String): LiveData<User> = userDao.findUser(username)

    fun countUser(username: String): Int = userDao.countExistUser(username)

    fun addUser(user: User){
        MyDatabase.databaseWriteExecutor.execute { userDao.addUser(user)}
    }

    fun updateUser(user: User){
        MyDatabase.databaseWriteExecutor.execute { userDao.updateUser(user) }
    }

    fun  deleteUser(user: User){
        MyDatabase.databaseWriteExecutor.execute { userDao.deleteUser(user) }
    }
}