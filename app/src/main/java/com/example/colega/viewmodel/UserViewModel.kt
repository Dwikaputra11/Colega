package com.example.colega.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.colega.api.UserService
import com.example.colega.models.user.DataUser
import com.example.colega.models.user.UserResponseItem
import com.example.colega.preferences.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

private const val TAG = "UserViewModel"

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userService: UserService,
    application: Application,
): ViewModel() {
    private val prefRepo = UserPreferencesRepository(application.applicationContext)
    val dataUser = prefRepo.readData.asLiveData()
    private val activeUser: MutableLiveData<UserResponseItem?> = MutableLiveData()
    private val allUsers: MutableLiveData<List<UserResponseItem>> = MutableLiveData()
    private val userData: MutableLiveData<UserResponseItem> = MutableLiveData()


    fun getUser(): MutableLiveData<UserResponseItem?> {
        return activeUser
    }

    fun getUserResponse(username: String){
        userService.getUserByUsername(username)
            .enqueue(object: Callback<List<UserResponseItem>>{
                override fun onResponse(
                    call: Call<List<UserResponseItem>>,
                    response: Response<List<UserResponseItem>>
                ) {
                    if (response.isSuccessful) {
                        Log.d("Login", "onResponse: ${response.body()}")
                        response.body()?.let { user ->
                            val isExist =  user.find { username == it.username }
                            if(isExist != null)
                                activeUser.postValue(isExist)
                            else
                                Log.d(TAG, "onResponse: User Not Exist")
                        }
                    }
                }

                override fun onFailure(call: Call<List<UserResponseItem>>, t: Throwable) {
                    Log.d(TAG, "onFailure: Failed to login")
                }

            })
    }

    fun getUserData(): MutableLiveData<UserResponseItem>{
        return userData
    }

    fun postUser(user: DataUser){
        userService.addUser(user)
            .enqueue(object :Callback<UserResponseItem>{
                override fun onResponse(
                    call: Call<UserResponseItem>,
                    response: Response<UserResponseItem>
                ) {
                    if(response.isSuccessful){
                        Log.d(TAG, "onResponse: ${response.body()}")
                        userData.postValue(response.body())
                    }else{
                        Log.d(TAG, "onResponse: Failed Fetch")
                    }
                }

                override fun onFailure(call: Call<UserResponseItem>, t: Throwable) {
                    Log.d(TAG, "onFailure: Api Failed")
                }

            })
    }

    fun updateUser(userId: String,user: DataUser){
        userService.updateUser(userId,user)
            .enqueue(object : Callback<UserResponseItem>{
                override fun onResponse(
                    call: Call<UserResponseItem>,
                    response: Response<UserResponseItem>
                ) {
                    if(response.isSuccessful){
                        Log.d(TAG, "onResponse Update: Successfully")
                        userData.postValue(response.body())
                    }
                }
                override fun onFailure(call: Call<UserResponseItem>, t: Throwable) {
                    Log.d(TAG, "onFailure: Update ${t.message}")
                }

            })
    }

    fun addToUserPref(user: UserResponseItem){
        viewModelScope.launch { prefRepo.saveData(user) }
    }

    fun clearUserPref(){
        viewModelScope.launch { prefRepo.clearData() }
    }
}