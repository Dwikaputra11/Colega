package com.example.colega.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.colega.api.RetrofitClient
import com.example.colega.data.FollowingSource
import com.example.colega.models.user.UserFollowingSource
import com.example.colega.models.user.UserResponseItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "FollowingSourceViewModel"

class FollowingSourceViewModel(application: Application): AndroidViewModel(application) {
    private val allUserFollowingSource: MutableLiveData<List<UserFollowingSource>?> = MutableLiveData()
    private val postFollowingSource: MutableLiveData<UserFollowingSource?> = MutableLiveData()
    private val deleteFollowingSource: MutableLiveData<UserFollowingSource?> = MutableLiveData()
    private val singleSource: MutableLiveData<List<UserFollowingSource>?> = MutableLiveData()

    fun getFollowingSource(): MutableLiveData<List<UserFollowingSource>?> = allUserFollowingSource
    fun getPostFollowingSource(): MutableLiveData<UserFollowingSource?> = postFollowingSource
    fun getDeleteFollowingSource():MutableLiveData<UserFollowingSource?> = deleteFollowingSource
    fun getSingleSource():MutableLiveData<List<UserFollowingSource>?> = singleSource

    fun getFollowingFromApi(userId: String){
        RetrofitClient.instanceUser.getUserFollowingSource(userId)
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

    fun postFollowingSourceToApi(userId: String,followingSource: FollowingSource){
        RetrofitClient.instanceUser.postFollowingSource(userId, followingSource)
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

    fun deleteFollowingSource(userId: String, id: String){
        RetrofitClient.instanceUser.deleteFollowingSource(userId, id)
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
        RetrofitClient.instanceUser.getSingleSource(userId, sourceId)
            .enqueue(object : Callback<List<UserFollowingSource>>{

                override fun onResponse(
                    call: Call<List<UserFollowingSource>>,
                    response: Response<List<UserFollowingSource>>
                ) {
                    if(response.isSuccessful){
                        singleSource.postValue(response.body())
                    }else{
                        Log.d(TAG, "onResponse: Delete Unsuccessfully")
                        singleSource.postValue(null)
                    }
                }

                override fun onFailure(call: Call<List<UserFollowingSource>>, t: Throwable) {
                    Log.d(TAG, "onFailure: Delete ${t.message}")
                    singleSource.postValue(null)
                }

            })
    }


}