package com.example.colega.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.colega.api.RetrofitClient
import com.example.colega.data.Bookmark
import com.example.colega.data.BookmarkRepository
import com.example.colega.db.MyDatabase
import com.example.colega.models.user.UserBookmark
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "BookmarkViewModel"

class BookmarkViewModel(application: Application): AndroidViewModel(application) {
    private val repository: BookmarkRepository
    private val getAllUserBookmark: MutableLiveData<List<UserBookmark>?> = MutableLiveData()
    private val postUserBookmark: MutableLiveData<UserBookmark> = MutableLiveData()
    private val deleteUserBookmark: MutableLiveData<UserBookmark> = MutableLiveData()

    init {
        val bookmarkDao = MyDatabase.getDatabase(application)
            .bookmarkDao()
        repository = BookmarkRepository(bookmarkDao)
    }

    fun getUserBookmark(): MutableLiveData<List<UserBookmark>?> = getAllUserBookmark

    fun postUserBookmark(): MutableLiveData<UserBookmark> = postUserBookmark

    fun deleteUserBookmark(): MutableLiveData<UserBookmark> = deleteUserBookmark

    fun getBookmarkFromApi(userId: String){
        RetrofitClient.instanceUser.getUserBookmark(userId)
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
        RetrofitClient.instanceUser.postBookmark(bookmark.userId.toString(), bookmark)
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
        RetrofitClient.instanceUser.deleteBookmark(userId, id)
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


    fun getAllBookmark(userId: Int): LiveData<List<Bookmark>> = repository.getAllBookmark(userId)

    fun insertBookmark(bookmark: Bookmark){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertBookmark(bookmark)
        }
    }

    fun updateBookmark(bookmark: Bookmark){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateBookmark(bookmark)
        }
    }

    fun deleteAllBookmark(){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteAllBookmark()
        }
    }

    fun deleteBookmark(bookmark: Bookmark){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteBookmark(bookmark)
        }
    }
}