package com.example.colega.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.colega.api.UserService
import com.example.colega.models.user.DataFollowingSource
import com.example.colega.models.user.UserFollowingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

private const val TAG = "FollowingSourceViewModel"

@HiltViewModel
class FollowingSourceViewModel @Inject constructor(private val userService: UserService): ViewModel() {
    private val allUserFollowingSource: MutableLiveData<List<UserFollowingSource>?> = MutableLiveData()
    private val postFollowingSource: MutableLiveData<UserFollowingSource?> = MutableLiveData()
    private val deleteFollowingSource: MutableLiveData<UserFollowingSource?> = MutableLiveData()
    private val singleSource: MutableLiveData<UserFollowingSource> = MutableLiveData()
    fun getFollowingSource(): MutableLiveData<List<UserFollowingSource>?> = allUserFollowingSource
    fun getPostFollowingSource(): MutableLiveData<UserFollowingSource?> = postFollowingSource
    fun getDeleteFollowingSource():MutableLiveData<UserFollowingSource?> = deleteFollowingSource
    fun getSingleSource():MutableLiveData<UserFollowingSource> = singleSource


    // API
    fun getFollowingFromApi(userId: String){
        userService.getUserFollowingSource(userId)
            .enqueue(object : Callback<List<UserFollowingSource>>{
                override fun onResponse(
                    call: Call<List<UserFollowingSource>>,
                    response: Response<List<UserFollowingSource>>
                ) {
                    if(response.isSuccessful){
                        if(response.body() != null){
                            allUserFollowingSource.postValue(response.body())
                        }
                    }else{
                        Log.d(TAG, "onResponse: Unsuccessfully")
                        allUserFollowingSource.postValue(null)
                    }
                }

                override fun onFailure(call: Call<List<UserFollowingSource>>, t: Throwable) {
                    Log.d(TAG, "onFailure: ${t.message}")
                }

            })
    }

    fun postFollowingSourceToApi(userId: String, dataFollowingSource: DataFollowingSource){
        userService.postFollowingSource(userId, dataFollowingSource)
            .enqueue(object : Callback<UserFollowingSource>{
                override fun onResponse(
                    call: Call<UserFollowingSource>,
                    response: Response<UserFollowingSource>
                ) {
                    if(response.isSuccessful){
                        postFollowingSource.postValue(response.body())
                    }else{
                        Log.d(TAG, "onResponse: Post Unsuccessfully")
                        postFollowingSource.postValue(null)
                    }
                }

                override fun onFailure(call: Call<UserFollowingSource>, t: Throwable) {
                    Log.d(TAG, "onFailure: Post ${t.message}")
                    postFollowingSource.postValue(null)
                }

            })
    }

    fun deleteFollowingFromApi(userId: String, id: String){
        userService.deleteFollowingSource(userId, id)
            .enqueue(object : Callback<UserFollowingSource>{
                override fun onResponse(
                    call: Call<UserFollowingSource>,
                    response: Response<UserFollowingSource>
                ) {
                    if(response.isSuccessful){
                        deleteFollowingSource.postValue(response.body())
                    }else{
                        Log.d(TAG, "onResponse: Delete Unsuccessfully")
                        deleteFollowingSource.postValue(null)
                    }
                }

                override fun onFailure(call: Call<UserFollowingSource>, t: Throwable) {
                    Log.d(TAG, "onFailure: Delete ${t.message}")
                    deleteFollowingSource.postValue(null)
                }

            })
    }

    fun getSingleSourceFromApi(userId: String, sourceId: String){
        userService.getSingleSource(userId, sourceId)
            .enqueue(object : Callback<List<UserFollowingSource>>{

                override fun onResponse(
                    call: Call<List<UserFollowingSource>>,
                    response: Response<List<UserFollowingSource>>
                ) {
                    if(response.isSuccessful){
                        response.body()?.let {
                            singleSource.postValue(it.first())
                        }
                    }else{
                        Log.d(TAG, "onResponse: Delete Unsuccessfully")
                    }
                }

                override fun onFailure(call: Call<List<UserFollowingSource>>, t: Throwable) {
                    Log.d(TAG, "onFailure: Delete ${t.message}")
                }

            })
    }
}