package com.example.colega.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.colega.data.User
import com.example.colega.data.UserRepository
import com.example.colega.db.MyDatabase
import com.example.colega.preferences.UserPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application): AndroidViewModel(application) {
    private val prefRepo = UserPreferencesRepository(application)
    val dataUser = prefRepo.readData.asLiveData()
    private val repository: UserRepository

    init {
        val userDao = MyDatabase.getDatabase(
            application
        ).userDao()
        repository = UserRepository(userDao)
    }

    fun addUser(user: User){
        viewModelScope.launch(Dispatchers.IO){ repository.addUser(user) }
    }

    fun updateUser(user: User){
        viewModelScope.launch(Dispatchers.IO){ repository.updateUser(user) }
    }

    fun deleteUser(user: User){
        viewModelScope.launch(Dispatchers.IO){ repository.deleteUser(user) }
    }

    fun findUser(username:String): LiveData<User> {
        return repository.findUser(username)
    }

    fun isUserExist(username: String): Int{
        return repository.countUser(username)
    }

    fun addToUserPref(user: User){
        viewModelScope.launch { prefRepo.saveData(user) }
    }
}