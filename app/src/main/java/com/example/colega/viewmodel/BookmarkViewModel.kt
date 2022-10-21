package com.example.colega.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.colega.api.UserService
import com.example.colega.data.users.Bookmark
import com.example.colega.models.user.UserBookmark
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

private const val TAG = "BookmarkViewModel"

@HiltViewModel
class BookmarkViewModel @Inject constructor(private val userService: UserService): ViewModel() {
    private val getAllUserBookmark: MutableLiveData<List<UserBookmark>?> = MutableLiveData()
    private val postUserBookmark: MutableLiveData<UserBookmark> = MutableLiveData()
    private val deleteUserBookmark: MutableLiveData<UserBookmark> = MutableLiveData()


    fun getUserBookmark(): MutableLiveData<List<UserBookmark>?> = getAllUserBookmark

    fun postUserBookmark(): MutableLiveData<UserBookmark> = postUserBookmark

    fun deleteUserBookmark(): MutableLiveData<UserBookmark> = deleteUserBookmark

    fun getBookmarkFromApi(userId: String){
        userService.getUserBookmark(userId)
            .enqueue(object : Callback<List<UserBookmark>>{
                override fun onResponse(
                    call: Call<List<UserBookmark>>,
                    response: Response<List<UserBookmark>>
                ) {
                    if(response.isSuccessful){
                        if(response.body() != null){
                            getAllUserBookmark.postValue(response.body())
                            Log.d(TAG, "onResponse: Successfully")
                        }else{
                            getAllUserBookmark.postValue(null)
                            Log.d(TAG, "onResponse: Bookmark null")
                        }
                    }
                }

                override fun onFailure(call: Call<List<UserBookmark>>, t: Throwable) {
                    Log.d(TAG, "onFailure: Something Wrong")
                }

            })
    }

    fun postBookmarkToApi(bookmark: Bookmark){
        userService.postBookmark(bookmark.userId.toString(), bookmark)
            .enqueue(object : Callback<UserBookmark>{
                override fun onResponse(
                    call: Call<UserBookmark>,
                    response: Response<UserBookmark>
                ) {
                    if(response.isSuccessful){
                        postUserBookmark.postValue(response.body())
                    }else{
                        Log.d(TAG, "onResponse: Not Success")
                    }
                }

                override fun onFailure(call: Call<UserBookmark>, t: Throwable) {
                    Log.d(TAG, "onFailure: Something Wrong Post")
                }

            })
    }

    fun deleteBookmarkUserFromApi(userId: String, id: String){
        userService.deleteBookmark(userId, id)
            .enqueue(object: Callback<UserBookmark>{
                override fun onResponse(
                    call: Call<UserBookmark>,
                    response: Response<UserBookmark>
                ) {
                    if(response.isSuccessful){
                        deleteUserBookmark.postValue(response.body())
                    }else{
                        Log.d(TAG, "onResponse: Delete Failed")
                    }
                }

                override fun onFailure(call: Call<UserBookmark>, t: Throwable) {
                    Log.d(TAG, "onFailure: Something Wrong")
                }
            })
    }
}