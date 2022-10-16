package com.example.colega.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.colega.api.RetrofitClient
import com.example.colega.data.DataUser
import com.example.colega.data.User
import com.example.colega.data.UserRepository
import com.example.colega.db.MyDatabase
import com.example.colega.models.user.UserResponseItem
import com.example.colega.preferences.UserPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "UserViewModel"

class UserViewModel(application: Application): AndroidViewModel(application) {
    private val prefRepo = UserPreferencesRepository(application)
    val dataUser = prefRepo.readData.asLiveData()
    private val repository: UserRepository
    private val activeUser: MutableLiveData<UserResponseItem> = MutableLiveData()
    private val postUser: MutableLiveData<UserResponseItem> = MutableLiveData()

    init {
        val userDao = MyDatabase.getDatabase(
            application
        ).userDao()
        repository = UserRepository(userDao)
    }

    fun getUser(): MutableLiveData<UserResponseItem>{
        return activeUser
    }

    fun getUserResponse(username: String){
        RetrofitClient.instanceUser.getAllUsers()
            .enqueue(object: Callback<List<UserResponseItem>>{
                override fun onResponse(
                    call: Call<List<UserResponseItem>>,
                    response: Response<List<UserResponseItem>>
                ) {
                    if (response.isSuccessful) {
                        Log.d("Login", "onResponse: ${response.body()}")
                        val userList = response.body()?.filter {
                            it.username == username
                        } as List<UserResponseItem>
                        if (!userList.indices.isEmpty()) {
                            val user = userList.first {
                                it.username == username
                            }
                            activeUser.postValue(user)
                            Log.d("Login", "onResponse: $user")
                        } else {
                            Log.d("Login Activity", "onResponse: Gagal")
                        }
                    }
                }

                override fun onFailure(call: Call<List<UserResponseItem>>, t: Throwable) {
                    Log.d(TAG, "onFailure: Faild to login")
                }

            })
    }

    fun getPostUser(): MutableLiveData<UserResponseItem>{
        return postUser
    }

    fun postUser(user: DataUser){
        RetrofitClient.instanceUser.addUser(user)
            .enqueue(object :Callback<UserResponseItem>{
                override fun onResponse(
                    call: Call<UserResponseItem>,
                    response: Response<UserResponseItem>
                ) {
                    if(response.isSuccessful){
                        Log.d(TAG, "onResponse: ${response.body()}")
                        postUser.postValue(response.body())
                    }else{
                        Log.d(TAG, "onResponse: Failed Fetch")
                    }
                }

                override fun onFailure(call: Call<UserResponseItem>, t: Throwable) {
                    Log.d(TAG, "onFailure: Api Failed")
                }

            })
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

    fun addToUserPref(user: UserResponseItem){
        viewModelScope.launch { prefRepo.saveData(user) }
    }

    fun clearUserPref(){
        viewModelScope.launch { prefRepo.clearData() }
    }
}